package com.github.lee0609x.easysecurity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Json->Object | Object->Json
 * Created by Lee0609x
 * Date:2020/5/18
 */
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
    }

    public static <T> T Json2Object(String json, Class<T> requiredType) throws IOException {
        return objectMapper.readValue(json, requiredType);
    }

    public static String Object2Json(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T Json2Object(InputStream inputStream, Class<T> requiredType) throws IOException {
        return objectMapper.readValue(inputStream, requiredType);
    }

}
