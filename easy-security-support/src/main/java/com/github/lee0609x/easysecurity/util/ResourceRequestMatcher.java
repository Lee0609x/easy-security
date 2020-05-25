package com.github.lee0609x.easysecurity.util;

import com.github.lee0609x.easysecurity.model.Resource;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * 比较Resource与HttpRequest是否相同
 * Created by Lee0609x
 * Date:2020/5/17
 */
public class ResourceRequestMatcher {

    private static final String ALLOW_ALL_METHOD_MARK = "*";

    /**
     * 比较请求是否符合资源配置, 使用的是java正则
     * @param resource
     * @param request
     * @return
     */
    public static boolean matches(Resource resource, HttpServletRequest request) {
        Pattern pattern = Pattern.compile(resource.getUrl(), Pattern.CASE_INSENSITIVE);
        if (ALLOW_ALL_METHOD_MARK.equals(resource.getMethod())) {
            return pattern.matcher(getRequestURL(request)).matches();
        } else {
            HttpMethod resourceMethod = HttpMethod.valueOf(resource.getMethod());
            HttpMethod requestMethod = HttpMethod.valueOf(request.getMethod());
            return pattern.matcher(getRequestURL(request)).matches() && resourceMethod == requestMethod;
        }
    }

    /**
     * 获取request的全URL
     * @param request
     * @return
     */
    public static String getRequestURL(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String query = request.getQueryString();
        if (pathInfo != null || query != null) {
            StringBuilder sb = new StringBuilder(url);

            if (pathInfo != null) {
                sb.append(pathInfo);
            }

            if (query != null) {
                sb.append('?').append(query);
            }
            url = sb.toString();
        }
        return url;
    }

}
