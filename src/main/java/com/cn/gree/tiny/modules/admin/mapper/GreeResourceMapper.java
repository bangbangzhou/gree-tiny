package com.cn.gree.tiny.modules.admin.mapper;

import com.cn.gree.tiny.modules.admin.model.GreeResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台资源表 Mapper 接口
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeResourceMapper extends BaseMapper<GreeResource> {

    /**
     * 获取用户所有可访问资源
     */
    List<GreeResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 根据角色ID获取资源
     */
    List<GreeResource> getResourceListByRoleId(@Param("roleId") Long roleId);

}
