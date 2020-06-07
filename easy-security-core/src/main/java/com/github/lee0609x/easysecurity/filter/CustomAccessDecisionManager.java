package com.github.lee0609x.easysecurity.filter;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.List;

/**
 * 自定义权限决断器
 * 判断用户是否拥有权限
 * 投票逻辑：
 *  只要存在一个voter同意，则通过验证
 *  其他情况为未通过
 * Created by Lee0609x
 * Date:2020/5/16
 */
public class CustomAccessDecisionManager extends AbstractAccessDecisionManager {

    public CustomAccessDecisionManager(List<AccessDecisionVoter<?>> decisionVoters) {
        super(decisionVoters);
    }

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        int pass = 0;
        List<AccessDecisionVoter<?>> decisionVoters = getDecisionVoters();
        for (AccessDecisionVoter voter : decisionVoters) {
            int vote = voter.vote(authentication, object, configAttributes);
            switch (vote) {
                case AccessDecisionVoter.ACCESS_GRANTED:
                    pass ++;
                    return;
                default:
                    break;
            }
        }
        //无通过
        if (pass == 0) {
            FilterInvocation fi = (FilterInvocation) object;
            throw new AccessDeniedException(messages.getMessage(
                    "AbstractAccessDecisionManager.accessDenied", String.format("请求:%s拒绝访问", fi.getRequest().getServletPath())));
        }
    }

}
