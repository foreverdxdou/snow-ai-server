package com.dxdou.snowai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dxdou.snowai.domain.entity.KbCategory;
import com.dxdou.snowai.domain.vo.KbCategoryVO;

import java.util.List;

/**
 * 知识库分类服务接口
 *
 * @author foreverdxdou
 */
public interface KbCategoryService extends IService<KbCategory> {

    /**
     * 分页查询分类列表
     *
     * @param page   分页参数
     * @param name   分类名称
     * @param status 状态
     * @return 分类列表
     */
    Page<KbCategoryVO> getCategoryPage(Page<KbCategory> page, String name, Integer status);

    /**
     * 根据ID查询分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
    KbCategoryVO getCategoryById(Long id);

    /**
     * 创建分类
     *
     * @param kbId        知识库ID
     * @param name        分类名称
     * @param description 分类描述
     * @param parentId    父分类ID
     * @param sort        排序号
     * @param creatorId   创建者ID
     * @return 分类信息
     */
    KbCategoryVO createCategory(Long kbId, String name, String description, Long parentId, Integer sort,
            Long creatorId);

    /**
     * 更新分类
     *
     * @param id          分类ID
     * @param name        分类名称
     * @param description 分类描述
     * @param parentId    父分类ID
     * @param sort        排序号
     * @return 分类信息
     */
    KbCategoryVO updateCategory(Long id, String name, String description, Long parentId, Integer sort);

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 更新分类状态
     *
     * @param id     分类ID
     * @param status 状态
     */
    void updateCategoryStatus(Long id, Integer status);

    /**
     * 获取知识库的分类树
     *
     * @return 分类树
     */
    List<KbCategoryVO> getCategoryTree();

    /**
     * 移动分类
     *
     * @param id       分类ID
     * @param parentId 目标父分类ID
     * @param sort     目标排序号
     */
    void moveCategory(Long id, Long parentId, Integer sort);
}