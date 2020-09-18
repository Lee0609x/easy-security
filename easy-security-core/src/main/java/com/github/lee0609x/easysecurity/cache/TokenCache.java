package com.github.lee0609x.easysecurity.cache;

import com.github.lee0609x.easysecurity.model.token.EasySecurityRefreshToken;
import com.github.lee0609x.easysecurity.model.token.EasySecurityUserToken;

/**
 * Created by Lee0609x
 * Date:2020/9/16
 */
public interface TokenCache extends Cache {
    boolean createOrUpdateUserToken(String id, EasySecurityUserToken userToken);
    boolean deleteUserToken(String id);
    EasySecurityUserToken retrieveUserToken(String id);

    boolean createOrUpdateRefreshToken(String id, EasySecurityRefreshToken refreshToken);
    boolean deleteRefreshToken(String id);
    EasySecurityRefreshToken retrieveRefreshToken(String id);
}
