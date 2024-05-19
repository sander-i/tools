package org.tools.bedrock.util.network;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author sander
 * @version 1.0
 * @date 2023/10/23 21:02
 * @description 网络工具
 * @scope all
 */
@Slf4j
public class NetworkUtils {

    private NetworkUtils() {
    }

    /**
     * 通过域名获取ipv4地址
     *
     * @param domain 域名
     * @return ipv4
     * @throws UnknownHostException 未知主机异常
     */
    public static String getIpv4ByDomain(String domain) throws UnknownHostException {
        return InetAddress.getByName(domain).getHostAddress();
    }

    /**
     * 通过ip获得域名
     *
     * @param ipv4 ip
     * @return 域名
     * @throws UnknownHostException 未知主机异常
     */
    public static String getDomainByIpv4(String ipv4) throws UnknownHostException {
        String[] ipStr = ipv4.split("[.]");
        byte[] ipBytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            int m = Integer.parseInt(ipStr[i]);
            ipBytes[i] = (byte) (m & 0xff);
        }
        InetAddress inetAddressByIp = InetAddress.getByAddress(ipBytes);
        return inetAddressByIp.getCanonicalHostName();
    }

    /**
     * 获取本机ipv6地址
     *
     * @return ipv6
     * @throws SocketException 网络连接异常
     */
    public static String getLocalIpv6() throws SocketException {
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface network = enumeration.nextElement();
            if (!network.isVirtual() && network.isUp()) {
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if ((address instanceof Inet4Address || address instanceof Inet6Address)) {
                        if (address.getHostAddress().startsWith("2409") || address.getHostAddress().startsWith("2408") || address.getHostAddress().startsWith("240e") || address.getHostAddress().startsWith("240a")) {
                            return address.getHostAddress().substring(0, address.getHostAddress().lastIndexOf("%"));
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 获取外网ipv4
     *
     * @return ipv4
     */
    public static String getExtranetIpv4() {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL realUrl = new URL("https://www.taobao.com/help/getip.php");
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("获取ipv4公网地址异常", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                log.error("获取ipv4公网地址异常", e2);
            }
        }
        String str = result.toString().replace("ipCallback({ip:", "");
        String ipStr = str.replace("})", "");

        return ipStr.replace('"', ' ');
    }

}
