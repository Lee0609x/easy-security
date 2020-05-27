package com.github.lee0609x.easysecurity.filter;


import com.github.lee0609x.easysecurity.constants.EasyConstants;
import com.github.lee0609x.easysecurity.model.SecurityUser;
import com.github.lee0609x.easysecurity.model.User;
import com.github.lee0609x.easysecurity.util.JsonUtil;
import com.github.lee0609x.easysecurity.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录,赋予客户端Token
 * Created by Lee0609x
 * Date:2020/5/18
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String FILTER_APPLIED = "__spring_security_demoFilter_filterApplied";

    private final String defaultSussesURL;
    private final String defaultUnSussesURL;
    private final long jwtTimeOut;

    /**
     * @param loginRequestMatcher 请求路径匹配
     * @param authenticationManager 用户认证管理器
     * @param defaultSussesURL 登录成功跳转
     * @param defaultUnSussesURL 登录失败跳转
     * @param timeOut 登录有效期
     */
    public JwtLoginFilter(RequestMatcher loginRequestMatcher, AuthenticationManager authenticationManager, String defaultSussesURL, String defaultUnSussesURL, long timeOut) {
        super(loginRequestMatcher);
        this.defaultSussesURL = defaultSussesURL;
        this.defaultUnSussesURL = defaultUnSussesURL;
        this.jwtTimeOut = timeOut;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        User user = JsonUtil.Json2Object(request.getInputStream(), User.class);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 认证成功时, 生成token并返回
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
        securityUser.getUser().setPassword("");//将密码置空
        String jwt = JwtUtil.getTokenBySecurityUser(securityUser, jwtTimeOut);
        Cookie cookie = new Cookie(EasyConstants.JWT_COOKIE_NAME, jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect(defaultSussesURL);
    }

    /**
     * 认证失败, 重定向到指定URL
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        logger.info(String.format("用户登录失败, 重定向至:%s", defaultUnSussesURL));
        response.sendRedirect(defaultUnSussesURL);
    }

}
