package com.github.lee0609x.easysecurity.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lee0609x.easysecurity.model.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * Created by Lee0609x
 * Date:2020/5/12
 */
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private static final long overTime = 1000 * 3600;

    private static String generateToken(Map<String, Object> claim) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject("user-token")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + overTime))
                .addClaims(claim)
                .signWith(secretKey)
                .compact();
    }

    private static Claims parseToken(String token) throws SignatureException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 将Security User封装为JWT
     * @param securityUser
     * @return
     */
    public static String getTokenBySecurityUser(SecurityUser securityUser) throws JsonProcessingException {
        String json = JsonUtil.Object2Json(securityUser);
        logger.info(String.format("开始序列化SecurityUser:%s", json));
        Map<String, Object> claim = new HashMap<>();
        claim.put("securityUser", json);
        String jwt = generateToken(claim);
        logger.info(String.format("生成Token:%s", jwt));
        return jwt;
    }

    /**
     * 将JWT解析为Security User
     * @return
     */
    public static SecurityUser getSecurityUserByToken(String token) throws IOException, SignatureException {
        logger.info(String.format("解析Token:%s", token));
        Claims claims = parseToken(token);
        String tokenDetail = claims.get("securityUser").toString();
        logger.info(String.format("将JSON转换为SecurityUser:%s", tokenDetail));
        SecurityUser securityUser = JsonUtil.Json2Object(tokenDetail, SecurityUser.class);
        return securityUser;
    }

}
