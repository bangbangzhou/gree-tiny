package com.cn.gree.tiny.modules.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.model.GreeMenu;
import com.cn.gree.tiny.modules.admin.model.GreeResource;
import com.cn.gree.tiny.modules.admin.model.GreeRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeRoleService extends IService<GreeRole> {
    /**
     * 添加角色
     */
    boolean create(GreeRole role);

    /**
     * 批量删除角色
     */
    boolean delete(List<Long> ids);

    /**
     * 分页获取角色列表
     */
    Page<GreeRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据管理员ID获取对应菜单
     */
    List<GreeMenu> getMenuList(Long adminId);

    /**
     * 获取角色相关菜单
     */
    List<GreeMenu> listMenu(Long roleId);

    /**
     * 获取角色相关资源
     */
    List<GreeResource> listResource(Long roleId);

    /**
     * 给角色分配菜单
     */
    @Transactional
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     */
    @Transactional
    int allocResource(Long roleId, List<Long> resourceIds);

}
