package org.tools.encrypt;

import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyGenerator;

/**
 * EncryptKeyGenerator 冒烟测试
 * 覆盖全部公开方法：getKeyGenerator / getKeyPairGenerator（含并发切换算法场景）
 *
 * @author sander 2026/10/24
 * @since 2.0.0
 */
public class EncryptKeyGeneratorTest {
    @Test
    public void testGetKeyGenerator() throws Exception {
        KeyGenerator generator = EncryptKeyGenerator.getKeyGenerator("AES");
        Assert.assertEquals("AES", generator.getAlgorithm());
        // 无状态工厂：每次调用返回新实例，避免共享可变生成器跨线程污染
        Assert.assertNotSame(generator, EncryptKeyGenerator.getKeyGenerator("AES"));
        Assert.assertEquals("DES", EncryptKeyGenerator.getKeyGenerator("DES").getAlgorithm());
    }

    @Test
    public void testGetKeyPairGenerator() throws Exception {
        KeyPairGenerator generator = EncryptKeyGenerator.getKeyPairGenerator("RSA");
        Assert.assertEquals("RSA", generator.getAlgorithm());
        Assert.assertNotSame(generator, EncryptKeyGenerator.getKeyPairGenerator("RSA"));
        Assert.assertEquals("DSA", EncryptKeyGenerator.getKeyPairGenerator("DSA").getAlgorithm());
    }

    /**
     * 并发切换算法回归：此前静态单例缓存的 check-then-act 竞态会导致
     * 并发切换算法时拿到错误算法的生成器；现每次调用返回新实例，必须全部正确。
      *
      * @throws Exception 执行过程中出现异常
     */
    @Test
    public void testConcurrentAlgorithmSwitching() throws Exception {
        String[] algorithms = {"AES", "DES", "HmacSHA256"};
        int threads = 12;
        int iterations = 200;
        List<Throwable> errors = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            final String expected = algorithms[t % algorithms.length];
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < iterations; i++) {
                        KeyGenerator generator = EncryptKeyGenerator.getKeyGenerator(expected);
                        if (!expected.equals(generator.getAlgorithm())) {
                            synchronized (errors) {
                                errors.add(new AssertionError("期望 " + expected + " 实际 " + generator.getAlgorithm(
                                        )));
                            }
                            break;
                        }
                    }
                } catch (Throwable e) {
                    synchronized (errors) {
                        errors.add(e);
                    }
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        Assert.assertTrue("并发测试未在超时内完成", done.await(60, TimeUnit.SECONDS));
        pool.shutdownNow();

        Assert.assertEquals("并发切换算法不应拿到错误算法的生成器: " + errors, 0, errors.size());
    }
}
