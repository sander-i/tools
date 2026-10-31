/**
 * RC4 流密码对称加密包：密钥生成与编解码工具。
 *
 * <p>RC4 为<b>流密码</b>：无 IV、无填充、无分组，密文长度恒等于明文长度。
 * 密钥为 Hex 字符串，默认 128 位。RC4 存在已知的密钥流统计偏差，
 * 不建议在新系统中用于保护敏感数据。</p>
 *
 * @author sander
 * @since 2.0.1
 */
package org.tools.encrypt.symmetrical.rc.rc4;
