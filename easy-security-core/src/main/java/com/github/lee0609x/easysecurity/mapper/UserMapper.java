package com.github.lee0609x.easysecurity.mapper;

import com.github.lee0609x.easysecurity.model.User;

/**
 * Created by Lee0609x
 * Date:2020/5/12
 */
public interface UserMapper {
    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User retrieveUserByUsername(String username);
}
