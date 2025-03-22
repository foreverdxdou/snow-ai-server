package com.dxdou.snowai.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * JWT配置类
 *
 * @author foreverdxdou
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String TOKEN_PREFIX = "token:";
    private static final String USER_PREFIX = "user:";

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间（毫秒）
     */
    private long expiration;

    public JwtConfig(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成JWT令牌
     *
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateToken(String username) {
        String uuid = UUID.randomUUID().toString();
        String token = generateTokenWithUUID(uuid);
        
        // 存储token和用户名的映射关系
        redisTemplate.opsForValue().set(TOKEN_PREFIX + uuid, username, expiration, TimeUnit.MILLISECONDS);
        // 存储用户名和token的映射关系
        redisTemplate.opsForValue().set(USER_PREFIX + username, uuid, expiration, TimeUnit.MILLISECONDS);
        
        return token;
    }

    /**
     * 生成JWT令牌
     *
     * @param uuid UUID
     * @return JWT令牌
     */
    private String generateTokenWithUUID(String uuid) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uuid", uuid);
        
        Instant now = Instant.now();
        Date expirationDate = new Date(now.toEpochMilli() + expiration);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String uuid = claims.get("uuid", String.class);
            return redisTemplate.hasKey(TOKEN_PREFIX + uuid);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从JWT令牌中获取用户名
     *
     * @param token JWT令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String uuid = claims.get("uuid", String.class);
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + uuid);
    }

    /**
     * 从JWT令牌中获取声明信息
     *
     * @param token JWT令牌
     * @return 声明信息
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 使token失效
     *
     * @param token JWT令牌
     */
    public void invalidateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String uuid = claims.get("uuid", String.class);
        String username = redisTemplate.opsForValue().get(TOKEN_PREFIX + uuid);
        
        if (username != null) {
            redisTemplate.delete(TOKEN_PREFIX + uuid);
            redisTemplate.delete(USER_PREFIX + username);
        }
    }
}