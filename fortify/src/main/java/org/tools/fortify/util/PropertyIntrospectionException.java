package org.tools.fortify.util;


/**
 * 属性内省异常类
 * <p>
 * 当在进行属性内省操作时发生错误，会抛出此异常。
 * </p>
 */
public class PropertyIntrospectionException extends RuntimeException {

    /**
     * 构造一个新的属性内省异常
     *
     * @param message 异常信息
     * @param cause   异常原因
     */
    public PropertyIntrospectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
