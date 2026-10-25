# tools — 以 encrypt 为旗舰的 Java 加密工具库

一个经过验证、可按需引入的 Java 加密库（含配套工具）：对称 / 非对称加密、摘要、签名、JWT 开箱即用，也可通过 Spring Boot Starter 一键接入；基础工具（bedrock）与增强能力（fortify）作为配套模块按需引入，不会被强加进你的依赖树。

> **文档导航**：[📖 操作文档（HTML 版）](#操作文档html)（能力矩阵、安全边界、完整示例，可在浏览器中直接查阅） · [📚 在线 API 文档](https://apidoc.gitee.com/sander-o/tools)（Gitee 自动生成的 Javadoc） · [encrypt 模块文档](encrypt/README.md) · [encrypt-spring-boot-starter 5 分钟接入](tools-spring-boot-starter/encrypt-spring-boot-starter/README.md) · [安全策略](SECURITY.md) · [变更记录](CHANGELOG.md) · [English](README.en.md)

## 📖 操作文档（HTML）

一份自包含的操作文档（无外部依赖，含目录导航、能力矩阵、安全边界表、各能力可复制示例、Spring Boot 接入指引、FAQ）：**[docs/operations-guide.html](docs/operations-guide.html)**。支持三种查阅方式：

| 方式 | 操作 |
|---|---|
| 克隆仓库后本地打开 | `git clone https://gitee.com/sander-o/tools.git` 后，用浏览器打开 **`docs/operations-guide.html`** |
| Gitee 网页端 | 进入 `docs/operations-guide.html` 页面，点击"原始数据/RAW"下载后用浏览器打开 |
| 离线分发 | 文件自包含单文件，可直接下载、邮件分发或打印（已适配打印样式） |

## 快速开始

### 1. 引入 encrypt

```xml
<dependency>
    <groupId>io.gitee.sander-o</groupId>
    <artifactId>encrypt</artifactId>
    <version>1.0.6.7-d01</version>
</dependency>
```

> 版本号读取自项目根 `pom.xml`（当前为 `1.0.6.7-d01`，属于开发中版本，发布时可能变化；以 [Maven Central](https://central.sonatype.com/artifact/io.gitee.sander-o/tools) 上实际可用的版本为准）。

### 2. 三行代码开始使用

```java
// 摘要：MD5（大写十六进制输出）
String md5 = MD5Utils.encode("password");
```

更稳妥的最小示例（完整 round-trip）：

```java
// 生成 128 位随机密钥（Hex 字符串），配置 IV 后加解密
SecretKey key = AesKeyGenerator.getSecretKey();
AesProperties props = new AesProperties();
props.setSecretKey(key);
props.setIvParameter("0123456789abcdef");
AesHelper.load(props);

String ciphertext = AesHelper.encode("hello");
String plaintext  = AesHelper.decode(ciphertext); // "hello"
```

### 3. Spring Boot 一键接入

```xml
<dependency>
    <groupId>io.gitee.sander-o.spring.boot</groupId>
    <artifactId>encrypt-spring-boot-starter</artifactId>
    <version>1.0.6.7-d01</version>
</dependency>
```

启动类加 `@EnableToolsEncrypt`，在 `application.yml` 中配置 `tools.encrypt.*` 即可，详见 [5 分钟接入](tools-spring-boot-starter/encrypt-spring-boot-starter/README.md)。

## 为什么是 encrypt

- **有测试兜底**：核心加解密路径有单元测试覆盖（运行方式见 [encrypt 模块文档](encrypt/README.md#测试)）。
- **边界明确**：每个能力的算法 / 模式 / 填充 / 密钥 / IV / 字符集行为都有文档约束，见 [安全边界表](encrypt/README.md#安全边界)。
- **可按需引入**：只要加密，就只引 `encrypt`；要 Spring Boot 自动装配，加 `encrypt-spring-boot-starter`；`bedrock`、`fortify` 是可选的配套模块，不是前置依赖（仅 `encrypt` 内部依赖 `bedrock` 的少量基础工具，随其自动传递）。
- **算法覆盖常见场景**：AES / DES / IDEA、RSA、DSA、MD5 / SHA 系列、数字签名、JWT、Base64、CRC32。

## 项目结构

```
tools
├── encrypt           # ★ 旗舰模块：加密与安全（本项目的第一入口）
│   ├── asymmetric    # 非对称加密（RSA 等）
│   ├── basic         # 摘要与编码（MD5、SHA、JWT、Base64、CRC32）
│   ├── digitalsignature # 数字签名（DSA 等）
│   └── symmetrical   # 对称加密（AES、DES、IDEA 等）
├── tools-spring-boot-starter
│   └── encrypt-spring-boot-starter  # Spring Boot 自动装配
├── bedrock           # 配套模块：基础工具
└── fortify           # 配套模块：增强能力
```

## 配套模块

以下模块围绕加密 / 基础场景提供扩展能力，按需引入，完整说明见各自文档：

### bedrock — 基础工具

坐标：`io.gitee.sander-o:bedrock`（版本同根 `pom.xml`）。包含集合 / 映射 / 日期 / 文件 / 图像等工具类、HTTP 统一响应、雪花 ID 等分布式 ID 生成。`encrypt` 依赖其中的基础工具类，单独使用 `encrypt` 时会自动传递引入。详见 [bedrock/README.md](bedrock/README.md)。

### fortify — 增强能力

坐标：`io.gitee.sander-o:fortify`（版本同根 `pom.xml`）。包含 Excel 文件生成（`ExcelUtils` + `ExcelSheet`）、图像压缩处理（`ImageUtils`）、阿里云 DDNS（`AliyunDdnsUtils`）等。详见 [fortify/README.md](fortify/README.md)。

## JDK 版本支持

所有模块统一以 **Java 8 字节码**（class major 52）发布，最低运行环境 **JDK 8**；在 JDK 8 / 17 / 21 三个版本上完成构建与全量单元测试验证（CI 矩阵）。

| 模块 | 字节码目标 | 验证过的 JDK | 说明 |
|---|---|---|---|
| bedrock | Java 8 | 8 / 17 / 21 | 纯 JDK 实现，无版本敏感依赖 |
| encrypt | Java 8 | 8 / 17 / 21 | BouncyCastle 1.78.1（multi-release jar，官方支持 8~21） |
| fortify | Java 8 | 8 / 17 / 21 | fastjson2 / 阿里云 SDK / POI 均兼容 17/21 |
| encrypt-spring-boot-starter | Java 8 | 8 / 17 / 21 | Spring Boot 2.7.18 + Spring 5.3.39（5.3.30 起官方支持 JDK 21） |

构建侧的多版本保护：在 JDK 9+ 上构建时会自动启用交叉编译（`jdk9-cross-compile` profile，`maven.compiler.release=8`），静态禁止误用 JDK 9+ API，保证任何环境构建出的产物都可运行在 Java 8；JDK 8 上构建则维持 `source/target=1.8` 既有路径。验证命令与 CI 一致：

```bash
mvn -B -DskipTests=false test
```

## 安装教程（全量引入）

> **本地构建与镜像加速**：项目 POM 不声明远程仓库（避免把区域镜像强加给下游使用方，也让 CI / 在线文档构建环境按自身网络解析依赖）；唯一例外是 `local-repo/`——com.aliyun SDK 依赖闭包以标准 Maven 仓库布局随仓库分发（file:// 直读，供受限构建环境使用），常规环境无感知。本地构建如需镜像加速，请在**你自己的** `~/.m2/settings.xml` 中配置 `<mirrors>`，例如：
>
> ```xml
> <mirrors>
>     <mirror>
>         <id>aliyunmaven</id>
>         <mirrorOf>central</mirrorOf>
>         <name>aliyun public</name>
>         <url>https://maven.aliyun.com/repository/public</url>
>     </mirror>
> </mirrors>
> ```

如需一次性引入全部模块，可在自己的 `dependencyManagement` 中导入 BOM 后按需声明依赖：

```xml
<properties>
    <!-- 以 Maven Central 实际发布的版本为准 -->
    <tools.version>1.0.6.7-d01</tools.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.gitee.sander-o</groupId>
            <artifactId>tools</artifactId>
            <version>${tools.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- 加密工具（旗舰） -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>encrypt</artifactId>
    </dependency>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>io.gitee.sander-o.spring.boot</groupId>
        <artifactId>encrypt-spring-boot-starter</artifactId>
    </dependency>
    <!-- 基础工具（配套，按需） -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>bedrock</artifactId>
    </dependency>
    <!-- 增强功能（配套，按需） -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>fortify</artifactId>
    </dependency>
</dependencies>
```

## 使用示例

### encrypt（详见 [encrypt/README.md](encrypt/README.md)）

```java
// MD5 / SHA 摘要
String md5 = MD5Utils.encode("password");                       // 大写十六进制；入参为 null 时抛 EncryptException
String sha256 = ShaUtils.encode("password");                    // 默认 SHA-256，小写十六进制

// Base64（UTF-8）
String encoded = Base64Utils.encode("Hello World");
String decoded = Base64Utils.decode(encoded);

// AES（默认 AES/CBC/PKCS5Padding；密钥为 Hex 字符串；IV 需 16 字节）
SecretKey key = AesKeyGenerator.getSecretKey();                 // 随机 128 位密钥
AesProperties props = new AesProperties();
props.setSecretKey(key);
props.setIvParameter("0123456789abcdef");
AesHelper.load(props);
String encrypted = AesHelper.encode("plaintext");
String decrypted = AesHelper.decode(encrypted);                 // "plaintext"

// RSA（默认 OAEPWithSHA-256AndMGF1Padding；签名默认 SHA3_256withRSA）
Rsa rsa = new Rsa();                                            // 默认 2048 位
rsa.setPublicKey("...Base64 公钥...");
rsa.setPrivateKey("...Base64 私钥...");
RsaHelper.load(rsa);
String cipherText = RsaHelper.encode("plaintext");
String plainText = RsaHelper.decode(cipherText);
String sign = RsaHelper.sign("plaintext");
boolean ok = RsaHelper.verify("plaintext", sign);

// JWT（HMAC256；建议始终显式传入签名密钥，避免使用内置默认值）
Map<String, String> payload = new HashMap<>();
payload.put("userId", "123");
String token = JWTUtils.generatorToken(payload, "your-strong-secret");
Map<String, String> parsed = JWTUtils.parseToken(token, "your-strong-secret");
```

> 注意：`AesHelper` / `RsaHelper` 等基于静态配置（`load`）工作，进程内共享一份配置；密钥明文请勿硬编码在源码中，密钥来源建议见 [starter 文档](tools-spring-boot-starter/encrypt-spring-boot-starter/README.md)。

### bedrock（配套）

```java
// 集合 / 映射工具（不含构造器，构造 List 请使用 JDK API）
List<String> list = Arrays.asList("a", "b", "c");
boolean empty = Lists.isEmpty(list);

Map<String, Object> map = Maps.objToMap(user);
User user2 = Maps.mapToObj(map, new User());

// 日期工具
String formatted = DateUtils.format(new Date(), "yyyy-MM-dd");
Date parsed = DateUtils.strDateFormat("2024-01-01", "yyyy-MM-dd");
```

### fortify（配套）

```java
// Excel 导出（ExcelSheet 位于 bedrock 模块，支持 builder 风格）
ExcelSheet sheet = ExcelSheet.builder()
    .name("用户列表")
    .headers(Arrays.asList(Arrays.asList("姓名", "年龄")))
    .contents(Arrays.asList(Arrays.asList("张三", "25")))
    .build();
ExcelUtils.toFile("output.xlsx", sheet);

// 阿里云 DDNS
List<Record> records = AliyunDdnsUtils.readDomainRecords(config, "example.com", "A");
```

## 参与 / 支持 / 许可

- 问题与建议请提交 [Gitee Issue](https://gitee.com/sander-o/tools/issues)；安全问题请勿公开提交，见 [SECURITY.md](SECURITY.md)。
- 项目使用 [MIT](LICENSE) 许可证。
- 技术支持：可通过 Gitee Issue 与维护者联系。

<!-- TODO(badge): 待确认可用的 CI / 版本徽章后再补充，不使用未经确认的徽章 URL。 -->
