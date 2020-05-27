package com.github.lee0609x.easysecurity.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Lee0609x
 * Date:2020/5/23
 */
@ConfigurationProperties(prefix = "easy-security")
public class EasySecurityConfigProperties {
    private boolean enable = true;
    private boolean api = true;
    private boolean sql = true;
    private Page page = new Page();
    private Jwt jwt = new Jwt();

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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public static class Jwt {
        private long timeOut = 1;

        public long getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(long timeOut) {
            this.timeOut = timeOut;
        }
    }

    public static class Page {

        private String needLogin = "/easy-security/status/401";//需要登录
        private String http403 = "/easy-security/status/403";//权限不足
        private String loginSuccess = "/easy-security/status/login/success";//登录成功
        private String loginFailure = "/easy-security/status/login/failure";//登录失败
        private String login = "/easy-security/login";//登录
        private String logout = "/easy-security/logout";//注销
        private String logoutSuccess = "/easy-security/status/logout/success";//注销成功

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getLoginSuccess() {
            return loginSuccess;
        }

        public void setLoginSuccess(String loginSuccess) {
            this.loginSuccess = loginSuccess;
        }

        public String getLoginFailure() {
            return loginFailure;
        }

        public void setLoginFailure(String loginFailure) {
            this.loginFailure = loginFailure;
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

        public String getLogoutSuccess() {
            return logoutSuccess;
        }

        public void setLogoutSuccess(String logoutSuccess) {
            this.logoutSuccess = logoutSuccess;
        }
    }
}
