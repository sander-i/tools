/**
 * DH 密钥协商包：密钥对生成与共享秘密协商。
 *
 * <p>DH 不是加密算法，本包不提供 {@code encode}/{@code decode}，也不接入
 * {@code AsymmetricUtils} / {@code CipherAsymmetric}。原始共享秘密需经 HKDF/SHA-256
 * 等 KDF 派生后才能作为对称密钥使用。</p>
 *
 * @author sander
 * @since 2.0.1
 */
package org.tools.encrypt.asymmetric.dh;
