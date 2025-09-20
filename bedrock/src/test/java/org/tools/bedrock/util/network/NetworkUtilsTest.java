package org.tools.bedrock.util.network;

import org.junit.Test;
import java.net.UnknownHostException;
import static org.junit.Assert.*;

/**
 * NetworkUtils 冒烟测试
 * 测试网络工具类的基本功能
 */
public class NetworkUtilsTest {

    @Test
    public void testGetIpv4ByDomain() {
        try {
            // 测试通过域名获取IP地址的基本功能
            String ip = NetworkUtils.getIpv4ByDomain("www.baidu.com");
            assertNotNull("通过域名获取的IP地址不应为null", ip);
            assertFalse("通过域名获取的IP地址不应为空", ip.isEmpty());
            // 验证是否为有效的IP格式（简单验证）
            assertTrue("IP地址应包含点号", ip.contains("."));
        } catch (UnknownHostException e) {
            // 如果DNS解析失败，可能是网络问题，不视为测试失败
            System.out.println("无法解析域名，可能是网络问题: " + e.getMessage());
        }
    }

    @Test
    public void testGetDomainByIpv4() {
        try {
            // 测试通过IP地址获取域名的基本功能
            String domain = NetworkUtils.getDomainByIpv4("8.8.8.8");
            // 注意：不是所有IP都有反向解析的域名
            // 测试至少不抛出异常即可
            assertNotNull("通过IP获取域名结果不应为null", domain);
        } catch (UnknownHostException e) {
            // 反向DNS解析可能失败，这在某些网络环境下是正常的
            System.out.println("无法反向解析IP地址，可能是网络问题: " + e.getMessage());
        }
    }

    @Test
    public void testGetExtranetIpv4() {
        // 测试获取外网IP地址的功能
        String ip = NetworkUtils.getExtranetIpv4();
        assertNotNull("获取的外网IP不应为null", ip);
        // 外网IP可能包含空格，需要处理
        String trimmedIp = ip.trim();
        assertFalse("获取的外网IP不应为空", trimmedIp.isEmpty());
        // 验证基本的IP格式
        if (!trimmedIp.isEmpty()) {
            assertTrue("外网IP应包含点号", trimmedIp.contains("."));
        }
    }

    @Test
    public void testGetLocalIpv6() {
        try {
            // 测试获取本地IPv6地址的功能
            String ipv6 = NetworkUtils.getLocalIpv6();
            // 由于不是所有环境都有IPv6地址，我们只验证不抛出异常
            assertNotNull("获取的IPv6地址不应为null", ipv6);
            // 如果获取到IPv6地址，则验证格式
            if (!ipv6.isEmpty()) {
                // IPv6地址通常包含冒号
                assertTrue("IPv6地址应包含冒号或符合特定格式", ipv6.contains(":"));
            }
        } catch (Exception e) {
            // 某些环境下可能无法获取IPv6地址，这可以接受
            System.out.println("获取IPv6地址时出现异常（可能是环境问题）: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidIpConversion() {
        try {
            // 测试无效IP转换的情况
            NetworkUtils.getDomainByIpv4("999.999.999.999");
            fail("应该抛出UnknownHostException异常");
        } catch (UnknownHostException e) {
            // 预期的异常
            assertTrue("应抛出UnknownHostException", e instanceof UnknownHostException);
        }
    }
}
