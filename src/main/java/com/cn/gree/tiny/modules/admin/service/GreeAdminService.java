package com.cn.gree.tiny.modules.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.modules.admin.dto.GreeAdminParam;
import com.cn.gree.tiny.modules.admin.dto.UpdateAdminPasswordParam;
import com.cn.gree.tiny.modules.admin.model.GreeAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.gree.tiny.modules.admin.model.GreeResource;
import com.cn.gree.tiny.modules.admin.model.GreeRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
public interface GreeAdminService extends IService<GreeAdmin> {
    /**
     * 根据用户名获取后台管理员
     */
    GreeAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    GreeAdmin register(GreeAdminParam umsAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username,String password);

    /**
     * 刷新token的功能
     * @param oldToken 旧的token
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户名或昵称分页查询用户
     */
    Page<GreeAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    boolean update(Long id, GreeAdmin admin);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    /**
     * 修改用户角色关系
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对于角色
     */
    List<GreeRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     */
    List<GreeResource> getResourceList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam param);

    /**
     * 获取用户信息
     */
    UserDetails loadUserByUsername(String username);

}
