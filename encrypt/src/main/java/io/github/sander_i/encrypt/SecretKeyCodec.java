package io.github.sander_i.encrypt;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import io.github.sander_i.bedrock.util.BaseUtils;
import io.github.sander_i.encrypt.exception.EncryptException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 密钥编解码工具
 *
 * <p>
 * 对称密钥在字符串与 {@link SecretKey} 之间转换时，必须保证编码与解码对称，
 * 否则还原出的密钥字节数与原密钥不一致，加解密必然失败。
 * </p>
 *
 * <p>
 * 本类采用带前缀的格式区分两种密钥来源：
 * </p>
 * <ul>
 *   <li>{@code hex:4a541823...} —— {@link #encode(SecretKey)} 产出的十六进制编码，
 *       解码时按 Hex 还原为原始字节</li>
 *   <li>无前缀 —— 用户配置里手写的明文密钥，按 UTF-8 取字节</li>
 * </ul>
 *
 * <p>
 * 加前缀而非靠内容猜测，是因为纯十六进制字符的明文密钥（如 {@code "abcdef12"}）
 * 与 Hex 编码无法从内容上区分，猜测会产生歧义。
 * </p>
 *
 * @author sander
 * @version 1.0
 */
public class SecretKeyCodec {

    private SecretKeyCodec() {
    }

    /**
     * 十六进制密钥前缀
     */
    public static final String HEX_PREFIX = "hex:";

    /**
     * 密钥对象编码为字符串
     *
     * @param secretKey 密钥对象
     * @return {@code hex:} 前缀加十六进制字符串
     */
    public static String encode(SecretKey secretKey) {
        if (BaseUtils.isNull(secretKey) || BaseUtils.isNull(secretKey.getEncoded())) {
            return null;
        }
        return HEX_PREFIX + Hex.encodeHexString(secretKey.getEncoded());
    }

    /**
     * 字符串解码为密钥对象
     *
     * <p>
     * 带 {@code hex:} 前缀按十六进制还原，否则按 UTF-8 取字节。
     * </p>
     *
     * @param algorithm 算法
     * @param secretKey 密钥字符串
     * @return 密钥对象
     * @throws EncryptException 十六进制内容非法时抛出
     */
    public static SecretKey decode(String algorithm, String secretKey) throws EncryptException {
        return new SecretKeySpec(decodeBytes(secretKey), algorithm);
    }

    /**
     * 字符串解码为密钥字节
     *
     * @param secretKey 密钥字符串
     * @return 密钥字节
     * @throws EncryptException 十六进制内容非法时抛出
     */
    public static byte[] decodeBytes(String secretKey) throws EncryptException {
        if (BaseUtils.isEmpty(secretKey)) {
            throw new EncryptException("secretKey 不能为空！");
        }
        if (secretKey.startsWith(HEX_PREFIX)) {
            String hex = secretKey.substring(HEX_PREFIX.length());
            try {
                return Hex.decodeHex(hex.toCharArray());
            } catch (DecoderException | IllegalArgumentException e) {
                throw new EncryptException("密钥的十六进制内容非法: " + secretKey, e);
            }
        }
        return secretKey.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 判断字符串是否为带前缀的十六进制编码
     *
     * @param secretKey 密钥字符串
     * @return 是否带 {@code hex:} 前缀
     */
    public static boolean isEncoded(String secretKey) {
        return BaseUtils.isNotEmpty(secretKey) && secretKey.startsWith(HEX_PREFIX);
    }

}
