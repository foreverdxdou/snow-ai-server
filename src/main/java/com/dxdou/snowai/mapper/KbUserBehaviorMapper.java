package com.dxdou.snowai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxdou.snowai.domain.entity.KbUserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户行为Mapper接口
 *
 * @author foreverdxdou
 */
@Mapper
public interface KbUserBehaviorMapper extends BaseMapper<KbUserBehavior> {

    /**
     * 获取用户最近的行为记录
     *
     * @param userId 用户ID
     * @param kbId   知识库ID
     * @param limit  限制数量
     * @return 行为记录列表
     */
    List<KbUserBehavior> selectRecentBehaviors(@Param("userId") Long userId,
            @Param("kbId") Long kbId, @Param("limit") int limit);

    /**
     * 获取用户行为统计
     *
     * @param userId 用户ID
     * @param kbId   知识库ID
     * @return 行为统计信息
     */
    Map<String, Object> selectBehaviorStats(@Param("userId") Long userId, @Param("kbId") Long kbId);
}