package com.github.lee0609x.easysecurity.model;

/**
 * Created by Lee0609x
 * Date:2020/5/24
 */
public enum ResponseBodyStatus {
    SUCCESS(0, "成功"), FAILURE(-1, "失败"),
    HTTP401(401, "当前用户未登录，需要登录方可继续操作"),
    HTTP403(403, "当前用户未持有该资源权限"),;

    private Integer code;
    private String message;

    private ResponseBodyStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
