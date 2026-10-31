/**
 * Skipjack 对称加密包：密钥生成与编解码工具。
 *
 * <p>Skipjack 由 BouncyCastle Provider 提供（JDK 未内置），密钥固定 80 位（10 字节），
 * 分组长度 64 位（CBC 模式的 IV 必须为 8 字节）。</p>
 *
 * @author sander
 * @since 2.0.1
 */
package org.tools.encrypt.symmetrical.skipjack;
