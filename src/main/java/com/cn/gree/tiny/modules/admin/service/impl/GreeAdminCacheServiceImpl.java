package com.cn.gree.tiny.modules.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cn.gree.tiny.modules.admin.mapper.GreeAdminMapper;
import com.cn.gree.tiny.modules.admin.model.GreeAdmin;
import com.cn.gree.tiny.modules.admin.model.GreeAdminRoleRelation;
import com.cn.gree.tiny.modules.admin.model.GreeResource;
import com.cn.gree.tiny.modules.admin.service.GreeAdminCacheService;
import com.cn.gree.tiny.modules.admin.service.GreeAdminRoleRelationService;
import com.cn.gree.tiny.modules.admin.service.GreeAdminService;
import com.cn.gree.tiny.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GreeAdminCacheServiceImpl implements GreeAdminCacheService {


    @Autowired
    private GreeAdminService adminService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private GreeAdminMapper adminMapper;

    @Autowired
    private GreeAdminRoleRelationService adminRoleRelationService;

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public void delAdmin(Long adminId) {

        GreeAdmin greeAdmin = adminService.getById(adminId);
        if(adminId!=null){
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + greeAdmin.getUsername();
            redisService.del(key);
        }

    }

    @Override
    public void delResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.del(key);
    }

    @Override
    public void delResourceListByRole(Long roleId) {
        QueryWrapper<GreeAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GreeAdminRoleRelation::getRoleId,roleId);
        List<GreeAdminRoleRelation> relationList = adminRoleRelationService.list(wrapper);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }

    }

    @Override
    public void delResourceListByRoleIds(List<Long> roleIds) {
        QueryWrapper<GreeAdminRoleRelation> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(GreeAdminRoleRelation::getRoleId,roleIds);
        List<GreeAdminRoleRelation> relationList = adminRoleRelationService.list(wrapper);
        if (CollUtil.isNotEmpty(relationList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = relationList.stream().map(relation -> keyPrefix + relation.getAdminId()).collect(Collectors.toList());
            redisService.del(keys);
        }
    }

    @Override
    public void delResourceListByResource(Long resourceId) {
        List<Long> adminIdList = adminMapper.getAdminIdList(resourceId);
        if (CollUtil.isNotEmpty(adminIdList)) {
            String keyPrefix = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":";
            List<String> keys = adminIdList.stream().map(adminId -> keyPrefix + adminId).collect(Collectors.toList());
            redisService.del(keys);
        }

    }

    @Override
    public GreeAdmin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (GreeAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(GreeAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisService.set(key, admin, REDIS_EXPIRE);

    }

    @Override
    public List<GreeResource> getResourceList(Long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        return (List<GreeResource>) redisService.get(key);
    }

    @Override
    public void setResourceList(Long adminId, List<GreeResource> resourceList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }
}
