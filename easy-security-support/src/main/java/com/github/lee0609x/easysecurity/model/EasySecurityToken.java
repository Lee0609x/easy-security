package com.github.lee0609x.easysecurity.model;

/**
 * 登录成功返回对象
 * Created by Lee0609x
 * Date:2020/6/6
 */
public class EasySecurityToken {
    private String userID;
    private String tokenHeaderName;
    private String tokenHeaderValue;
    private long timeout;

    public EasySecurityToken(String userID, String tokenHeaderName, String tokenHeaderValue, long timeout) {
        this.userID = userID;
        this.tokenHeaderName = tokenHeaderName;
        this.tokenHeaderValue = tokenHeaderValue;
        this.timeout = timeout;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTokenHeaderName() {
        return tokenHeaderName;
    }

    public void setTokenHeaderName(String tokenHeaderName) {
        this.tokenHeaderName = tokenHeaderName;
    }

    public String getTokenHeaderValue() {
        return tokenHeaderValue;
    }

    public void setTokenHeaderValue(String tokenHeaderValue) {
        this.tokenHeaderValue = tokenHeaderValue;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
