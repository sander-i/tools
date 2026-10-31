package org.tools.encrypt.exception.asymmetric.dh;

import org.tools.encrypt.exception.asymmetric.AsymmetricException;

/**
 * DH 密钥协商异常。
 *
 * <p>用于统一包装密钥对生成、密钥解析与共享秘密协商过程中的 JCE 异常，
 * 并保留原始异常链。</p>
 *
 * @author sander 2026/10/31
 * @since 2.0.1
 */
public class DhException extends AsymmetricException {
    private static final long serialVersionUID = 6415283907716043861L;

    public DhException() {
    }

    public DhException(String error) {
        super(error);
    }

    public DhException(Exception exception) {
        super(exception);
    }

    public DhException(String error, Exception exception) {
        super(error, exception);
    }
}
