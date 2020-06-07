package com.github.lee0609x.easysecurity.autoconfigure.properties;

import com.github.lee0609x.easysecurity.constants.EasyConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import sun.jvm.hotspot.debugger.Page;

/**
 * Created by Lee0609x
 * Date:2020/5/23
 */
@ConfigurationProperties(prefix = "easy-security")
public class EasySecurityConfigProperties {
    private boolean enable = true;
    private boolean api = true;
    private boolean sql = true;
    private Jwt jwt = new Jwt();
    private Request request = new Request();

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }

    public boolean isSql() {
        return sql;
    }

    public void setSql(boolean sql) {
        this.sql = sql;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public static class Jwt {
        private long timeOut = 1000 * 60 * 30;
        private String header = EasyConstants.JWT_DEFAULT_HTTP_HEAD_NAME;

        public long getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(long timeOut) {
            this.timeOut = timeOut;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }
    }

    public static class Request {

        private String needLogin = "/easy-security/status/401";//需要登录
        private String http403 = "/easy-security/status/403";//权限不足
        private String login = "/easy-security/login";//登录请求拦截
        private String logout = "/easy-security/logout";//注销请求拦截
        private String renewal = "/easy-security/renewal";//token续签请求拦截

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getHttp403() {
            return http403;
        }

        public void setHttp403(String http403) {
            this.http403 = http403;
        }

        public String getNeedLogin() {
            return needLogin;
        }

        public void setNeedLogin(String needLogin) {
            this.needLogin = needLogin;
        }

        public String getLogout() {
            return logout;
        }

        public void setLogout(String logout) {
            this.logout = logout;
        }

        public String getRenewal() {
            return renewal;
        }

        public void setRenewal(String renewal) {
            this.renewal = renewal;
        }
    }
}
