package com.github.lee0609x.easysecurity.filter;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

/**
 * URL鉴权
 * (已废弃,另一种实现方案,该过滤器添加至FilterSecurityInterceptor之后，实现同样的拦截效果，并使得security默认的matcher依然生效)
 * Created by Lee0609x
 * Date:2020/5/16
 */
@Deprecated
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    private SecurityMetadataSource securityMetadataSource;

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";

    public CustomFilterSecurityInterceptor(AccessDecisionManager accessDecisionManager, SecurityMetadataSource securityMetadataSource) {
        super.setAccessDecisionManager(accessDecisionManager);
        this.securityMetadataSource = securityMetadataSource;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ("anonymousUser".equals(authentication.getPrincipal())) {//当前为匿名用户状态，FilterSecurityInterceptor应该已经处理完毕，所以这里不做任何处理
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {//当前用户已经登录，对角色权限进行判断
            InterceptorStatusToken token = super.beforeInvocation(fi);
            try {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            }
            finally {
                super.finallyInvocation(token);
            }
            super.afterInvocation(token, null);
        }
    }
}
