package com.github.lee0609x.easysecurity.model.token;

import java.util.Date;

/**
 * Created by Lee0609x
 * Date:2020/9/16
 */
public class EasySecurityRefreshToken extends EasySecurityToken {
    private String userId;
    private Date issueDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
}
