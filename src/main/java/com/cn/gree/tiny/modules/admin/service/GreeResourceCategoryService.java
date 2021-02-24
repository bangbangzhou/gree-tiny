package com.cn.gree.tiny.modules.admin.service;

import com.cn.gree.tiny.modules.admin.model.GreeResourceCategory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 资源分类表 服务类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeResourceCategoryService extends IService<GreeResourceCategory> {
    /**
     * 获取所有资源分类
     */
    List<GreeResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    boolean create(GreeResourceCategory umsResourceCategory);

}
