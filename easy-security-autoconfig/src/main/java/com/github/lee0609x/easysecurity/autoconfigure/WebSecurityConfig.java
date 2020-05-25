package com.github.lee0609x.easysecurity.autoconfigure;

import com.github.lee0609x.easysecurity.autoconfigure.properties.EasySecurityConfigProperties;
import com.github.lee0609x.easysecurity.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
     * 主要是修改了默认的FilterSecurityInterceptor的SecurityMetadataSource和AccessDecisionManager
     * 原有的matcher已经全部失效，所有经过的请求，都要与数据库中的资源进行比较
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//关闭csrf
                .formLogin().disable()//关闭默认的登录功能
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
        //JWT登录与认证
        http.addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtTokenFilter(), JwtLoginFilter.class);
    }

    /**
     * 基于user-details的用户签名机制的配置
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder(PASSWORD_SECRET);
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
        return new NeedLoginAuthenticationEntryPoint(configProperties.getPage().getLogin());
    }

    @Bean
    public Http403AccessDeniedHandler http403AccessDeniedHandler() {
        return new Http403AccessDeniedHandler(configProperties.getPage().getHttp403());
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {
        return new JwtLoginFilter(authenticationManager(), configProperties.getPage().getLoginSuccess(), configProperties.getPage().getLoginFailure());
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

}
