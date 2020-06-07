package com.github.lee0609x.easysecurity.handler;

import com.github.lee0609x.easysecurity.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 暂时不做服务端注销
 * Created by Lee0609x
 * Date:2020/5/27
 */
public class JwtLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //清空security上下文
        SecurityUtil.setSecurityUser(null);
        //TODO 将当前用户token放入黑名单直到token过期
    }
}
