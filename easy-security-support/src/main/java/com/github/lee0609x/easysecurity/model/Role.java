package com.github.lee0609x.easysecurity.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Lee0609x
 * Date:2020/5/12
 */
public class Role implements GrantedAuthority {

    private String id;
    private String name;
    private String chineseName;

    @Override
    public String getAuthority() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

}
