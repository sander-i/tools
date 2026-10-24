package org.tools.encrypt.basic;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTUtils 冒烟测试
 * 覆盖全部公开方法：generatorToken×4 / parseToken×2 / sign 读写
 *
 * @author sander 2026/10/24
 */
public class JWTUtilsTest {

    private static final String SECRET = "jwt-smoke-test-secret";

    private static Map<String, String> payload() {
        Map<String, String> payload = new HashMap<>();
        payload.put("userId", "123");
        payload.put("name", "tools");
        return payload;
    }

    @Test
    public void testGetAndSetSign() {
        String old = JWTUtils.getSign();
        try {
            JWTUtils.setSign(SECRET);
            Assert.assertEquals(SECRET, JWTUtils.getSign());
            // 无 sign 参数重载使用静态 sign 字段
            String token = JWTUtils.generatorToken(payload());
            Assert.assertNotNull(JWTUtils.parseToken(token));
        } finally {
            JWTUtils.setSign(old);
        }
    }

    @Test
    public void testGeneratorToken() {
        String old = JWTUtils.getSign();
        try {
            JWTUtils.setSign(SECRET);
            Assert.assertNotNull(JWTUtils.generatorToken(payload()));
        } finally {
            JWTUtils.setSign(old);
        }
    }

    @Test
    public void testGeneratorTokenWithSign() {
        Assert.assertNotNull(JWTUtils.generatorToken(payload(), SECRET));
    }

    @Test
    public void testGeneratorTokenWithDate() {
        String old = JWTUtils.getSign();
        try {
            JWTUtils.setSign(SECRET);
            Date expiresAt = new Date(System.currentTimeMillis() + 60_000L);
            Assert.assertNotNull(JWTUtils.generatorToken(payload(), expiresAt));
        } finally {
            JWTUtils.setSign(old);
        }
    }

    @Test
    public void testGeneratorTokenWithDateAndSign() {
        Date expiresAt = new Date(System.currentTimeMillis() + 60_000L);
        String token = JWTUtils.generatorToken(payload(), expiresAt, SECRET);
        Assert.assertNotNull(token);
    }

    @Test
    public void testParseToken() {
        String old = JWTUtils.getSign();
        try {
            JWTUtils.setSign(SECRET);
            String token = JWTUtils.generatorToken(payload());
            Map<String, String> parsed = JWTUtils.parseToken(token);
            Assert.assertEquals("123", parsed.get("userId"));
            Assert.assertEquals("tools", parsed.get("name"));
        } finally {
            JWTUtils.setSign(old);
        }
    }

    @Test
    public void testParseTokenWithSign() {
        String token = JWTUtils.generatorToken(payload(), SECRET);
        Map<String, String> parsed = JWTUtils.parseToken(token, SECRET);
        Assert.assertEquals("123", parsed.get("userId"));
    }

    @Test(expected = JWTVerificationException.class)
    public void testParseTokenWithWrongSignRejected() {
        String token = JWTUtils.generatorToken(payload(), SECRET);
        JWTUtils.parseToken(token, "wrong-secret");
    }

}
