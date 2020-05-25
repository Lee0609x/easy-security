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

    public static class Page {

        private String login = "/easy-security/status/401";//需要登录跳转URL
        private String loginSuccess = "/easy-security/user/status";//登录成功跳转URL
        private String loginFailure = "/easy-security/user/status";//登录失败跳转URL
        private String http403 = "/easy-security/status/403";//权限不足跳转URL

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

    }
}
