package com.github.lee0609x.easysecurity.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 鉴权异常处理器
 * Created by Lee0609x
 * Date:2020/5/21
 */
public class Http403AccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LoggerFactory.getLogger(Http403AccessDeniedHandler.class);

    private final String redirect403URL;

    public Http403AccessDeniedHandler(String redirect403URL) {
        this.redirect403URL = redirect403URL;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.info(String.format("权限不足, 请求重定向至: %s", redirect403URL));
        response.sendRedirect(redirect403URL);
    }
}
