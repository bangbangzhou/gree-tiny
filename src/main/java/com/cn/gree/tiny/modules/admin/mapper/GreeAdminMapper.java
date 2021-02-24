package com.cn.gree.tiny.modules.admin.mapper;

import com.cn.gree.tiny.modules.admin.model.GreeAdmin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeAdminMapper extends BaseMapper<GreeAdmin> {
    /**
     * 获取资源相关用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);

}
