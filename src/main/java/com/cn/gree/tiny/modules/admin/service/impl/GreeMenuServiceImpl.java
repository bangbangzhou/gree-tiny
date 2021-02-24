package com.cn.gree.tiny.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.dto.GreeMenuNode;
import com.cn.gree.tiny.modules.admin.model.GreeMenu;
import com.cn.gree.tiny.modules.admin.mapper.GreeMenuMapper;
import com.cn.gree.tiny.modules.admin.service.GreeMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
@Service
public class GreeMenuServiceImpl extends ServiceImpl<GreeMenuMapper, GreeMenu> implements GreeMenuService {

    @Override
    public boolean create(GreeMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
        updateLevel(umsMenu);
        return save(umsMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(GreeMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            GreeMenu parentMenu = getById(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public boolean update(Long id, GreeMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        return updateById(umsMenu);
    }

    @Override
    public Page<GreeMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        Page<GreeMenu> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GreeMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeMenu::getParentId,parentId)
                .orderByDesc(GreeMenu::getSort);
        return page(page,wrapper);
    }

    @Override
    public List<GreeMenuNode> treeList() {
        List<GreeMenu> menuList = list();
        List<GreeMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        GreeMenu umsMenu = new GreeMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return updateById(umsMenu);
    }
    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private GreeMenuNode covertMenuNode(GreeMenu menu, List<GreeMenu> menuList) {
        GreeMenuNode node = new GreeMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<GreeMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
