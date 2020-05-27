package com.github.lee0609x.easysecurity.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证异常处理器
 * Created by Lee0609x
 * Date:2020/5/21
 */
public class NeedLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(NeedLoginAuthenticationEntryPoint.class);

    private String redirectLoginURL = "/login";

    public NeedLoginAuthenticationEntryPoint(String redirectLoginURL) {
        this.redirectLoginURL = redirectLoginURL;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.info(String.format("需要登录才可以访问, 请求重定向至: %s", redirectLoginURL));
        response.sendRedirect(redirectLoginURL);
    }
}
