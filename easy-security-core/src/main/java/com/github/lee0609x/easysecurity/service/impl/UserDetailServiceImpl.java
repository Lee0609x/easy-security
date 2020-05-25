package com.github.lee0609x.easysecurity.service.impl;

import com.github.lee0609x.easysecurity.mapper.RoleMapper;
import com.github.lee0609x.easysecurity.mapper.UserMapper;
import com.github.lee0609x.easysecurity.model.Role;
import com.github.lee0609x.easysecurity.model.SecurityUser;
import com.github.lee0609x.easysecurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by Lee0609x
 * Date:2020/5/12
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.retrieveUserByUsername(username);
        Assert.notNull(user, "未查询到用户");
        List<Role> roleList = roleMapper.retrieveRoleByUserId(user.getId());
        Assert.notNull(roleList, "当前用户没有任何权限");
        return new SecurityUser(user, roleList);
    }
}
