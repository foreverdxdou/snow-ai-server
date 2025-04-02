package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.KbDocumentVector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 文档向量Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbDocumentVectorMapper extends BaseMapper<KbDocumentVector> {

    /**
     * 查找相似向量
     *
     * @param queryVector 查询向量
     * @param kbIds       知识库ID列表
     * @param docIds
     * @param limit       限制数量
     * @return 相似向量列表
     */
    List<KbDocumentVector> findSimilarVectors(@Param("queryVector") float[] queryVector,
                                              @Param("kbIds") Long[] kbIds,@Param("docIds") Set<Long> docIds, @Param("limit") long limit);

    /**
     * 根据文档ID查询向量
     *
     * @param documentId 文档ID
     * @return 文档向量
     */
    KbDocumentVector selectByDocumentId(@Param("documentId") Long documentId);

    /**
     * 插入或更新向量
     *
     * @param documentVector 文档向量
     */
    void insertOrUpdate(KbDocumentVector documentVector);

    /**
     * 根据文档ID删除向量
     *
     * @param documentId 文档ID
     */
    void deleteByDocumentId(@Param("documentId") Long documentId);
}