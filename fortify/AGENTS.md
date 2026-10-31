# fortify

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

配套增强模块：Excel 导出、图像压缩与格式处理、阿里云 DDNS、HTTP 请求体复用过滤器、JSON 转换工具。坐标 `io.gitee.sander-o:fortify`。

**零 Spring 依赖**：2.0.0 起移除了 `spring-context`（纯历史残留）与 `tomcat-embed-core`（BodyReader 系列只用 `javax.servlet` API，改以 `jakarta.servlet-api` provided 声明）。

规模：`src/main/java` 共 12 个 Java 文件（其中 9 个类，其余为 `package-info.java`），4 个测试类。

## Key Files

| File | Description |
|------|-------------|
| `pom.xml` | 依赖 bedrock、fastjson2、aspectj、byte-buddy、thumbnailator、POI、阿里云 alidns SDK、`jakarta.servlet-api`(provided) |
| `README.md` | 模块说明与示例 |
| `src/main/java/org/tools/fortify/util/file/ExcelUtils.java` | 按 `ExcelSheet`（在 bedrock）生成 xlsx 文件/字节数组 |
| `src/main/java/org/tools/fortify/util/file/ImageUtils.java` | 基于 Thumbnailator 的压缩与格式转换 |
| `src/main/java/org/tools/fortify/util/JSONUtils.java` | fastjson2 包装，含 String↔对象、Map/List 转换 |
| `src/main/java/org/tools/fortify/ddns/AliyunDdnsUtils.java` | 阿里云解析记录读取/更新 |
| `src/main/java/org/tools/fortify/http/req/BodyReaderRequestFilter.java` | 请求体复用 Filter（`implements javax.servlet.Filter`） |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `ddns/` | `AliyunDdnsUtils`；`ddns/aliyun/` 为 `AliyunDdnsConfig`、`Record` 实体 |
| `http/req/` | `BodyReaderRequestFilter` + `BodyReaderRequestWrapper`（请求体缓存复用） |
| `util/` | `JSONUtils`、`PropertyIntrospectionException`；`util/file/` `ExcelUtils`、`ImageUtils` |
| `factory/` | 预留，仅有 package-info |

## For AI Agents

### Working In This Directory

- **`JSONUtils` 的公开返回类型是 fastjson2 的 `JSONObject`**，不是 fastjson 1.x 类型。2.0.0 迁移已生效，写示例/文档时不要回退到 `com.alibaba.fastjson`。
- **不要给 fortify 加 Spring 依赖**。HTTP 相关类只用 servlet API；`jakarta.servlet-api` 为 `provided`，运行期由消费方容器提供。
- 阿里云 SDK 的 `com.aliyun:tea` 版本已在根 POM 固定为 `1.4.2`（上游 POM 写的是版本范围 `[1.0.3, 2.0.0)`，会反复拉取 metadata 并命中快照）。不要改成范围。
- 阿里云 SDK 依赖闭包在 `local-repo/`（file:// 仓库）。移除该仓库或其中的 jar 会让受限环境构建失败。
- 阿里云 SDK 传递的旧 BouncyCastle 1.65 已被 exclusion 排除，统一由 `bcprov-jdk18on:1.78.1` 提供同包名类。改 pom 时保留该 exclusion。
- `ExcelSheet` 在 **bedrock** 模块，不在 fortify；改 Excel 相关数据结构时去 bedrock 改。
- `ddns/aliyun/Record` 的字段是 `public` 且命名保留阿里云 API 原样（`RR`、`TTL`、`recordId`），不要改成 Java 惯例命名——它直接映射 API 返回。
- `ImageUtils` 依赖 `java.awt`，无头环境需 `-Djava.awt.headless=true`。

### Testing Requirements

```bash
mvn -B -DskipTests=false -pl fortify -am test
```

JUnit 4。fortify 无 `src/test/resources`——`ImageUtilsTest` 在测试内用 `ImageIO.write` 动态生成图像字节，`ExcelUtilsTest` 写到临时文件；`AliyunDdnsUtilsTest` 涉及外部服务，注意不要依赖真实 AccessKey 做断言。

### Common Patterns

- 工具类私有构造器 + 静态方法（`ExcelUtils`、`ImageUtils`、`AliyunDdnsUtils`、`JSONUtils`）。
- 过滤器按 servlet 规范实现，包装器继承 `HttpServletRequestWrapper` 缓存 body 以支持重复读取。
- 日志统一 `@Slf4j`；**日志/异常消息用英文**（遵循外部编码规范扫描规则），Javadoc 与普通注释保持中文。

## Dependencies

### Internal

`bedrock`（`BaseUtils`、`ExcelSheet`、`Constants`、`ClassUtils`、`DateUtils`）。

### External

| 依赖 | 版本 | 用途 |
|---|---|---|
| fastjson2 | 2.0.53 | JSON（fastjson 1.x 已 EOL） |
| Apache POI `poi` / `poi-ooxml` | 5.2.5 | Excel |
| Thumbnailator | 0.4.8 | 图像压缩 |
| aspectjrt / aspectjweaver | 1.9.4 / 1.9.7 | 切面支持 |
| byte-buddy | 1.14.9 | 字节码 |
| alidns20150109 | 2.0.1（就地声明） | 阿里云 DNS |
| jakarta.servlet-api | provided（Boot BOM 管理） | BodyReader Filter |

## Manual Notes
