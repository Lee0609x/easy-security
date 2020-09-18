package com.github.lee0609x.easysecurity.cache;

/**
 * Created by Lee0609x
 * Date:2020/9/16
 */
public interface Cache {
    String getName();
    <T> T get(Object key, Class<T> myClass);
    void put(Object key, Object value);
    void clear();
}
