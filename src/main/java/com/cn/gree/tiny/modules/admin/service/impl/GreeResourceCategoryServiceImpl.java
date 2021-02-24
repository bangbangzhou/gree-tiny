package com.cn.gree.tiny.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.gree.tiny.modules.admin.model.GreeResourceCategory;
import com.cn.gree.tiny.modules.admin.mapper.GreeResourceCategoryMapper;
import com.cn.gree.tiny.modules.admin.service.GreeResourceCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 资源分类表 服务实现类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
@Service
public class GreeResourceCategoryServiceImpl extends ServiceImpl<GreeResourceCategoryMapper, GreeResourceCategory> implements GreeResourceCategoryService {

    @Override
    public List<GreeResourceCategory> listAll() {
        QueryWrapper<GreeResourceCategory> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByDesc(GreeResourceCategory::getSort);
        return list(wrapper);
    }

    @Override
    public boolean create(GreeResourceCategory umsResourceCategory) {
        umsResourceCategory.setCreateTime(new Date());
        return save(umsResourceCategory);
    }
}
