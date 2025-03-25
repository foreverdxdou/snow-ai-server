package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dxdou.snowai.domain.entity.KbCategory;
import com.dxdou.snowai.domain.vo.KbCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库分类Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbCategoryMapper extends BaseMapper<KbCategory> {

    /**
     * 分页查询分类列表
     *
     * @param page   分页参数
     * @param name   分类名称
     * @param status 状态
     * @return 分类列表
     */
    Page<KbCategoryVO> selectCategoryList(Page<KbCategory> page,
            @Param("name") String name, @Param("status") Integer status);

    /**
     * 查询分类树
     *
     * @return 分类树
     */
    List<KbCategoryVO> selectCategoryTree();

    /**
     * 根据ID查询分类信息
     *
     * @param id 分类ID
     * @return 分类信息
     */
    KbCategoryVO selectCategoryById(@Param("id") Long id);

    /**
     * 查询子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<KbCategoryVO> selectChildren(@Param("parentId") Long parentId);

    /**
     * 查询分类及其所有子分类ID
     *
     * @param categoryId 分类ID
     * @return 分类ID列表
     */
    List<Long> selectCategoryAndChildren(@Param("categoryId") Long categoryId);

    /**
     * 查询知识库的所有分类
     *
     * @param kbId 知识库ID
     * @return 分类列表
     */
    List<KbCategoryVO> selectCategoryListByKbId(@Param("kbId") Long kbId);
}