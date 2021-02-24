package com.cn.gree.tiny.modules.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.model.GreeResource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 后台资源表 服务类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeResourceService extends IService<GreeResource> {
    /**
     * 添加资源
     */
    boolean create(GreeResource umsResource);

    /**
     * 修改资源
     */
    boolean update(Long id, GreeResource umsResource);

    /**
     * 删除资源
     */
    boolean delete(Long id);

    /**
     * 分页查询资源
     */
    Page<GreeResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);
}
