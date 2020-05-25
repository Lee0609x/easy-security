package com.github.lee0609x.easysecurity.model;

/**
 * Created by Lee0609x
 * Date:2020/5/12
 */
public class Resource {
    private String id;
    private String url;
    private String method;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
