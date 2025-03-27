package com.dxdou.snowai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dxdou.snowai.common.BusinessException;
import com.dxdou.snowai.domain.entity.KbDocument;
import com.dxdou.snowai.domain.entity.KbDocumentTag;
import com.dxdou.snowai.domain.entity.KbDocumentVersion;
import com.dxdou.snowai.domain.vo.KbDocumentVO;
import com.dxdou.snowai.domain.vo.KbDocumentVersionVO;
import com.dxdou.snowai.domain.vo.KbTagVO;
import com.dxdou.snowai.mapper.KbDocumentMapper;
import com.dxdou.snowai.mapper.KbDocumentTagMapper;
import com.dxdou.snowai.mapper.KbDocumentVersionMapper;
import com.dxdou.snowai.service.KbDocumentService;
import com.dxdou.snowai.service.KbSearchService;
import com.dxdou.snowai.service.MinioService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 文档服务实现类
 *
 * @author foreverdxdou
 */
@Service
@RequiredArgsConstructor
public class KbDocumentServiceImpl extends ServiceImpl<KbDocumentMapper, KbDocument> implements KbDocumentService {

    private final KbDocumentMapper documentMapper;
    private final KbDocumentVersionMapper documentVersionMapper;
    private final KbDocumentTagMapper documentTagMapper;
    private final MinioService minioService;
    private final KbSearchService searchService;

    @Override
    public IPage<KbDocumentVO> getDocumentPage(Page<KbDocument> page, String title, Long kbId, Long categoryId,
            Long creatorId, Integer status) {
        return documentMapper.selectDocumentList(page, title, kbId, categoryId, creatorId, status);
    }

    @Override
    public KbDocumentVO getDocumentById(Long id) {
        return documentMapper.selectDocumentById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbDocumentVO uploadDocument(MultipartFile file, Long kbId, List<Long> tagIds, Long creatorId) {
        // 1. 保存文件到MinIO
        String fileUrl = uploadFileToMinio(file);

        // 2. 解析文档内容
        String content = parseDocumentContent(file);

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

        // 6. 更新文档向量
        searchService.updateDocumentVector(document.getId());

        // 7. 保存到搜索引擎

        return getDocumentById(document.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public KbDocumentVO updateDocument(Long id, String title, String content, List<Long> tagIds) {
        // 1. 查询原文档
        KbDocument document = documentMapper.selectById(id);
        if (document == null) {
            throw new BusinessException("文档不存在");
        }

        // 2. 更新文档信息
        document.setTitle(title);
        document.setContent(content);
        document.setVersion(document.getVersion() + 1);
        documentMapper.updateById(document);

        // 3. 保存文档版本
        saveDocumentVersion(document);

        // 4. 更新文档标签关联
        documentTagMapper.delete(new LambdaQueryWrapper<KbDocumentTag>()
                .eq(KbDocumentTag::getDocumentId, id));
        if (tagIds != null && !tagIds.isEmpty()) {
            saveDocumentTags(id, tagIds);
        }

        return getDocumentById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(Long id) {
        // 1. 删除文档
        documentMapper.deleteById(id);

        // 2. 删除文档标签关联
        documentTagMapper.delete(new LambdaQueryWrapper<KbDocumentTag>()
                .eq(KbDocumentTag::getDocumentId, id));
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
}