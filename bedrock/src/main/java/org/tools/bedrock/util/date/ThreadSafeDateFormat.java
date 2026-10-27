package org.tools.bedrock.util.date;

import java.text.DateFormat;

/**
 * 线程安全 DateFormat 基类：用 ThreadLocal 隔离各线程的格式化实例。
 *
 * @author sander
 * @version 1.0
 * @since 1.0.3
 */
public abstract class ThreadSafeDateFormat extends DateFormat {
    private static final long serialVersionUID = -3797785225203298722L;
}
