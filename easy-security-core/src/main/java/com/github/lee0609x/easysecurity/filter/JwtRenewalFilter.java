package com.github.lee0609x.easysecurity.filter;

import com.github.lee0609x.easysecurity.constants.EasyConstants;
import com.github.lee0609x.easysecurity.model.EasySecurityToken;
import com.github.lee0609x.easysecurity.model.ResponseBody;
import com.github.lee0609x.easysecurity.model.ResponseBodyStatus;
import com.github.lee0609x.easysecurity.model.SecurityUser;
import com.github.lee0609x.easysecurity.util.JsonUtil;
import com.github.lee0609x.easysecurity.util.JwtUtil;
import com.github.lee0609x.easysecurity.util.ResponseBodyUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT续签
 * Created by Lee0609x
 * Date:2020/6/7
 */
public class JwtRenewalFilter extends GenericFilterBean {

    private static final String FILTER_APPLIED = "__spring_security_jwtRenewalFilter_filterApplied";

    private final RequestMatcher logoutRequestMatcher;
    private final long timeout;
    private final String header;

    public JwtRenewalFilter(RequestMatcher logoutRequestMatcher, String header, long timeout) {
        this.logoutRequestMatcher = logoutRequestMatcher;
        this.header = header;
        this.timeout = timeout;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (requiresRenewal(httpServletRequest)) {
            renewal(httpServletRequest.getHeader(header), httpServletResponse);
            return;
        }
        chain.doFilter(request, response);
    }

    protected boolean requiresRenewal(HttpServletRequest request) {
        return logoutRequestMatcher.matches(request);
    }

    /**
     * 传入一个旧的JWT，给与一个新的
     * @param jwt
     * @param httpServletResponse
     */
    protected void renewal(String jwt, HttpServletResponse httpServletResponse) {
        try {
            //TODO 其实可以直接从Security上下文中获取securityUser
            SecurityUser securityUser = JwtUtil.getSecurityUserByToken(jwt);
            String newJwt = JwtUtil.getTokenBySecurityUser(securityUser, timeout);
            EasySecurityToken easySecurityToken = new EasySecurityToken(securityUser.getUser().getId(), header, newJwt, timeout);
            ResponseBody<EasySecurityToken> responseBody = ResponseBodyUtil.successResponse(easySecurityToken);
            ResponseBodyUtil.responseWrite(httpServletResponse, responseBody);
            renewalAfter(jwt);
        } catch (Exception e) {
            logger.error("token续签失败");
            ResponseBody<String> responseBody = ResponseBodyUtil.errorResponse(ResponseBodyStatus.FAILURE, "token error");
            try {
                ResponseBodyUtil.responseWrite(httpServletResponse, responseBody);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    protected void renewalAfter(String oldJwt) {
        //TODO 旧JWT加入黑名单直至超时
    }

}
