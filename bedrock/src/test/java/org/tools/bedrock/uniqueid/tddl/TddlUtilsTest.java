package org.tools.bedrock.uniqueid.tddl;

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
 * TddlUtils 冒烟测试
 * 覆盖公开方法：of / next()（实例）/ nextId（静态便捷入口、long/String 机器标识）/ 批量 + 并发唯一性
 *
 * @author sander 2026/10/24
 */
public class TddlUtilsTest {

    @Test
    public void testNextIdInstance() {
        TddlUtils tddl = TddlUtils.of("01");
        String first = tddl.next();
        String second = tddl.next();

        Assert.assertEquals("唯一键长度应为 22 位", 22, first.length());
        // 结构：14 位时间戳 + 2 位机器 + 6 位序列
        Assert.assertTrue(first.matches("\\d{22}"));
        Assert.assertEquals("01", first.substring(17, 19));
        Assert.assertTrue("同实例同毫秒序列号应递增", second.compareTo(first) > 0);
    }

    @Test
    public void testNextIdStatic() {
        String id = TddlUtils.nextId();
        Assert.assertEquals(22, id.length());

        String byLong = TddlUtils.nextId(5L);
        Assert.assertEquals("05", byLong.substring(17, 19));

        String byStr = TddlUtils.nextId("99");
        Assert.assertEquals("99", byStr.substring(17, 19));
    }

    @Test
    public void testNextIdBatch() {
        List<String> ids = TddlUtils.nextId(3L, 20);
        Assert.assertEquals(20, ids.size());
        Assert.assertEquals(20, new HashSet<>(ids).size());
        for (String id : ids) {
            Assert.assertEquals("03", id.substring(17, 19));
        }
    }

    @Test
    public void testOfInvalidMachineRejected() {
        try {
            TddlUtils.of("1");
            Assert.fail("机器标识位数不足应抛出异常");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            TddlUtils.of("ab");
            Assert.fail("非数字机器标识应抛出异常");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            TddlUtils.of(100L);
            Assert.fail("机器标识越界应抛出异常");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * 并发唯一性：同实例多线程取号（synchronized 保证序列正确），全局无重复。
     */
    @Test
    public void testConcurrentUniqueSameInstance() throws Exception {
        TddlUtils tddl = TddlUtils.of("01");
        int threads = 8;
        int perThread = 200;
        Set<String> all = ConcurrentHashMap.newKeySet();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) {
                        all.add(tddl.next());
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
