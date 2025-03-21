package com.dxdou.snowai.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT配置类
 *
 * @author foreverdxdou
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * 密钥
     */
    private String secret;

    /**
     * 过期时间（毫秒）
     */
    private long expiration;

    /**
     * 生成JWT令牌
     *
     * @param username 用户名
     * @return JWT令牌
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return generateToken(claims);
    }

    /**
     * 生成JWT令牌
     *
     * @param claims 声明信息
     * @return JWT令牌
     */
    public String generateToken(Map<String, Object> claims) {
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
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
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
}