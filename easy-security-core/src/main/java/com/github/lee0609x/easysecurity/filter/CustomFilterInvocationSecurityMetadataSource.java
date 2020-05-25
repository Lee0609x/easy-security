package com.github.lee0609x.easysecurity.filter;

import com.github.lee0609x.easysecurity.model.Resource;
import com.github.lee0609x.easysecurity.model.Role;
import com.github.lee0609x.easysecurity.service.ResourceService;
import com.github.lee0609x.easysecurity.util.ResourceRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 权限资源管理器
 * 为权限决断器提供支持
 * 这里的实现为查询数据库中的角色权限
 * Created by Lee0609x
 * Date:2020/5/16
 */
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger logger = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);

    @Autowired
    private ResourceService resourceService;

    /**
     * 查找当前URL所需的所有角色，将角色名称返回
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Collection<ConfigAttribute> configAttributeCollection = new ArrayList<>();
        FilterInvocation fi = (FilterInvocation) object;
        Map<Role, List<Resource>> roleResourceMap = resourceService.retrieveRoleResourceMap();
        roleResourceMap.forEach((role, resourceList) -> {
            resourceList.forEach(resource -> {
                if (ResourceRequestMatcher.matches(resource, fi.getRequest())) {
                    logger.info(String.format("装配ConfigAttribute... 当前[Request URL:%s, method:%s] 已匹配[URL:%s, method:%s] 需要角色为:%s",
                            ResourceRequestMatcher.getRequestURL(fi.getRequest()), fi.getRequest().getMethod(), resource.getUrl(), resource.getMethod(), role.getName()));
                    //这里用了SecurityConfig来装ConfigAttribute，这里的ConfigAttribute即Role Name
                    configAttributeCollection.add(new SecurityConfig(role.getName()));
                }
            });
        });
        return configAttributeCollection;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new ArrayList<>();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        //判断其父类是否为FilterInvocation
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
