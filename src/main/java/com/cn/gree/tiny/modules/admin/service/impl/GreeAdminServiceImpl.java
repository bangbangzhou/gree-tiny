package com.cn.gree.tiny.modules.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.gree.tiny.domain.AdminUserDetails;
import com.cn.gree.tiny.exception.Asserts;
import com.cn.gree.tiny.modules.admin.dto.GreeAdminParam;
import com.cn.gree.tiny.modules.admin.dto.UpdateAdminPasswordParam;
import com.cn.gree.tiny.modules.admin.mapper.GreeAdminLoginLogMapper;
import com.cn.gree.tiny.modules.admin.mapper.GreeResourceMapper;
import com.cn.gree.tiny.modules.admin.mapper.GreeRoleMapper;
import com.cn.gree.tiny.modules.admin.model.*;
import com.cn.gree.tiny.modules.admin.mapper.GreeAdminMapper;
import com.cn.gree.tiny.modules.admin.service.GreeAdminCacheService;
import com.cn.gree.tiny.modules.admin.service.GreeAdminRoleRelationService;
import com.cn.gree.tiny.modules.admin.service.GreeAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.gree.tiny.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author zbb
 * @since 2021-02-08
 */
@Service
public class GreeAdminServiceImpl extends ServiceImpl<GreeAdminMapper, GreeAdmin> implements GreeAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreeAdminServiceImpl.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GreeAdminLoginLogMapper loginLogMapper;
    @Autowired
    private GreeAdminCacheService adminCacheService;
    @Autowired
    private GreeAdminRoleRelationService adminRoleRelationService;
    @Autowired
    private GreeRoleMapper roleMapper;
    @Autowired
    private GreeResourceMapper resourceMapper;



    @Override
    public GreeAdmin getAdminByUsername(String username) {
        GreeAdmin admin = adminCacheService.getAdmin(username);
        if(admin!=null) return  admin;
        QueryWrapper<GreeAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeAdmin::getUsername,username);
        List<GreeAdmin> adminList = list(wrapper);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    @Override
    public GreeAdmin register(GreeAdminParam umsAdminParam) {
        GreeAdmin umsAdmin = new GreeAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        QueryWrapper<GreeAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeAdmin::getUsername,umsAdmin.getUsername());
        List<GreeAdmin> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        baseMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                Asserts.fail("密码不正确");
            }
            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        GreeAdmin admin = getAdminByUsername(username);
        if(admin==null) return;
        GreeAdminLoginLog loginLog = new GreeAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }



    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public Page<GreeAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        Page<GreeAdmin> page = new Page<>(pageNum,pageSize);
        QueryWrapper<GreeAdmin> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<GreeAdmin> lambda = wrapper.lambda();
        if(StrUtil.isNotEmpty(keyword)){
            lambda.like(GreeAdmin::getUsername,keyword);
            lambda.or().like(GreeAdmin::getNickName,keyword);
        }
        return page(page,wrapper);
    }

    @Override
    public boolean update(Long id, GreeAdmin admin) {
        admin.setId(id);
        GreeAdmin rawAdmin = getById(id);
        if(rawAdmin.getPassword().equals(admin.getPassword())){
            //与原加密密码相同的不需要修改
            admin.setPassword(null);
        }else{
            //与原加密密码不同的需要加密修改
            if(StrUtil.isEmpty(admin.getPassword())){
                admin.setPassword(null);
            }else{
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            }
        }
        boolean success = updateById(admin);
        adminCacheService.delAdmin(id);
        return success;
    }

    @Override
    public boolean delete(Long id) {
        adminCacheService.delAdmin(id);
        boolean success = removeById(id);
        adminCacheService.delResourceList(id);
        return success;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //先删除原来的关系
        QueryWrapper<GreeAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeAdminRoleRelation::getAdminId,adminId);
        adminRoleRelationService.remove(wrapper);
        //建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<GreeAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                GreeAdminRoleRelation roleRelation = new GreeAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationService.saveBatch(list);
        }
        adminCacheService.delResourceList(adminId);
        return count;
    }

    @Override
    public List<GreeRole> getRoleList(Long adminId) {
        return roleMapper.getRoleList(adminId);
    }

    @Override
    public List<GreeResource> getResourceList(Long adminId) {
        List<GreeResource> resourceList = adminCacheService.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            return  resourceList;
        }
        resourceList = resourceMapper.getResourceList(adminId);
        if(CollUtil.isNotEmpty(resourceList)){
            adminCacheService.setResourceList(adminId,resourceList);
        }
        return resourceList;
    }

    @Override
    public int updatePassword(UpdateAdminPasswordParam param) {
        if(StrUtil.isEmpty(param.getUsername())
                ||StrUtil.isEmpty(param.getOldPassword())
                ||StrUtil.isEmpty(param.getNewPassword())){
            return -1;
        }
        QueryWrapper<GreeAdmin> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeAdmin::getUsername,param.getUsername());
        List<GreeAdmin> adminList = list(wrapper);
        if(CollUtil.isEmpty(adminList)){
            return -2;
        }
        GreeAdmin umsAdmin = adminList.get(0);
        if(!passwordEncoder.matches(param.getOldPassword(),umsAdmin.getPassword())){
            return -3;
        }
        umsAdmin.setPassword(passwordEncoder.encode(param.getNewPassword()));
        updateById(umsAdmin);
        adminCacheService.delAdmin(umsAdmin.getId());
        return 1;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        //获取用户信息
        GreeAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<GreeResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin,resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
