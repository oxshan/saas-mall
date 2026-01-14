package cc.oxshan.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtUtils 单元测试
 */
@DisplayName("JWT 工具类测试")
class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private static final String SECRET = "your-256-bit-secret-key-here-must-be-long-enough";
    private static final Long EXPIRATION = 7200000L;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "expiration", EXPIRATION);
    }

    @Test
    @DisplayName("生成 Token 成功")
    void generateToken_Success() {
        // Given
        Long userId = 1L;
        Long shopId = 100L;
        String username = "admin";
        Integer roleType = 0;

        // When
        String token = jwtUtils.generateToken(userId, shopId, username, roleType);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // 验证 Token 内容
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals(userId.intValue(), claims.get("userId", Integer.class));
        assertEquals(shopId.intValue(), claims.get("shopId", Integer.class));
        assertEquals(username, claims.get("username", String.class));
        assertEquals(roleType, claims.get("roleType", Integer.class));
        assertEquals(username, claims.getSubject());
    }

    @Test
    @DisplayName("生成 Token 包含正确的过期时间")
    void generateToken_HasCorrectExpiration() {
        // Given
        Long userId = 1L;
        Long shopId = 100L;
        String username = "admin";
        Integer roleType = 0;

        // When
        long beforeGenerate = System.currentTimeMillis();
        String token = jwtUtils.generateToken(userId, shopId, username, roleType);
        long afterGenerate = System.currentTimeMillis();

        // Then
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        long expTime = claims.getExpiration().getTime();
        long issuedAt = claims.getIssuedAt().getTime();
        // 验证过期时间 = 签发时间 + 有效期
        assertEquals(EXPIRATION, expTime - issuedAt);
    }
}
