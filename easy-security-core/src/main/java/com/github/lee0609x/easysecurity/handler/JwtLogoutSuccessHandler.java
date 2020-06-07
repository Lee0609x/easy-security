package com.github.lee0609x.easysecurity.handler;

import com.github.lee0609x.easysecurity.util.JsonUtil;
import com.github.lee0609x.easysecurity.util.ResponseBodyUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销后，重定向到指定位置
 * Created by Lee0609x
 * Date:2020/5/27
 */
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String responseBody = JsonUtil.Object2Json(ResponseBodyUtil.successResponse("用户已注销"));
        response.getWriter().write(responseBody);
    }
}
