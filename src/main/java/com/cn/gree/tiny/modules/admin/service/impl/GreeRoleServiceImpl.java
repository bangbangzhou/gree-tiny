package com.cn.gree.tiny.modules.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.mapper.GreeMenuMapper;
import com.cn.gree.tiny.modules.admin.mapper.GreeResourceMapper;
import com.cn.gree.tiny.modules.admin.model.*;
import com.cn.gree.tiny.modules.admin.mapper.GreeRoleMapper;
import com.cn.gree.tiny.modules.admin.service.GreeAdminCacheService;
import com.cn.gree.tiny.modules.admin.service.GreeRoleMenuRelationService;
import com.cn.gree.tiny.modules.admin.service.GreeRoleResourceRelationService;
import com.cn.gree.tiny.modules.admin.service.GreeRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
@Service
public class GreeRoleServiceImpl extends ServiceImpl<GreeRoleMapper, GreeRole> implements GreeRoleService {


    @Autowired
    private GreeAdminCacheService adminCacheService;
    @Autowired
    private GreeRoleMenuRelationService roleMenuRelationService;
    @Autowired
    private GreeRoleResourceRelationService roleResourceRelationService;
    @Autowired
    private GreeMenuMapper menuMapper;
    @Autowired
    private GreeResourceMapper resourceMapper;

    @Override
    public boolean create(GreeRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        return save(role);
    }

    @Override
    public boolean delete(List<Long> ids) {
        boolean success = removeByIds(ids);
        adminCacheService.delResourceListByRoleIds(ids);
        return success;
    }

    @Override
    public Page<GreeRole> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<GreeRole> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GreeRole> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GreeRole> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(keyword)){
            lambda.like(GreeRole::getName,keyword);
        }
        return page(page,wrapper);
    }

    @Override
    public List<GreeMenu> getMenuList(Long adminId) {
        return menuMapper.getMenuList(adminId);
    }

    public List<GreeMenu> listMenu(Long roleId) {
        return menuMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<GreeResource> listResource(Long roleId) {
        return resourceMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        QueryWrapper<GreeRoleMenuRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeRoleMenuRelation::getRoleId,roleId);
        roleMenuRelationService.remove(wrapper);
        //批量插入新关系
        List<GreeRoleMenuRelation> relationList = new ArrayList<>();
        for (Long menuId : menuIds) {
            GreeRoleMenuRelation relation = new GreeRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            relationList.add(relation);
        }
        roleMenuRelationService.saveBatch(relationList);
        return menuIds.size();
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        //先删除原有关系
        QueryWrapper<GreeRoleResourceRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeRoleResourceRelation::getRoleId,roleId);
        roleResourceRelationService.remove(wrapper);
        //批量插入新关系
        List<GreeRoleResourceRelation> relationList = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            GreeRoleResourceRelation relation = new GreeRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            relationList.add(relation);
        }
        roleResourceRelationService.saveBatch(relationList);
        adminCacheService.delResourceListByRole(roleId);
        return resourceIds.size();
    }
}
