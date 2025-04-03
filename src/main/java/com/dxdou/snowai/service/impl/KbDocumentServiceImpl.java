package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.dto.KbDocumentDTO;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.entity.KbDocumentIndex;
import com.dxdou.snowai.domain.entity.KbDocumentTag;
import com.dxdou.snowai.domain.entity.KbDocumentVersion;
import com.dxdou.snowai.domain.vo.KbDocumentVO;
import com.dxdou.snowai.domain.vo.KbDocumentVersionVO;
import com.dxdou.snowai.domain.vo.KbTagVO;
import com.dxdou.snowai.mapper.KbDocumentMapper;
import com.dxdou.snowai.mapper.KbDocumentTagMapper;
import com.dxdou.snowai.mapper.KbDocumentVectorMapper;
import com.dxdou.snowai.mapper.KbDocumentVersionMapper;
import com.dxdou.snowai.service.KbDocumentService;
import com.dxdou.snowai.service.KbSearchService;
import com.dxdou.snowai.service.MinioService;
import com.dxdou.snowai.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识库文档服务实现类
 *
 * @author foreverdxdou
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KbDocumentServiceImpl extends ServiceImpl<KbDocumentMapper, KbDocument> implements KbDocumentService {

    private final KbDocumentMapper documentMapper;
    private final KbDocumentVersionMapper documentVersionMapper;
    private final KbDocumentTagMapper documentTagMapper;
    private final KbDocumentVectorMapper documentVectorMapper;
    private final MinioService minioService;
    private final KbSearchService searchService;
    private final SystemConfigService systemConfigService;
    private final ElasticsearchOperations elasticsearchTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbDocumentVO uploadDocument(MultipartFile file, Long kbId, Long creatorId, List<Long> tagIds) {
        try {
            // 1. 保存文件到MinIO
            String fileUrl = uploadFileToMinio(file);

            // 2. 解析文档内容
            String content = parseDocumentContent(file);

            KbDocument dbDocument = documentMapper
                    .selectOne(Wrappers.lambdaQuery(KbDocument.class)
                            .eq(KbDocument::getTitle, file.getOriginalFilename())
                            .eq(KbDocument::getKbId, kbId));
            if (dbDocument != null) {
                KbDocumentDTO dto = new KbDocumentDTO();
                dto.setId(dbDocument.getId());
                dto.setTitle(file.getOriginalFilename());
                dto.setContent(content);
                dto.setKbId(kbId);
                dto.setTagIds(tagIds);
                return this.updateDocument(dbDocument.getId(), dto);
            }

            // 3. 保存文档信息
            KbDocument document = new KbDocument();
            document.setTitle(file.getOriginalFilename());
            document.setContent(content);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setFileUrl(fileUrl);
            document.setKbId(kbId);
            document.setVersion(1);
            document.setStatus(1);
            document.setCreatorId(creatorId);
            documentMapper.insert(document);

            // 4. 保存文档版本
            saveDocumentVersion(document);

            // 5. 保存文档标签关联
            if (tagIds != null && !tagIds.isEmpty()) {
                saveDocumentTags(document.getId(), tagIds);
            }

            // 4. 异步处理文档解析
            processDocumentAsync(document.getId());

            // 5. 转换为VO并返回
            return convertToVO(document);
        } catch (Exception e) {
            log.error("上传文档失败", e);
            throw new BusinessException("上传文档失败：" + e.getMessage());
        }
    }

    @Async
    protected void processDocumentAsync(Long documentId) {
        try {
            // 1. 获取文档信息
            KbDocument document = documentMapper.selectById(documentId);
            if (document == null) {
                throw new BusinessException("文档不存在");
            }

            // 2. 获取系统配置，判断使用NLP还是Embedding
            String searchType = systemConfigService.getConfigValue("kb.search.type", "NLP");

            // 3. 根据配置选择处理方式
            if ("EMBEDDING".equals(searchType)) {
                // 使用Embedding处理
                searchService.updateDocumentVector(documentId, true);
            } else {
                // 使用NLP处理
                searchService.updateDocumentVector(documentId, false);
            }
            // 保存到ES
            saveToElasticsearch(document);

            // 4. 更新文档状态
            document.setParseStatus(2); // 解析成功
            document.setUpdateTime(LocalDateTime.now());
            documentMapper.updateById(document);

        } catch (Exception e) {
            log.error("处理文档失败，documentId: {}", documentId, e);
            // 更新文档状态为解析失败
            KbDocument document = new KbDocument();
            document.setId(documentId);
            document.setParseStatus(3); // 解析失败
            document.setParseError(e.getMessage());
            document.setUpdateTime(LocalDateTime.now());
            documentMapper.updateById(document);
        }
    }

    @Override
    public IPage<KbDocumentVO> getDocumentPage(Page<KbDocument> page, String title, Long kbId, Long categoryId,
            Long creatorId, Integer status) {
        return documentMapper.selectDocumentList(page, title, kbId, categoryId, creatorId, status);
    }

    @Override
    public KbDocumentVO getDocumentById(Long id) {
        KbDocument document = documentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }
        return convertToVO(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbDocumentVO updateDocument(Long id, KbDocumentDTO dto) {
        // 1. 获取文档信息
        KbDocument document = documentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }

        // 2. 更新文档信息
        BeanUtils.copyProperties(dto, document);
        document.setUpdateTime(LocalDateTime.now());
        documentMapper.updateById(document);

        // 3. 重新处理文档
        processDocumentAsync(id);

        return convertToVO(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long id) {
        KbDocument document = documentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }
        documentMapper.deleteById(id);
    }

    @Override
    public void updateDocumentStatus(Long id, Integer status) {
        KbDocument document = new KbDocument();
        document.setId(id);
        document.setStatus(status);
        documentMapper.updateById(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbDocumentVO rollbackVersion(Long id, Long versionId) {
        // 1. 查询版本信息
        KbDocumentVersion version = documentVersionMapper.selectById(versionId);
        if (version == null) {
            throw new BusinessException("版本不存在");
        }

        // 2. 更新文档信息
        KbDocument document = new KbDocument();
        document.setId(id);
        document.setTitle(version.getTitle());
        document.setContent(version.getContent());
        document.setFileUrl(version.getFileUrl());
        document.setVersion(version.getVersion());
        documentMapper.updateById(document);

        return getDocumentById(id);
    }

    @Override
    public List<KbDocumentVersionVO> getVersionHistory(Long documentId) {
        return documentVersionMapper.selectVersionHistory(documentId);
    }

    @Override
    public List<KbTagVO> getDocumentTags(Long documentId) {
        return documentMapper.selectDocumentTags(documentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDocumentTags(Long documentId, List<Long> tagIds) {
        // 1. 删除原有标签关联
        documentTagMapper.delete(new LambdaQueryWrapper<KbDocumentTag>()
                .eq(KbDocumentTag::getDocumentId, documentId));

        // 2. 保存新的标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            saveDocumentTags(documentId, tagIds);
        }
    }

    /**
     * 保存文档版本
     *
     * @param document 文档信息
     */
    private void saveDocumentVersion(KbDocument document) {
        KbDocumentVersion version = new KbDocumentVersion();
        version.setDocumentId(document.getId());
        version.setVersion(document.getVersion());
        version.setTitle(document.getTitle());
        version.setContent(document.getContent());
        version.setFileUrl(document.getFileUrl());
        version.setCreatorId(document.getCreatorId());
        documentVersionMapper.insert(version);
    }

    /**
     * 保存文档标签关联
     *
     * @param documentId 文档ID
     * @param tagIds     标签ID列表
     */
    private void saveDocumentTags(Long documentId, List<Long> tagIds) {
        List<KbDocumentTag> documentTags = tagIds.stream()
                .map(tagId -> {
                    KbDocumentTag documentTag = new KbDocumentTag();
                    documentTag.setDocumentId(documentId);
                    documentTag.setTagId(tagId);
                    return documentTag;
                })
                .toList();
        documentTagMapper.insertBatch(documentTags);
    }

    /**
     * 上传文件到MinIO
     *
     * @param file 文件
     * @return 文件URL
     */
    private String uploadFileToMinio(MultipartFile file) {
        return minioService.uploadFile(file, file.getOriginalFilename());
    }

    /**
     * 解析文档内容
     *
     * @param file 文件
     * @return 文档内容
     */
    private String parseDocumentContent(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

            switch (extension) {
                case "txt":
                    return new String(file.getBytes());
                case "doc":
                    return parseDoc(file.getInputStream());
                case "docx":
                    return parseDocx(file.getInputStream());
                case "pdf":
                    return parsePdf(file.getInputStream());
                case "md":
                    return new String(file.getBytes());
                case "ppt":
                    return parsePpt(file.getInputStream());
                case "pptx":
                    return parsePptx(file.getInputStream());
                default:
                    throw new BusinessException("不支持的文件类型：" + extension);
            }
        } catch (Exception e) {
            log.error("文档解析失败", e);
            throw new BusinessException("文档解析失败：" + e.getMessage());
        }
    }

    /**
     * 解析DOC文档
     */
    private String parseDoc(InputStream inputStream) throws IOException {
        try (HWPFDocument doc = new HWPFDocument(inputStream);
                WordExtractor extractor = new WordExtractor(doc)) {
            return extractor.getText();
        }
    }

    /**
     * 解析DOCX文档
     */
    private String parseDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    /**
     * 解析PDF文档
     */
    private String parsePdf(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * 解析PPT文档
     */
    private String parsePpt(InputStream inputStream) throws IOException {
        try (HSLFSlideShow ppt = new HSLFSlideShow(inputStream)) {
            StringBuilder content = new StringBuilder();
            for (HSLFSlide slide : ppt.getSlides()) {
                for (HSLFShape shape : slide.getShapes()) {
                    if (shape instanceof HSLFTextShape) {
                        content.append(((HSLFTextShape) shape).getText()).append("\n");
                    }
                }
            }
            return content.toString();
        }
    }

    /**
     * 解析PPTX文档
     */
    private String parsePptx(InputStream inputStream) throws IOException {
        try (XMLSlideShow pptx = new XMLSlideShow(inputStream)) {
            StringBuilder content = new StringBuilder();
            for (XSLFSlide slide : pptx.getSlides()) {
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        content.append(((XSLFTextShape) shape).getText()).append("\n");
                    }
                }
            }
            return content.toString();
        }
    }

    /**
     * 将文档内容分块
     *
     * @param content 文档内容
     * @return 文本块列表
     */
    private List<String> splitContentIntoChunks(String content) {
        List<String> chunks = new ArrayList<>();
        int chunkSize = 500; // 每个块的最大字符数
        int overlap = 100; // 块之间的重叠字符数

        int start = 0;
        while (start < content.length()) {
            int end = Math.min(start + chunkSize, content.length());

            // 如果不是最后一块，尝试在句子边界分割
            if (end < content.length()) {
                int lastPeriod = content.lastIndexOf("。", end);
                int lastQuestion = content.lastIndexOf("？", end);
                int lastExclamation = content.lastIndexOf("！", end);

                int lastBreak = Math.max(Math.max(lastPeriod, lastQuestion), lastExclamation);
                if (lastBreak > start) {
                    end = lastBreak + 1;
                }
            }

            chunks.add(content.substring(start, end));
            start = end - overlap;
        }

        return chunks;
    }

    /**
     * 保存文档到Elasticsearch
     *
     * @param document 文档信息
     */
    private void saveToElasticsearch(KbDocument document) {
        try {
            // 检查索引是否存在
            if (!elasticsearchTemplate.indexOps(KbDocumentIndex.class).exists()) {
                // 创建索引
                elasticsearchTemplate.indexOps(KbDocumentIndex.class).create();

                // 创建索引映射
                KbDocumentIndex doc = new KbDocumentIndex();
                doc.setId(document.getId().toString());
                doc.setTitle(document.getTitle());
                doc.setContent(document.getContent());
                doc.setKbId(document.getKbId().toString());
                doc.setCategoryId(document.getCategoryId() != null ? document.getCategoryId().toString() : null);
                doc.setCreateTime(document.getCreateTime().toString());
                doc.setUpdateTime(document.getUpdateTime().toString());

                // 保存文档，会自动创建映射
                elasticsearchTemplate.save(doc);
            } else {
                // 构建ES文档
                KbDocumentIndex doc = new KbDocumentIndex();
                doc.setId(document.getId().toString());
                doc.setTitle(document.getTitle());
                doc.setContent(document.getContent());
                doc.setKbId(document.getKbId().toString());
                doc.setCategoryId(document.getCategoryId() != null ? document.getCategoryId().toString() : null);
                doc.setCreateTime(document.getCreateTime().toString());
                doc.setUpdateTime(document.getUpdateTime().toString());

                // 保存到ES
                elasticsearchTemplate.save(doc);
            }
        } catch (Exception e) {
            log.error("保存文档到ES失败，documentId: {}", document.getId(), e);
            throw new BusinessException("保存文档到ES失败：" + e.getMessage());
        }
    }

    /**
     * 创建字段映射
     *
     * @param type 字段类型
     * @return 字段映射配置
     */
    private Map<String, Object> createFieldMapping(String type) {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("type", type);

        // 为text类型添加分词器配置
        if ("text".equals(type)) {
            Map<String, Object> analyzer = new HashMap<>();
            analyzer.put("analyzer", "ik_max_word");
            analyzer.put("search_analyzer", "ik_smart");
            mapping.putAll(analyzer);
        }

        return mapping;
    }

    /**
     * 将文档实体转换为VO对象
     *
     * @param document 文档实体
     * @return 文档VO
     */
    private KbDocumentVO convertToVO(KbDocument document) {
        KbDocumentVO vo = new KbDocumentVO();
        BeanUtils.copyProperties(document, vo);
        // TODO: 设置其他属性（如分类名称、知识库名称、创建者名称等）
        return vo;
    }

    @Override
    public long countByParseStatus(Integer status) {
        return documentMapper.countByParseStatus(status);
    }

    @Override
    public long countByCreateTimeAfter(LocalDateTime time) {
        return documentMapper.countByCreateTimeAfter(time);
    }

    @Override
    public List<KbDocument> getLatestDocs(int limit) {
        return documentMapper.selectLatestDocs(limit);
    }

    @Override
    public List<Long> getDocTrend(LocalDateTime startTime, LocalDateTime endTime) {
        return documentMapper.selectDocTrend(startTime, endTime);
    }
}