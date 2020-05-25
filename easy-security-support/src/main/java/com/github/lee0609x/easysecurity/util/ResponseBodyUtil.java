package com.github.lee0609x.easysecurity.util;

import com.github.lee0609x.easysecurity.model.ResponseBody;
import com.github.lee0609x.easysecurity.model.ResponseBodyStatus;

/**
 * Created by Lee0609x
 * Date:2020/5/24
 */
public class ResponseBodyUtil {

    public static <T> ResponseBody<T> successResponse(T data) {
        ResponseBody<T> responseBody = new ResponseBody<>();
        responseBody.setCode(0);
        responseBody.setMessage("success");
        responseBody.setData(data);
        return responseBody;
    }

    public static <T> ResponseBody<T> errorResponse(ResponseBodyStatus responseBodyStatus, T data) {
        ResponseBody<T> responseBody = new ResponseBody<>();
        responseBody.setCode(responseBodyStatus.getCode());
        responseBody.setMessage(responseBodyStatus.getMessage());
        responseBody.setData(data);
        return responseBody;
    }

    public static <T> ResponseBody<T> errorResponse(Integer errorCode, String errorMessage, T data) {
        ResponseBody<T> responseBody = new ResponseBody<>();
        responseBody.setCode(errorCode);
        responseBody.setMessage(errorMessage);
        responseBody.setData(data);
        return responseBody;
    }

}
