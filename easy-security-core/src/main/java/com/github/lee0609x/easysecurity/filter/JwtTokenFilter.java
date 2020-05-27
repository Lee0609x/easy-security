package com.github.lee0609x.easysecurity.filter;

import com.github.lee0609x.easysecurity.constants.EasyConstants;
import com.github.lee0609x.easysecurity.model.SecurityUser;
import com.github.lee0609x.easysecurity.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Token验证
 * Created by Lee0609x
 * Date:2020/5/12
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String FILTER_APPLIED = "__spring_security_demoFilter_filterApplied";//重要，该标识会使得该Filter不被自动注入到servlet filter chain中
    private final long jwtTimeOut;

    public JwtTokenFilter(long jwtTimeOut) {
        this.jwtTimeOut = jwtTimeOut;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            Arrays.asList(cookies).forEach(cookie -> {
                if (cookie.getName().equals(EasyConstants.JWT_COOKIE_NAME)) {
                    try {
                        SecurityUser securityUser = JwtUtil.getSecurityUserByToken(cookie.getValue());
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities()));

                    } catch (SignatureException e) {
                        logger.error("token解析失败, 清除当前Token");
                        clearCookies(httpServletResponse, cookie);
                    } catch (ExpiredJwtException e) {
                        logger.error("token已过期, 清除当前Token");
                        clearCookies(httpServletResponse, cookie);
                    } catch (IOException e) {
                        logger.error("未能将json内容转换为security user");
                        clearCookies(httpServletResponse, cookie);
                    }
                }
            });
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * JWT续签
     * 当令牌剩余有效期低于该值的一半时，令牌自动续签
     */
    private void renewalJwt() {

    }

    private void clearCookies(HttpServletResponse response, Cookie... cookies) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}
