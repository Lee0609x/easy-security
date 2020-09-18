package com.github.lee0609x.easysecurity.cache;

import java.util.Collection;

/**
 * Created by Lee0609x
 * Date:2020/9/16
 */
public interface CacheManager {

    Cache getCache(String name);

    Collection<String> getCacheNames();
}
