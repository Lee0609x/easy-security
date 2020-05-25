package com.github.lee0609x.easysecurity.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Arrays;
import java.util.Collection;

/**
 * 自定义投票器,这里比较的是ConfigAttribute与GrantedAuthority的值
 * Created by Lee0609x
 * Date:2020/5/16
 */
public class CustomVoter implements AccessDecisionVoter<FilterInvocation> {

    private static final Logger logger = LoggerFactory.getLogger(CustomVoter.class);

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            for (ConfigAttribute configAttribute : attributes) {
                if (grantedAuthority.getAuthority() != null && configAttribute.getAttribute() != null
                        && grantedAuthority.getAuthority().equals(configAttribute.getAttribute())) {
                    logger.info(String.format("角色匹配成功, 用户拥有%s角色", grantedAuthority.getAuthority()));
                    return ACCESS_GRANTED;
                }
            }
        }
        logger.info(String.format("当前用户权限不足, 期望角色:%s, 实际角色:%s",
                Arrays.toString(attributes.toArray()), Arrays.toString(authentication.getAuthorities().toArray())));
        return ACCESS_DENIED;
    }
}
