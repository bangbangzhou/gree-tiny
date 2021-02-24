package com.cn.gree.tiny.modules.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.model.GreeResource;
import com.cn.gree.tiny.modules.admin.mapper.GreeResourceMapper;
import com.cn.gree.tiny.modules.admin.service.GreeAdminCacheService;
import com.cn.gree.tiny.modules.admin.service.GreeResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 后台资源表 服务实现类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
@Service
public class GreeResourceServiceImpl extends ServiceImpl<GreeResourceMapper, GreeResource> implements GreeResourceService {
    @Autowired
    private GreeAdminCacheService adminCacheService;
    @Override
    public boolean create(GreeResource umsResource) {
        umsResource.setCreateTime(new Date());
        return save(umsResource);
    }

    @Override
    public boolean update(Long id, GreeResource umsResource) {
        umsResource.setId(id);
        boolean success = updateById(umsResource);
        adminCacheService.delResourceListByResource(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        boolean success = removeById(id);
        adminCacheService.delResourceListByResource(id);
        return success;
    }

    @Override
    public Page<GreeResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        Page<GreeResource> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GreeResource> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GreeResource> lambda = wrapper.lambda();
        if(categoryId!=null){
            lambda.eq(GreeResource::getCategoryId,categoryId);
        }
        if(StrUtil.isNotEmpty(nameKeyword)){
            lambda.like(GreeResource::getName,nameKeyword);
        }
        if(StrUtil.isNotEmpty(urlKeyword)){
            lambda.like(GreeResource::getUrl,urlKeyword);
        }
        return page(page,wrapper);
    }
}
