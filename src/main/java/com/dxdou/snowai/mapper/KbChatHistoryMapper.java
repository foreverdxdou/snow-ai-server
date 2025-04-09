package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库对话历史Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbChatHistoryMapper extends BaseMapper<KbChatHistory> {

    /**
     * 分页查询聊天历史列表
     *
     * @param page            分页参数
     * @param knowledgeBaseId 知识库ID
     * @param userId          用户ID
     * @return 聊天历史列表
     */
    Page<KbChatHistory> selectChatHistoryList(Page<KbChatHistory> page,
            @Param("knowledgeBaseId") Long knowledgeBaseId,
            @Param("userId") Long userId);

    /**
     * 根据ID查询聊天历史信息
     *
     * @param requestId 请求id
     * @return 聊天历史信息
     */
    KbChatHistory selectChatHistoryByRequestId(@Param("requestId") String requestId);

    /**
     * 查询用户的聊天历史列表
     *
     * @param userId 用户ID
     * @return 聊天历史列表
     */
    List<KbChatHistory> selectUserChatHistory(@Param("userId") Long userId);

    /**
     * 删除知识库的所有聊天历史
     *
     * @param knowledgeBaseId 知识库ID
     */
    void deleteByKnowledgeBaseId(@Param("knowledgeBaseId") Long knowledgeBaseId);

    /**
     * 清空会话聊天历史
     * @param requestId
     */
    void deleteByRequestId(@Param("requestId") String requestId);
}