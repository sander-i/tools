package org.tools.encrypt;

import javax.crypto.KeyGenerator;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * 密钥生成器工厂。
 *
 * <p>无状态设计：每次调用返回<b>新创建</b>的生成器实例。JCA 的 KeyGenerator /
 * KeyPairGenerator 自身非线程安全（{@code init} / {@code generateKey} 会改变内部状态），
 * 跨线程共享同一个实例会互相污染——此前以静态单例缓存生成器，并发切换算法时
 * check-then-act 竞态会导致调用方拿到错误算法的生成器，已修复。
 * {@code getInstance} 仅是一次 Provider 查找，开销远小于密钥生成本身，无需缓存。</p>
 *
 * <p>线程安全：无共享可变状态，可并发使用。</p>
 *
 * @author sander
 * @version 1.0
 */
public class EncryptKeyGenerator {

    private EncryptKeyGenerator() {
    }

    /**
     * 创建指定算法的密钥生成器
     *
     * @param algorithm 算法
     * @return KeyGenerator（每次调用返回新实例，调用方独立持有）
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     */
    public static KeyGenerator getKeyGenerator(String algorithm) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(algorithm);
    }

    /**
     * 创建指定算法的密钥对生成器
     *
     * @param algorithm 算法
     * @return KeyPairGenerator（每次调用返回新实例，调用方独立持有）
     * @throws NoSuchAlgorithmException 算法不存在或不支持异常
     */
    public static KeyPairGenerator getKeyPairGenerator(String algorithm) throws NoSuchAlgorithmException {
        return KeyPairGenerator.getInstance(algorithm);
    }

}
