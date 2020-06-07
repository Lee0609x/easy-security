package com.github.lee0609x.easysecurity.filter;

import com.github.lee0609x.easysecurity.constants.EasyConstants;
import com.github.lee0609x.easysecurity.model.ResponseBody;
import com.github.lee0609x.easysecurity.model.ResponseBodyStatus;
import com.github.lee0609x.easysecurity.model.SecurityUser;
import com.github.lee0609x.easysecurity.util.JsonUtil;
import com.github.lee0609x.easysecurity.util.JwtUtil;
import com.github.lee0609x.easysecurity.util.ResponseBodyUtil;
import com.github.lee0609x.easysecurity.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token验证
 * Created by Lee0609x
 * Date:2020/5/12
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String FILTER_APPLIED = "__spring_security_jwtTokenFilter_filterApplied";//该标识会使得该Filter不被自动注入到servlet filter chain中
    private final String header;

    public JwtTokenFilter(String header) {
        this.header = header;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader(header);
        if (token != null) {
            try {
                SecurityUser securityUser = parseToken(token, httpServletResponse);
                SecurityUtil.setSecurityUser(new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities()));
            } catch (SignatureException e) {
                tokenParseFailure(httpServletResponse, "token解析失败, 清除当前Token");
                return;
            } catch (ExpiredJwtException e) {
                tokenParseFailure(httpServletResponse, "token已过期, 清除当前Token");
                return;
            } catch (IOException e) {
                tokenParseFailure(httpServletResponse, "json转换失败");
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private SecurityUser parseToken(String token, HttpServletResponse response) throws IOException {
        Claims claims = JwtUtil.parseToken(token);
        String securityUserJson = claims.get(EasyConstants.JWT_SECURITY_USER_KEY).toString();
        return JsonUtil.Json2Object(securityUserJson, SecurityUser.class);
    }

    private void tokenParseFailure(HttpServletResponse httpServletResponse, String errorMessage) throws IOException {
        logger.debug(errorMessage);
        ResponseBody<String> responseBody = ResponseBodyUtil.errorResponse(ResponseBodyStatus.FAILURE, errorMessage);
        ResponseBodyUtil.responseWrite(httpServletResponse, responseBody);
    }
}
