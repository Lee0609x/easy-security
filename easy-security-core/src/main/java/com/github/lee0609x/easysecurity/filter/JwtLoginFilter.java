package com.github.lee0609x.easysecurity.filter;


import com.github.lee0609x.easysecurity.cache.TokenCache;
import com.github.lee0609x.easysecurity.constants.EasyConstants;
import com.github.lee0609x.easysecurity.model.*;
import com.github.lee0609x.easysecurity.model.token.EasySecurityAccessToken;
import com.github.lee0609x.easysecurity.model.token.EasySecurityRefreshToken;
import com.github.lee0609x.easysecurity.model.token.EasySecurityUserToken;
import com.github.lee0609x.easysecurity.util.JsonUtil;
import com.github.lee0609x.easysecurity.util.JwtUtil;
import com.github.lee0609x.easysecurity.util.ResponseBodyUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 用户登录,赋予客户端Token
 * Created by Lee0609x
 * Date:2020/5/18
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final long timeOut;

    private final String header;

    private final TokenCache tokenCache;

    /**
     * @param loginRequestMatcher 请求路径匹配
     * @param authenticationManager 用户认证管理器
     * @param header jwt请求头名称
     * @param timeOut 登录有效期
     * @param tokenCache token缓存服务
     */
    public JwtLoginFilter(RequestMatcher loginRequestMatcher, AuthenticationManager authenticationManager,
                          String header, long timeOut, TokenCache tokenCache) {
        super(loginRequestMatcher);
        this.header = header;
        this.timeOut = timeOut;
        this.tokenCache = tokenCache;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        User user = JsonUtil.Json2Object(request.getInputStream(), User.class);
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 认证成功时, 生成WebToken, RefreshToken，UserToken，放入缓存中
     * 并返回WebToken与RefreshToken
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
        securityUser.getUser().setPassword(null);//将密码置空
        EasySecurityAccessToken accessToken = new EasySecurityAccessToken();
        EasySecurityUserToken userToken = new EasySecurityUserToken();
        EasySecurityRefreshToken refreshToken = new EasySecurityRefreshToken();
        String id = securityUser.getUser().getId();
        //TODO

        String jwt = JwtUtil.getTokenBySecurityUser(securityUser, timeOut);
        EasySecurityToken easySecurityToken = new EasySecurityToken(securityUser.getUser().getId(), header, jwt, timeOut);
        ResponseBody<EasySecurityToken> responseBody = ResponseBodyUtil.successResponse(easySecurityToken);
        ResponseBodyUtil.responseWrite(response, responseBody);
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
        ResponseBody<String> responseBody = ResponseBodyUtil.errorResponse(ResponseBodyStatus.LOGIN_FAILURE);
        response.getWriter().write(JsonUtil.Object2Json(responseBody));
    }

}
