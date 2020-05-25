package com.github.lee0609x.easysecurity.mapper;

import com.github.lee0609x.easysecurity.model.Resource;

import java.util.List;

/**
 * Created by Lee0609x
 * Date:2020/5/17
 */
public interface ResourceMapper {
    /**
     * 通过角色ID查找资源
     * @param roleId
     * @return
     */
    List<Resource> retrieveResourceByRoleId(String roleId);
}
