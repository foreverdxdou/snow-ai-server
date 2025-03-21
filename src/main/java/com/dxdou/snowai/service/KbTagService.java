package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.KbTag;
import com.dxdou.snowai.domain.vo.KbTagVO;

import java.util.List;

/**
 * 标签服务接口
 *
 * @author foreverdxdou
 */
public interface KbTagService extends IService<KbTag> {

    /**
     * 分页查询标签列表
     *
     * @param page      分页参数
     * @param name      标签名称
     * @param kbId      知识库ID
     * @param creatorId 创建者ID
     * @param status    状态
     * @return 标签列表
     */
    Page<KbTagVO> getTagPage(Page<KbTag> page, String name, Long kbId, Long creatorId, Integer status);

    /**
     * 根据ID查询标签
     *
     * @param id 标签ID
     * @return 标签信息
     */
    KbTagVO getTagById(Long id);

    /**
     * 创建标签
     *
     * @param name        标签名称
     * @param description 标签描述
     * @param kbId        知识库ID
     * @return 标签信息
     */
    KbTagVO createTag(String name, String description, Long kbId);

    /**
     * 更新标签
     *
     * @param id          标签ID
     * @param name        标签名称
     * @param description 标签描述
     * @return 标签信息
     */
    KbTagVO updateTag(Long id, String name, String description);

    /**
     * 删除标签
     *
     * @param id 标签ID
     */
    void deleteTag(Long id);

    /**
     * 更新标签状态
     *
     * @param id     标签ID
     * @param status 状态
     */
    void updateTagStatus(Long id, Integer status);

    /**
     * 获取知识库的标签列表
     *
     * @param kbId 知识库ID
     * @return 标签列表
     */
    List<KbTagVO> getTagsByKbId(Long kbId);
}