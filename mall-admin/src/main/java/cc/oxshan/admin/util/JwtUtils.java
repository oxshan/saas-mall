package cc.oxshan.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类（仅用于生成 token）
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:your-256-bit-secret-key-here-must-be-long-enough}")
    private String secret;

    @Value("${jwt.expiration:7200000}")
    private Long expiration;

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_SHOP_ID = "shopId";
    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_ROLE_TYPE = "roleType";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Token
     */
    public String generateToken(Long userId, Long shopId, String username, Integer roleType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_SHOP_ID, shopId);
        claims.put(CLAIM_USERNAME, username);
        claims.put(CLAIM_ROLE_TYPE, roleType);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析 Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 获取用户ID
     */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get(CLAIM_USER_ID, Long.class);
    }
}
