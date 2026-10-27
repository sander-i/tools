package org.tools.bedrock.uniqueid.snowflake;

import java.util.ArrayList;
import java.util.List;

/**
 * 雪花 ID（Snowflake）生成工具：64 位长整数 = 1 位符号位 + 41 位毫秒时间戳 + 10 位机器标识 + 12 位序列号，
 * 单机每毫秒最多生成 4096 个，可使用约 69 年（相对自定义纪元）。
 *
 * <p>纯 JDK 实现（bedrock 基础包不引入任何第三方依赖）。线程安全：
 * 通过 synchronized 保证同一 workerId 下 id 单调递增、全局唯一。
 * 时钟回拨小于 {@link #MAX_BACKWARD_MS} 时等待追平；超过阈值抛出 IllegalStateException。</p>
 *
 * <p>使用方式：</p>
 * <pre>{@code
 * SnowflakeUtils snowflake = SnowflakeUtils.of(1L);    // workerId=1，dataCenterId=0
 * long id = snowflake.next();                          // 实例取号
 * long id2 = SnowflakeUtils.nextId();                  // 静态便捷入口（workerId=0，单机场景）
 * }</pre>
 *
 * <p>workerId 与 dataCenterId 位数共 10 位：多实例部署时须保证 (dataCenterId, workerId)
 * 组合不重复，否则可能产生重复 ID。</p>
 *
 * @author sander
 * @version 1.0
 * @since 2.0.0
 */
public class SnowflakeUtils {
    /**
     * 自定义纪元：2020-01-01T00:00:00Z（毫秒），时间戳部分以此为起点
     */
    private static final long EPOCH = 1577836800000L;

    /**
     * 机器标识位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据中心标识位数
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * 序列号位数（单毫秒最多 4096 个）
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器标识左移位数
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心标识左移位数
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 序列号掩码（4095）
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器标识上限（31）
     */
    public static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 数据中心标识上限（31）
     */
    public static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 允许的最大时钟回拨毫秒数，超过则抛出异常
     */
    public static final long MAX_BACKWARD_MS = 5L;

    /**
     * 上一次生成 ID 的时间戳（毫秒）
     */
    private long lastTimestamp = -1L;

    /**
     * 当前毫秒内的序列号
     */
    private long sequence = 0L;

    /**
     * 机器标识
     */
    private final long workerId;

    /**
     * 数据中心标识
     */
    private final long dataCenterId;

    private SnowflakeUtils(long workerId, long dataCenterId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("workerId 必须在 [0, " + MAX_WORKER_ID + "] 内: " + workerId);
        }
        if (dataCenterId < 0 || dataCenterId > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException("dataCenterId 必须在 [0, " + MAX_DATA_CENTER_ID + "] 内: "
                    + dataCenterId);
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 创建绑定 (workerId, dataCenterId) 的雪花 ID 生成实例。
     *
     * @param workerId     机器标识，范围 [0, 31]
     * @param dataCenterId 数据中心标识，范围 [0, 31]
     * @return 生成器实例
     */
    public static SnowflakeUtils of(long workerId, long dataCenterId) {
        return new SnowflakeUtils(workerId, dataCenterId);
    }

    /**
     * 创建绑定 workerId 的雪花 ID 生成实例（dataCenterId 固定为 0）。
     *
     * @param workerId 机器标识，范围 [0, 31]
     * @return 生成器实例
     */
    public static SnowflakeUtils of(long workerId) {
        return of(workerId, 0L);
    }

    /**
     * 生成下一个全局唯一 ID（阻塞至时间戳推进或时钟追平）。
     *
     * @return 64 位雪花 ID
     * @throws IllegalStateException 时钟回拨超过 {@link #MAX_BACKWARD_MS} 毫秒
     */
    public synchronized long next() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            long backward = lastTimestamp - timestamp;
            if (backward > MAX_BACKWARD_MS) {
                throw new IllegalStateException("时钟回拨 " + backward + "ms，超过允许阈值 "
                        + MAX_BACKWARD_MS + "ms，拒绝生成 ID");
            }
            // 回拨幅度小，等待追平
            timestamp = waitUntil(lastTimestamp);
        }

        if (timestamp == lastTimestamp) {
            // 同一毫秒内序列号递增
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 当前毫秒序列号用尽，等待下一毫秒
                timestamp = waitUntil(lastTimestamp + 1);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 使用默认 workerId=0、dataCenterId=0 生成一个雪花 ID。
     *
     * <p>便捷入口：仅适用于单机场景；多实例部署请使用 {@link #of(long, long)} 创建实例并保证标识组合唯一。</p>
     *
     * @return 64 位雪花 ID
     */
    public static long nextId() {
        return DefaultHolder.INSTANCE.next();
    }

    /**
     * 使用默认 workerId=0、dataCenterId=0 生成指定数量的雪花 ID。
     *
     * @param size 数量（必须大于 0）
     * @return ID 列表
     */
    public static List<Long> nextId(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size 必须大于 0: " + size);
        }
        List<Long> ids = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ids.add(nextId());
        }
        return ids;
    }

    /**
     * 反解析雪花 ID 中的生成时间戳（毫秒，自定义纪元已还原为 Unix 毫秒）。
     *
     * @param id 雪花 ID
     * @return 生成时间的 Unix 毫秒时间戳
     */
    public static long parseTimestamp(long id) {
        return (id >> TIMESTAMP_SHIFT) + EPOCH;
    }

    /**
     * 反解析雪花 ID 中的数据中心标识。
     *
     * @param id 雪花 ID
     * @return 数据中心标识
     */
    public static long parseDataCenterId(long id) {
        return (id >> DATA_CENTER_ID_SHIFT) & ~(-1L << DATA_CENTER_ID_BITS);
    }

    /**
     * 反解析雪花 ID 中的机器标识。
     *
     * @param id 雪花 ID
     * @return 机器标识
     */
    public static long parseWorkerId(long id) {
        return (id >> WORKER_ID_SHIFT) & ~(-1L << WORKER_ID_BITS);
    }

    /**
     * 阻塞直到系统时钟达到目标毫秒。
     *
     * @param targetTimestamp 目标毫秒时间戳
     * @return 达到目标后的当前时间戳
     */
    private long waitUntil(long targetTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp < targetTimestamp) {
            Thread.yield();
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 默认实例持有者（workerId=0，dataCenterId=0）
     * @since 2.0.0
     */
    private static class DefaultHolder {
        private static final SnowflakeUtils INSTANCE = new SnowflakeUtils(0L, 0L);
    }
}
