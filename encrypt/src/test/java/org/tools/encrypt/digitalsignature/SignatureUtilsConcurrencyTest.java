package org.tools.encrypt.digitalsignature;

import org.junit.Assert;
import org.junit.Test;
import org.tools.encrypt.asymmetric.rsa.RsaKeyGenerator;
import org.tools.encrypt.exception.asymmetric.rsa.RsaException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SignatureUtils 并发安全冒烟测试。
 *
 * <p>此前 sign/verify 共享静态 Signature 实例，多线程并发时会互相污染
 * （initSign/initVerify/update 交错导致签名错误或验签失败）。
 * 现每次调用使用独立实例，本用例在多线程混合两种签名算法的场景下验证正确性。</p>
 *
 * @author sander 2026/10/24
 */
public class SignatureUtilsConcurrencyTest {

    private static final String PLAINTEXT = "concurrent signature test";
    private static final int THREADS = 8;
    private static final int ITERATIONS = 40;

    @Test
    public void concurrentSignVerifyWithMixedAlgorithms() throws Exception {
        // 2048 位密钥对（Base64 X.509 / PKCS#8）
        java.security.KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        List<Throwable> errors = new ArrayList<>();
        CountDownLatch done = new CountDownLatch(THREADS);

        for (int t = 0; t < THREADS; t++) {
            // 偶数线程用 SHA256withRSA，奇数线程用 MD5withRSA，验证算法切换不再互相污染
            final String algorithm = (t % 2 == 0) ? "SHA256withRSA" : "MD5withRSA";
            pool.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < ITERATIONS; i++) {
                        String sign = SignatureUtils.sign(algorithm, privateKey, PLAINTEXT);
                        boolean pass = SignatureUtils.verify(algorithm, publicKey, PLAINTEXT, sign);
                        if (!pass) {
                            synchronized (errors) {
                                errors.add(new AssertionError(algorithm + " verify failed at iteration " + i));
                            }
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

        Assert.assertEquals("并发签名/验签不应有失败: " + errors, 0, errors.size());
    }

    @Test
    public void signThenVerifySingleThreadStillWorks() throws RsaException {
        java.security.KeyPair keyPair = RsaKeyGenerator.getKeyPair();
        String sign = SignatureUtils.sign("SHA256withRSA", keyPair.getPrivate(), PLAINTEXT);
        Assert.assertTrue(SignatureUtils.verify("SHA256withRSA", keyPair.getPublic(), PLAINTEXT, sign));
        Assert.assertFalse(SignatureUtils.verify("SHA256withRSA", keyPair.getPublic(), PLAINTEXT + "x", sign));
    }

}
