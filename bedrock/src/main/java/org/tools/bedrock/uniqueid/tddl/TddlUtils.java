package org.tools.bedrock.uniqueid.tddl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TDDL 风格唯一键生成工具：形如 {@code 20241231120000123450 0001} 的字符串主键，
 * 由 <b>时间戳(17 位，毫秒精度 yyyyMMddHHmmssSSS) + 机器标识(2 位) + 自增序列(3 位)</b> 拼接而成，共 22 位。
 *
 * <p>纯 JDK 实现（bedrock 基础包不引入任何第三方依赖）。线程安全：同一机器标识的取号
 * 通过 synchronized + 实例缓存保证序列连续；机器标识须在多实例部署时保证不重复。
 * 时间戳必须为毫秒精度——若只到秒级，同一秒内跨毫秒的序列号重置会产生重复 ID。</p>
 *
 * <p>使用方式：</p>
 * <pre>{@code
 * String id = TddlUtils.nextId(1L);              // 机器标识 1
 * String id2 = TddlUtils.nextId("01");           // 机器标识字符串 "01"
 * TddlUtils tddl = TddlUtils.of("01");           // 复用实例
 * List<String> ids = TddlUtils.nextId(1L, 10);   // 批量
 * }</pre>
 *
 * @author sander
 * @version 1.0
 * @since 2.0.0
 */
public class TddlUtils {
    /**
     * 时间戳部分格式：yyyyMMddHHmmssSSS（17 位，毫秒精度）
     */
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 序列号位数
     */
    private static final int SEQUENCE_BITS = 3;

    /**
     * 序列号上限（999，单毫秒内最多 999 个）
     */
    private static final long MAX_SEQUENCE = 999L;

    /**
     * 机器标识位数
     */
    private static final int MACHINE_BITS = 2;

    /**
     * 默认机器标识
     */
    private static final String DEFAULT_MACHINE = "00";

    /**
     * 序列号（按毫秒重置：同毫秒内递增，跨毫秒归零）
     */
    private final AtomicLong sequence = new AtomicLong();

    /**
     * 上一次取号的毫秒时间戳
     */
    private volatile long lastMillis = 0L;

    /**
     * 机器标识（固定 2 位字符串）
     */
    private final String machine;

    /**
     * 实例缓存：同一机器标识共享序列号状态，保证同毫秒取号不重复
     */
    private static final java.util.concurrent.ConcurrentHashMap<String, TddlUtils> INSTANCES =
            new java.util.concurrent.ConcurrentHashMap<>();

    private TddlUtils(String machine) {
        if (machine == null || machine.length() != MACHINE_BITS || !machine.chars().allMatch(c -> c >= '0' &&
                c <= '9')) {
            throw new IllegalArgumentException("machine 必须是 " + MACHINE_BITS + " 位数字字符串: " + machine);
        }
        this.machine = machine;
    }

    /**
     * 获取绑定机器标识的 TDDL 生成实例（同一机器标识共享序列号状态）。
     *
     * @param machine 机器标识（2 位数字字符串，多实例部署时须保证不重复）
     * @return 生成器实例
     */
    public static TddlUtils of(String machine) {
        if (machine == null || machine.length() != MACHINE_BITS || !machine.chars().allMatch(c -> c >= '0' &&
                c <= '9')) {
            throw new IllegalArgumentException("machine 必须是 " + MACHINE_BITS + " 位数字字符串: " + machine);
        }
        return INSTANCES.computeIfAbsent(machine, TddlUtils::new);
    }

    /**
     * 创建绑定机器标识的 TDDL 生成实例。
     *
     * @param machineId 机器标识（取模映射为 2 位数字，范围 [0, 99]）
     * @return 生成器实例
     */
    public static TddlUtils of(long machineId) {
        if (machineId < 0 || machineId > 99) {
            throw new IllegalArgumentException("machineId 必须在 [0, 99] 内: " + machineId);
        }
        return of(String.format("%02d", machineId));
    }

    /**
     * 生成下一个 TDDL 唯一键（默认机器标识 "00"）。
     *
     * <p>便捷入口：仅适用于单机场景；多实例部署请使用 {@link #of(String)} 创建实例并保证机器标识唯一。</p>
     *
     * @return 22 位唯一键字符串
     */
    public static String nextId() {
        return DefaultHolder.INSTANCE.next();
    }

    /**
     * 生成指定机器标识的 TDDL 唯一键。
     *
     * @param machineId 机器标识（取模映射为 2 位数字，范围 [0, 99]）
     * @return 22 位唯一键字符串
     */
    public static String nextId(long machineId) {
        return of(machineId).next();
    }

    /**
     * 生成指定机器标识的 TDDL 唯一键。
     *
     * @param machine 机器标识（2 位数字字符串）
     * @return 22 位唯一键字符串
     */
    public static String nextId(String machine) {
        return of(machine).next();
    }

    /**
     * 生成指定数量的唯一键（默认机器标识 "00"）。
     *
     * @param size 数量（必须大于 0）
     * @return 唯一键列表
     */
    public static List<String> nextId(int size) {
        return nextId(DEFAULT_MACHINE, size);
    }

    /**
     * 生成指定数量的唯一键。
     *
     * @param machineId 机器标识（取模映射为 2 位数字，范围 [0, 99]）
     * @param size      数量（必须大于 0）
     * @return 唯一键列表
     */
    public static List<String> nextId(long machineId, int size) {
        return nextId(String.format("%02d", machineId), size);
    }

    /**
     * 生成指定数量的唯一键。
     *
     * @param machine 机器标识（2 位数字字符串）
     * @param size    数量（必须大于 0）
     * @return 唯一键列表
     */
    public static List<String> nextId(String machine, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size 必须大于 0: " + size);
        }
        TddlUtils tddl = of(machine);
        List<String> ids = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ids.add(tddl.next());
        }
        return ids;
    }

    /**
     * 生成下一个唯一键：同毫秒内序列号递增，毫秒内取号超过 {@value #MAX_SEQUENCE} 时
     * 等待进入下一毫秒。
     *
     * @return 22 位唯一键字符串（时间戳 17 位 + 机器 2 位 + 序列 3 位）
     */
    public synchronized String next() {
        long millis = System.currentTimeMillis();
        long seq;
        if (millis == lastMillis) {
            seq = sequence.incrementAndGet();
            if (seq > MAX_SEQUENCE) {
                // 当前毫秒序列用尽，等待进入下一毫秒
                while (millis == lastMillis) {
                    millis = System.currentTimeMillis();
                    Thread.yield();
                }
                sequence.set(1);
                seq = 1;
            }
        } else {
            lastMillis = millis;
            sequence.set(1);
            seq = 1;
        }

        String timestamp = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(millis), java.time.ZoneId
                .systemDefault())
                .format(TIMESTAMP_FORMAT);
        return timestamp + machine + String.format("%03d", seq);
    }

    /**
     * 默认实例持有者（机器标识 "00"）
     *
     * @since 2.0.0
     */
    private static class DefaultHolder {
        private static final TddlUtils INSTANCE = new TddlUtils(DEFAULT_MACHINE);
    }
}
