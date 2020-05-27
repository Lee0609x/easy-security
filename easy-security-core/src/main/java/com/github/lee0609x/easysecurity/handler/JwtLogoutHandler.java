package com.github.lee0609x.easysecurity.handler;

import com.github.lee0609x.easysecurity.constants.EasyConstants;
import com.github.lee0609x.easysecurity.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Lee0609x
 * Date:2020/5/27
 */
public class JwtLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //清除cookie
        Cookie cookie = new Cookie(EasyConstants.JWT_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //清除上下文
        SecurityUtil.setSecurityUser(null);
    }
}
