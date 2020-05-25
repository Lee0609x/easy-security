package com.github.lee0609x.easysecurity.mapper;

import com.github.lee0609x.easysecurity.model.Role;

import java.util.List;

/**
 * Created by Lee0609x
 * Date:2020/5/17
 */
public interface RoleMapper {
    /**
     * 查询所有的角色
     * @return
     */
    List<Role> retrieveAllRole();
    /**
     * 通过用户ID查找角色
     * @param id
     * @return
     */
    List<Role> retrieveRoleByUserId(String id);
}
