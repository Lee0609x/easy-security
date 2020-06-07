package com.github.lee0609x.easysecurity.autoconfigure;

import com.github.lee0609x.easysecurity.autoconfigure.properties.EasySecurityConfigProperties;
import com.github.lee0609x.easysecurity.filter.*;
import com.github.lee0609x.easysecurity.handler.Http403AccessDeniedHandler;
import com.github.lee0609x.easysecurity.handler.JwtLogoutHandler;
import com.github.lee0609x.easysecurity.handler.JwtLogoutSuccessHandler;
import com.github.lee0609x.easysecurity.handler.NeedLoginAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SpringSecurity配置类
 *
 * Filter链路与配置类对照表：
 * ------------------------------------------------------------------
 * Configurer	                Filter
 * ------------------------------------------------------------------
 * CsrfConfigurer	            CsrfFilter
 * 空	                        WebAsyncManagerIntegrationFilter
 * ExceptionHandlingConfigurer	ExceptionTranslationFilter
 * HeadersConfigurer	        HeaderWriterFilter
 * SessionManagementConfigurer	SessionManagementFilter
 * SecurityContextConfigurer	SecurityContextPersistenceFilter
 * RequestCacheConfigurer	    RequestCacheAwareFilter
 * AnonymousConfigurer	        AnonymousAuthenticationFilter
 * ServletApiConfigurer	        SecurityContextHolderAwareRequestFilter
 * DefaultLoginPageConfigurer	DefaultLoginPageGeneratingFilter
 * LogoutConfigurer	            LogoutFilter
 * FormLoginConfigurer	        UsernamePasswordAuthenticationFilter
 * HttpBasicConfigurer	        BasicAuthenticationFilter
 * -------------------------------------------------------------------
 * Created by Lee0609x
 * Date:2020/5/12
 */
@EnableWebSecurity
@EnableConfigurationProperties(EasySecurityConfigProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String PASSWORD_SECRET = "FUCK";//加密盐值

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EasySecurityConfigProperties configProperties;

    /**
     * 因为修改了默认的FilterSecurityInterceptor的SecurityMetadataSource和AccessDecisionManager
     * 原有的matcher已经全部失效，所有经过登录与非登录请求，都要与数据库中的资源进行比较
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//关闭csrf
                .formLogin().disable()//关闭默认的登录功能
                .logout().disable()//关闭默认的注销功能
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭session
                .and()
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {//自定义鉴权
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O filterSecurityInterceptor) {
                        filterSecurityInterceptor.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource());
                        filterSecurityInterceptor.setAccessDecisionManager(customAccessDecisionManager());
                        return filterSecurityInterceptor;
                    }
                })
                .anyRequest()
                .authenticated();
        //异常处理
        http.exceptionHandling()
                .authenticationEntryPoint(needLoginAuthenticationEntryPoint())
                .accessDeniedHandler(http403AccessDeniedHandler());
        //JWT登录
        http.addFilterAfter(jwtLoginFilter(), HeaderWriterFilter.class);
        //JWT认证
        http.addFilterAfter(jwtTokenFilter(), JwtLoginFilter.class);
        //JWT注销
        http.addFilterAfter(jwtLogoutFilter(), JwtTokenFilter.class);
        //JWT续签
        http.addFilterAfter(jwtRenewalFilter(), FilterSecurityInterceptor.class);
    }

    /**
     * 基于user-details的用户签名机制的配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new Pbkdf2PasswordEncoder(PASSWORD_SECRET));
    }

    @Bean
    public CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource() {
        return new CustomFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public CustomAccessDecisionManager customAccessDecisionManager() {
        ArrayList<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(customVoter());
        return new CustomAccessDecisionManager(accessDecisionVoters);
    }

    @Bean
    public CustomVoter customVoter() {
        return new CustomVoter();
    }

    @Bean
    public NeedLoginAuthenticationEntryPoint needLoginAuthenticationEntryPoint() {
        return new NeedLoginAuthenticationEntryPoint(configProperties.getRequest().getNeedLogin());
    }

    @Bean
    public Http403AccessDeniedHandler http403AccessDeniedHandler() {
        return new Http403AccessDeniedHandler(configProperties.getRequest().getHttp403());
    }

    public JwtLoginFilter jwtLoginFilter() throws Exception {
        return new JwtLoginFilter(new AntPathRequestMatcher(configProperties.getRequest().getLogin(), "POST"),
                authenticationManager(),
                configProperties.getJwt().getHeader(),
                configProperties.getJwt().getTimeOut());
    }

    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(configProperties.getJwt().getHeader());
    }

    public JwtLogoutFilter jwtLogoutFilter() throws IOException, ServletException {
        return new JwtLogoutFilter(new AntPathRequestMatcher(configProperties.getRequest().getLogout(), "GET"),
                new JwtLogoutSuccessHandler(),
                new JwtLogoutHandler());
    }

    public JwtRenewalFilter jwtRenewalFilter() {
        return new JwtRenewalFilter(new AntPathRequestMatcher(configProperties.getRequest().getRenewal(), "GET"),
                configProperties.getJwt().getHeader(),
                configProperties.getJwt().getTimeOut());
    }

}
