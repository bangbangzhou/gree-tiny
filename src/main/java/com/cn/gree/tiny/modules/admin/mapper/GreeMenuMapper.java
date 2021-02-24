package com.cn.gree.tiny.modules.admin.mapper;

import com.cn.gree.tiny.modules.admin.model.GreeMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeMenuMapper extends BaseMapper<GreeMenu> {

    /**
     * 根据后台用户ID获取菜单
     */
    List<GreeMenu> getMenuList(@Param("adminId") Long adminId);
    /**
     * 根据角色ID获取菜单
     */
    List<GreeMenu> getMenuListByRoleId(@Param("roleId") Long roleId);
}
