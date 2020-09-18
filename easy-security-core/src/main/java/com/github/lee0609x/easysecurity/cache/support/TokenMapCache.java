package com.github.lee0609x.easysecurity.cache.support;

import com.github.lee0609x.easysecurity.cache.TokenCache;
import com.github.lee0609x.easysecurity.model.token.EasySecurityRefreshToken;
import com.github.lee0609x.easysecurity.model.token.EasySecurityUserToken;

import java.util.*;

/**
 * Created by Lee0609x
 * Date:2020/9/17
 */
public class TokenMapCache implements TokenCache {

    private Map<String, EasySecurityUserToken> userTokenMapCache = new LinkedHashMap<>();
    private Map<String, EasySecurityRefreshToken> refreshTokenMapCache = new LinkedHashMap<>();
    private long tokenTimeout = 60000;

    public TokenMapCache() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 60000);
    }

    protected void refresh() {
        userTokenMapCache.keySet().forEach(key -> {
            if (isExpire(userTokenMapCache.get(key))) {
                userTokenMapCache.remove(key);
            }
        });
        refreshTokenMapCache.keySet().forEach(key -> {
            if (isExpire(refreshTokenMapCache.get(key))) {
                refreshTokenMapCache.remove(key);
            }
        });
    }

    /**
     * 检查token是否过期
     */
    protected boolean isExpire(EasySecurityUserToken userToken) {
        return new Date().getTime() > userToken.getIssueDate().getTime() + tokenTimeout;
    }

    /**
     * 检查token是否过期
     */
    protected boolean isExpire(EasySecurityRefreshToken refreshToken) {
        return new Date().getTime() > refreshToken.getIssueDate().getTime() + tokenTimeout;
    }

    @Override
    public boolean createOrUpdateUserToken(String id, EasySecurityUserToken userToken) {
        return userTokenMapCache.put(id, userToken) != null;
    }

    @Override
    public boolean deleteUserToken(String id) {
        return userTokenMapCache.remove(id) != null;
    }

    @Override
    public EasySecurityUserToken retrieveUserToken(String id) {
        EasySecurityUserToken userToken = userTokenMapCache.get(id);
        if (!isExpire(userToken)) {
            return userToken;
        }
        return null;
    }

    @Override
    public boolean createOrUpdateRefreshToken(String id, EasySecurityRefreshToken refreshToken) {
        return refreshTokenMapCache.put(id, refreshToken) != null;
    }

    @Override
    public boolean deleteRefreshToken(String id) {
        return refreshTokenMapCache.remove(id) != null;
    }

    @Override
    public EasySecurityRefreshToken retrieveRefreshToken(String id) {
        EasySecurityRefreshToken refreshToken = refreshTokenMapCache.get(id);
        if (!isExpire(refreshToken)) {
            return refreshToken;
        }
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> myClass) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void clear() {
        userTokenMapCache.keySet().forEach(key -> {
            userTokenMapCache.remove(key);
        });
        refreshTokenMapCache.keySet().forEach(key -> {
            refreshTokenMapCache.remove(key);
        });
    }


}
