package com.cn.gree.tiny.modules.admin.mapper;

import com.cn.gree.tiny.modules.admin.model.GreeRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeRoleMapper extends BaseMapper<GreeRole> {
    /**
     * 获取用户所有角色
     */
    List<GreeRole> getRoleList(@Param("adminId") Long adminId);


}
