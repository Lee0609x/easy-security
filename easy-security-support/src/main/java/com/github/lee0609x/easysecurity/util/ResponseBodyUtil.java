package com.github.lee0609x.easysecurity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lee0609x.easysecurity.model.ResponseBody;
import com.github.lee0609x.easysecurity.model.ResponseBodyStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Lee0609x
 * Date:2020/5/24
 */
public class ResponseBodyUtil {

    public static <T> ResponseBody<T> successResponse(T data) {
        ResponseBody<T> responseBody = new ResponseBody<>();
        responseBody.setCode(ResponseBodyStatus.SUCCESS.getCode());
        responseBody.setMessage(ResponseBodyStatus.SUCCESS.getMessage());
        responseBody.setData(data);
        return responseBody;
    }

    public static ResponseBody<String> errorResponse(ResponseBodyStatus responseBodyStatus) {
        ResponseBody<String> responseBody = new ResponseBody<>();
        responseBody.setCode(responseBodyStatus.getCode());
        responseBody.setMessage(responseBodyStatus.getMessage());
        responseBody.setData("");
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

    public static void responseWrite(HttpServletResponse response, ResponseBody<?> responseBody) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(JsonUtil.Object2Json(responseBody));
    }

}
