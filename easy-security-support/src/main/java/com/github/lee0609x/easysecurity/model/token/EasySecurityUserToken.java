package com.github.lee0609x.easysecurity.model.token;

import com.github.lee0609x.easysecurity.model.Role;
import com.github.lee0609x.easysecurity.model.SecurityUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Lee0609x
 * Date:2020/9/16
 */
public class EasySecurityUserToken extends EasySecurityToken {
    private SecurityUser securityUser;
    private Date issueDate;

    public SecurityUser getSecurityUser() {
        return securityUser;
    }

    public void setSecurityUser(SecurityUser securityUser) {
        this.securityUser = securityUser;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
}
