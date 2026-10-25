package org.tools.bedrock.http.resp;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * R / RR 序列化冒烟测试（JDK ObjectOutputStream round-trip）
 *
 * @author sander 2026/10/24
 */
public class RSerializationSmokeTest {

    @Test
    public void rRoundTrip() throws Exception {
        R<String> r = R.ok("data-value");

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(r);
            bytes = bos.toByteArray();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            @SuppressWarnings("unchecked")
            R<String> back = (R<String>) ois.readObject();
            Assert.assertTrue(back.isSuccess());
            Assert.assertEquals("data-value", back.getData());
            Assert.assertEquals(RS.OK.getCode(), back.getCode());
            Assert.assertEquals("成功", back.getMessage());
            Assert.assertTrue("timestamp 应在构造时自动初始化", back.getTimestamp() > 0);
        }
    }

    @Test
    public void rGenericDataRoundTrip() throws Exception {
        java.util.HashMap<String, String> data = new java.util.HashMap<>();
        data.put("k", "v");
        R<java.util.HashMap<String, String>> r = R.ok(data);

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(r);
            bytes = bos.toByteArray();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            @SuppressWarnings("unchecked")
            R<java.util.HashMap<String, String>> back = (R<java.util.HashMap<String, String>>) ois.readObject();
            Assert.assertEquals("v", back.getData().get("k"));
        }
    }

    @Test
    public void rrFailRoundTrip() throws Exception {
        RR fail = RR.fail();
        Assert.assertFalse("fail() 的 success 应为 false", fail.isSuccess());

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(fail);
            bytes = bos.toByteArray();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            RR back = (RR) ois.readObject();
            Assert.assertFalse(back.isSuccess());
            Assert.assertEquals(RS.FAIL.getCode(), back.getCode());
        }
    }

    @Test
    public void messageMerge() {
        RR custom = R.build(RS.ARGUMENT_ILLEGAL, "参数 userId 非法");
        Assert.assertFalse(custom.isSuccess());
        Assert.assertEquals("参数 userId 非法", custom.getMessage());

        RR fallback = R.build(RS.ARGUMENT_ILLEGAL, null);
        Assert.assertEquals(RS.ARGUMENT_ILLEGAL.getMsg(), fallback.getMessage());
    }

    @Test
    public void httpResultRoundTrip() throws Exception {
        org.tools.bedrock.http.HttpResult<String> result = org.tools.bedrock.http.HttpResult.success("payload");

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(result);
            bytes = bos.toByteArray();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            @SuppressWarnings("unchecked")
            org.tools.bedrock.http.HttpResult<String> back = (org.tools.bedrock.http.HttpResult<String>) ois.readObject();
            Assert.assertEquals("payload", back.getData());
        }
    }

}
