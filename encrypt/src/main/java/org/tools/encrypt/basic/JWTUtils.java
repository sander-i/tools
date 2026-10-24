package org.tools.encrypt.basic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具（基于 auth0 java-jwt，签名算法固定 HS256 / HMAC256）。
 *
 * <p>线程安全：无共享可变状态，可并发使用。</p>
 *
 * <p><b>安全提示</b>：类内存在默认签名串 {@code TOOLS&DH$*001}（静态字段 {@code sign}，经 Lombok 生成的
 * setter 可改），未显式传入签名串的调用将使用该默认值，仅适用于演示。生产环境请始终使用带 {@code sign}
 * 参数的重载，并通过配置中心 / 环境变量注入强随机密钥。</p>
 *
 * @author sander
 * @version 1.0
 */
public class JWTUtils {

    private JWTUtils() {
    }

    @Getter
    @Setter
    private static String sign = "TOOLS&DH$*001";

    /**
     * 生成token
     *
     * @param map 参数
     * @return token
     */
    public static String generatorToken(Map<String, String> map) {
        return generatorToken(map, sign);
    }

    /**
     * 生成token
     *
     * @param map  参数
     * @param sign 签名
     * @return token
     */
    public static String generatorToken(Map<String, String> map, String sign) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        // 签名
        return builder.sign(Algorithm.HMAC256(sign));
    }

    /**
     * 生成token
     *
     * @param map  参数
     * @param date 有效时间
     * @return token
     */
    public static String generatorToken(Map<String, String> map, Date date) {
        return generatorToken(map, date, sign);
    }

    /**
     * 生成token
     *
     * @param map  参数
     * @param date 有效时间
     * @param sign 签名
     * @return token
     */
    public static String generatorToken(Map<String, String> map, Date date, String sign) {
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        builder.withExpiresAt(date);
        // 生成签名
        return builder.sign(Algorithm.HMAC256(sign));
    }

    /**
     * 解析token
     *
     * @param token 签名
     * @return token
     */
    public static Map<String, String> parseToken(String token) {
        return parseToken(token, sign);
    }

    /**
     * 解析token
     *
     * @param token 签名
     * @param sign  签名
     * @return token
     */
    public static Map<String, String> parseToken(String token, String sign) {
        Map<String, String> map = new HashMap<>();
        // 解析
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
        verify.getClaims().forEach((k, v) -> map.put(k, v.asString()));
        return map;
    }

}
