package org.tools.bedrock.base;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

/**
 * BaseBean / serializable.BaseBean 冒烟测试：
 * 审计字段重命名（creator/updater/createdAt/updatedAt）后的访问器与 JDK 序列化 round-trip。
 *
 * @author sander 2026/10/25
 */
public class BaseBeanSmokeTest {

    /** base.BaseBean：全参构造、重命名后的访问器、toString 覆盖 remark（回归：手写 toString 丢字段缺陷） */
    @Test
    public void baseBeanAccessorsAndToString() {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis() + 1000L);
        // 字段声明顺序：id, pid, bid, sfid, uuid, sort, state, remark, creator, createdAt, updater, updatedAt
        BaseBean bean = new BaseBean(1L, 0L, "b-1", "sf-1", "uuid-1", 1L, (byte) 1,
                "备注", "sander", createdAt, "sander", updatedAt);

        Assert.assertEquals("sander", bean.getCreator());
        Assert.assertEquals(createdAt, bean.getCreatedAt());
        Assert.assertEquals("sander", bean.getUpdater());
        Assert.assertEquals(updatedAt, bean.getUpdatedAt());
        Assert.assertEquals("备注", bean.getRemark());

        String str = bean.toString();
        Assert.assertTrue("toString 应包含 remark 字段（回归手写 toString 丢字段缺陷）", str.contains("remark=备注"));
        Assert.assertTrue("toString 应包含重命名后的 creator 字段", str.contains("creator=sander"));
        Assert.assertTrue("toString 应包含重命名后的 createdAt 字段", str.contains("createdAt="));
    }

    /** serializable.BaseBean：JDK 序列化 round-trip，审计字段（含 Timestamp 纳秒）完整保留 */
    @Test
    public void serializableBaseBeanRoundTrip() throws Exception {
        Timestamp createdAt = Timestamp.valueOf("2026-10-25 10:00:00.123456789");
        Timestamp updatedAt = Timestamp.valueOf("2026-10-25 12:30:00.987654321");
        org.tools.bedrock.base.serializable.BaseBean bean =
                new org.tools.bedrock.base.serializable.BaseBean(
                        1L, 0L, "b-1", "sf-1", "uuid-1", 1L, (byte) 1,
                        "备注", "creator-a", createdAt, "updater-b", updatedAt);

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(bean);
            bytes = bos.toByteArray();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            org.tools.bedrock.base.serializable.BaseBean back =
                    (org.tools.bedrock.base.serializable.BaseBean) ois.readObject();
            Assert.assertEquals("creator-a", back.getCreator());
            Assert.assertEquals("updater-b", back.getUpdater());
            Assert.assertEquals(createdAt, back.getCreatedAt());
            Assert.assertEquals(updatedAt, back.getUpdatedAt());
            Assert.assertEquals(123456789, back.getCreatedAt().getNanos());
            Assert.assertEquals("备注", back.getRemark());
        }
    }

}
