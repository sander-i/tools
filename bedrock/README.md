# bedrock — 基础工具（配套模块）

> 本模块是 tools 项目的**配套模块**。旗舰能力请查看 [encrypt 模块](../encrypt/README.md)。

为 Java 应用提供基础工具：集合 / 映射 / 日期 / 文件 / 图像工具类、HTTP 统一响应（`HttpResult`）与分页（`Paging`）、分布式 ID（UUID / 雪花 / TDDL）、常用实体与常量。

[encrypt 模块](../encrypt/README.md) 依赖本模块中的少量基础工具类，单独引入 `encrypt` 时会自动传递引入；如需独立使用基础工具，可显式声明：

```xml
<dependency>
    <groupId>io.gitee.sander-o</groupId>
    <artifactId>bedrock</artifactId>
    <version>2.1.0</version> <!-- 以根 pom.xml / Maven Central 为准 -->
</dependency>
```

## 示例

```java
// 集合 / 映射工具（只做判空与转换，不含构造器）
boolean empty = Lists.isEmpty(list);
Map<String, Object> map = Maps.objToMap(user);
User user2 = Maps.mapToObj(map, new User());

// 日期
String formatted = DateUtils.format(new Date(), "yyyy-MM-dd");
Date parsed = DateUtils.strDateFormat("2024-01-01", "yyyy-MM-dd");
```

更完整的能力清单见根 [README](../README.md)。
