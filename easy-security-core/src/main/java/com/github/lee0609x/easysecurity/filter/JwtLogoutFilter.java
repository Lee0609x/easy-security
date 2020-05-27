package com.github.lee0609x.easysecurity.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户注销
 * 只做了客户端注销，服务端注销暂未实现
 * Created by Lee0609x
 * Date:2020/5/27
 */
public class JwtLogoutFilter extends GenericFilterBean {

    private static final String FILTER_APPLIED = "__spring_security_demoFilter_filterApplied";

    private final RequestMatcher logoutRequestMatcher;
    private final LogoutHandler handler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    /**
     * @param logoutRequestMatcher 请求路径匹配
     * @param logoutSuccessHandler 注销handler
     * @param handlers 额外操作handler
     */
    public JwtLogoutFilter(RequestMatcher logoutRequestMatcher, LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers) {
        this.logoutRequestMatcher = logoutRequestMatcher;
        this.handler = new CompositeLogoutHandler(handlers);
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (requiresLogout(request, response)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                logger.debug("用户已被注销");
                logoutSuccessHandler.onLogoutSuccess(request, response, null);
                return;
            }
            logger.debug(String.format("用户[%s]开始注销", auth.getName()));
            this.handler.logout(request, response, auth);
            logoutSuccessHandler.onLogoutSuccess(request, response, auth);
            return;
        }
        chain.doFilter(request, response);
    }

    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
        return logoutRequestMatcher.matches(request);
    }

}
