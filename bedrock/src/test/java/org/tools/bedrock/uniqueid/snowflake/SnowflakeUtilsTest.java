package org.tools.bedrock.uniqueid.snowflake;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SnowflakeUtils 冒烟测试
 * 覆盖公开方法：of / next()（实例）/ nextId（静态便捷入口）/ parse 系列反解析 + 并发唯一性
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class SnowflakeUtilsTest {
    @Test
    public void testNextIdInstance() {
        SnowflakeUtils snowflake = SnowflakeUtils.of(1L, 1L);
        long first = snowflake.next();
        long second = snowflake.next();
        Assert.assertTrue("同实例 ID 应单调递增", second > first);
    }

    @Test
    public void testNextIdStatic() {
        long id = SnowflakeUtils.nextId();
        Assert.assertTrue("雪花 ID 应为正数", id > 0);
        List<Long> ids = SnowflakeUtils.nextId(10);
        Assert.assertEquals(10, ids.size());
        Assert.assertEquals("批量 ID 不应重复", 10, new HashSet<>(ids).size());
    }

    @Test
    public void testOfInvalidWorkerIdRejected() {
        try {
            SnowflakeUtils.of(32L);
            Assert.fail("workerId 越界应抛出异常");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("workerId"));
        }
        try {
            SnowflakeUtils.of(0L, 32L);
            Assert.fail("dataCenterId 越界应抛出异常");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("dataCenterId"));
        }
    }

    @Test
    public void testParse() {
        SnowflakeUtils snowflake = SnowflakeUtils.of(7L, 3L);
        long id = snowflake.next();

        Assert.assertEquals(7L, SnowflakeUtils.parseWorkerId(id));
        Assert.assertEquals(3L, SnowflakeUtils.parseDataCenterId(id));
        long timestamp = SnowflakeUtils.parseTimestamp(id);
        long now = System.currentTimeMillis();
        Assert.assertTrue("解析出的时间应接近当前时间", Math.abs(now - timestamp) < 10_000L);
    }

    /**
     * 并发唯一性：多线程多实例（不同 workerId）取号，全局无重复。
      *
      * @throws Exception 执行过程中出现异常
     */
    @Test
    public void testConcurrentUnique() throws Exception {
        int threads = 8;
        int perThread = 500;
        Set<Long> all = ConcurrentHashMap.newKeySet();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            final SnowflakeUtils snowflake = SnowflakeUtils.of(t % 32L);
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) {
                        all.add(snowflake.next());
                    }
                } catch (Exception e) {
                    Assert.fail(e.getMessage());
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        Assert.assertTrue("并发取号未在超时内完成", done.await(60, java.util.concurrent.TimeUnit.SECONDS));
        pool.shutdownNow();

        Assert.assertEquals("并发取号应全局唯一", threads * perThread, all.size());
    }
}
