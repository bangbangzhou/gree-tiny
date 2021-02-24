package com.cn.gree.tiny.modules.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.dto.GreeMenuNode;
import com.cn.gree.tiny.modules.admin.model.GreeMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台菜单表 服务类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeMenuService extends IService<GreeMenu> {
    /**
     * 创建后台菜单
     */
    boolean create(GreeMenu umsMenu);

    /**
     * 修改后台菜单
     */
    boolean update(Long id, GreeMenu umsMenu);

    /**
     * 分页查询后台菜单
     */
    Page<GreeMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     */
    List<GreeMenuNode> treeList();

    /**
     * 修改菜单显示状态
     */
    boolean updateHidden(Long id, Integer hidden);

}
