package org.tools.encrypt.basic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sander
 * @version 1.0
 * @date 2023/03/18 0:28
 * @description jwt
 * @scope basic
 */
@Deprecated
public class JWTUtils {

    private JWTUtils() {
    }

    private static String sign = "TOOLS&DH$*001";

    public static String getSign() {
        return sign;
    }

    public static void setSign(String sign) {
        JWTUtils.sign = sign;
    }

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
