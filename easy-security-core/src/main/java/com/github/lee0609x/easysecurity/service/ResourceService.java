package com.github.lee0609x.easysecurity.service;

import com.github.lee0609x.easysecurity.model.Resource;
import com.github.lee0609x.easysecurity.model.Role;

import java.util.List;
import java.util.Map;

/**
 * Created by Lee0609x
 * Date:2020/5/17
 */
public interface ResourceService {
    /**
     * 查询所有角色的资源
     * @return
     */
    Map<Role, List<Resource>> retrieveRoleResourceMap();
}
