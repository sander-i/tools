# fortify — 增强能力（配套模块）

> 本模块是 tools 项目的**配套模块**。旗舰能力请查看 [encrypt 模块](../encrypt/README.md)。

围绕常见业务场景的增强能力：Excel 文件生成（`ExcelUtils` + `ExcelSheet`，`ExcelSheet` 位于 bedrock 模块）、图像压缩与格式处理（`ImageUtils`）、阿里云 DDNS（`AliyunDdnsUtils`）、HTTP 请求体读取过滤器、JSON 转换工具。

```xml
<dependency>
    <groupId>io.gitee.sander-o</groupId>
    <artifactId>fortify</artifactId>
    <version>1.0.6.7-d01</version> <!-- 以根 pom.xml / Maven Central 为准 -->
</dependency>
```

## 示例

```java
// Excel 导出
ExcelSheet sheet = ExcelSheet.builder()
    .name("用户列表")
    .headers(Arrays.asList(Arrays.asList("姓名", "年龄")))
    .contents(Arrays.asList(Arrays.asList("张三", "25")))
    .build();
ExcelUtils.toFile("output.xlsx", sheet);

// 阿里云 DDNS：读取并更新解析记录
List<Record> records = AliyunDdnsUtils.readDomainRecords(config, "example.com", "A");
AliyunDdnsUtils.updateDomainRecord(config, newRecord);
```

更完整的能力清单见根 [README](../README.md)。
