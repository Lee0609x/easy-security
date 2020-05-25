package com.github.lee0609x.easysecurity.service.impl;

import com.github.lee0609x.easysecurity.mapper.ResourceMapper;
import com.github.lee0609x.easysecurity.mapper.RoleMapper;
import com.github.lee0609x.easysecurity.model.Resource;
import com.github.lee0609x.easysecurity.model.Role;
import com.github.lee0609x.easysecurity.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lee0609x
 * Date:2020/5/17
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Map<Role, List<Resource>> retrieveRoleResourceMap() {
        Map<Role, List<Resource>> roleResourceMap = new HashMap<>();
        roleMapper.retrieveAllRole().forEach(role -> {
            List<Resource> resourceList = resourceMapper.retrieveResourceByRoleId(role.getId());
            roleResourceMap.put(role, resourceList);
        });
        return roleResourceMap;
    }
}
