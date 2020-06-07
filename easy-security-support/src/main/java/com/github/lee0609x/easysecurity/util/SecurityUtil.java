package com.github.lee0609x.easysecurity.util;

import com.github.lee0609x.easysecurity.model.SecurityUser;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Lee0609x
 * Date:2020/5/25
 */
public class SecurityUtil {

    private final static AuthenticationTrustResolverImpl resolver = new AuthenticationTrustResolverImpl();

    /**
     * 获取当前用户
     */
    public static Authentication getSecurityUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    public static void setSecurityUser(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 判断当前用户是否为匿名用户
     * @return
     */
    public static boolean getUserLoginStatus() {
        return !resolver.isAnonymous(getSecurityUser());
    }
}
