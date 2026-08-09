

# tools - 便携工具箱

让编码更易！本项目提供经过验证、实用性强的工具类代码，解决网上搜索查找费时费力、难以找到真正可用代码的问题。

## 📋 目录

- [项目介绍](#项目介绍)
- [软件架构](#软件架构)
- [功能特性](#功能特性)
- [安装教程](#安装教程)
- [使用说明](#使用说明)
- [模块详解](#模块详解)
- [参与贡献](#参与贡献)
- [许可证](#许可证)

## 项目介绍

**tools** 是一个综合性的 Java 工具箱项目，致力于为开发者提供经过生产环境验证的实用工具类。项目核心理念是解决以下问题：
- 网上搜索代码费时费力
- 难以找到真正可用的代码
- 复制的代码存在各种问题

## 软件架构

```
tools
├── bedrock           # 基础工具模块
│   ├── annotations   # 自定义注解
│   ├── base          # 基础对象和枚举
│   ├── constant      # 常量定义
│   ├── database      # 数据库工具
│   ├── exception     # 异常处理
│   ├── http          # HTTP相关工具
│   ├── uniqueid      # 分布式ID生成
│   └── util          # 通用工具类
├── encrypt           # 加密与安全模块
│   ├── asymmetric    # 非对称加密（RSA、DSA等）
│   ├── basic         # 基础加密（MD5、SHA、JWT等）
│   ├── digitalsignature # 数字签名
│   └── symmetrical   # 对称加密（AES、DES、IDEA等）
├── fortify           # 增强功能模块
│   ├── ddns          # 动态域名服务
│   ├── http          # HTTP请求处理
│   └── util          # 增强工具类
└── tools-spring-boot-starter
    └── encrypt-spring-boot-starter  # Spring Boot集成
```

## 功能特性

### bedrock 模块
- **基础对象**：BaseBean、Identity、Address 等常用实体类
- **常量定义**：数字、字符、特殊符号常量
- **数据库工具**：MySQL、Oracle、SQL Server 数据库信息获取
- **HTTP工具**：HttpResult 统一响应、Paging 分页组件
- **ID生成**：UUID、雪花算法、TDDL分布式ID
- **工具类**：集合、映射、日期、文件、图像处理等

### encrypt 模块
- **不可逆加密**：MD5、SHA 系列
- **对称加密**：AES、DES、IDEA
- **非对称加密**：RSA、DSA、ECDSA
- **数字签名**：支持多种签名算法
- **JWT支持**：JSON Web Token 生成与解析
- **CRC校验**：CRC32 校验码生成

### fortify 模块
- **DDNS**：阿里云动态域名服务
- **HTTP请求**：请求体读取过滤器
- **JSON工具**：JSON 与对象互转
- **Excel工具**：Excel 文件生成
- **图像处理**：图片压缩、格式转换

## 安装教程

### Maven 依赖配置

在 `pom.xml` 中添加依赖：

```xml
<properties>
    <tools.version>Latest Version</tools.version>
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
    <!-- 基础工具 -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>bedrock</artifactId>
    </dependency>
    <!-- 加密工具 -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>encrypt</artifactId>
    </dependency>
    <!-- 增强功能 -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>fortify</artifactId>
    </dependency>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>io.gitee.sander-o.spring.boot</groupId>
        <artifactId>encrypt-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

## 使用说明

### bedrock 使用示例

```java
// 集合工具
List<String> list = Lists.newArrayList("a", "b", "c");
boolean empty = Lists.isEmpty(list);

// 映射工具
Map<String, Object> map = Maps.objToMap(user);
User user = Maps.mapToObj(map, User.class);

// 日期工具
String formatted = DateUtils.format(new Date(), "yyyy-MM-dd");
Date parsed = DateUtils.strDateFormat("2024-01-01", "yyyy-MM-dd");

// 文件工具
String content = FileUtil.readFile("path/to/file.txt");
FileUtil.compressToZip("sourcePath", "targetPath.zip");

// 图像相似度检测
SimilarityResult result = ImageUtil.calculateFusedSimilarity(image1, image2);
```

### encrypt 使用示例

```java
// MD5 加密
String md5 = MD5Utils.encode("password");

// Base64 编码
String encoded = Base64Utils.encode("Hello World");
String decoded = Base64Utils.decode(encoded);

// AES 加密
String encrypted = AesHelper.encode("secretKey", "plaintext");
String decrypted = AesHelper.decode("secretKey", encrypted);

// RSA 加密
String publicKey = RsaKeyGenerator.getPublicKeyStr();
String privateKey = RsaKeyGenerator.getPrivateKeyStr();
String sign = RsaHelper.sign(wrought);
boolean verified = RsaHelper.verify(wrought, sign);

// JWT
Map<String, String> payload = new HashMap<>();
payload.put("userId", "123");
String token = JWTUtils.generatorToken(payload);
Map<String, String> parsed = JWTUtils.parseToken(token);
```

### fortify 使用示例

```java
// Excel 导出
ExcelSheet sheet = ExcelSheet.builder()
    .name("用户列表")
    .headers(Arrays.asList(Arrays.asList("姓名", "年龄")))
    .contents(Arrays.asList(Arrays.asList("张三", "25")))
    .build();
ExcelUtils.toFile("output.xlsx", sheet);

// 图像压缩
byte[] compressed = ImageUtils.compressPicByScale(imageBytes, 0.8f);

// 阿里云 DDNS
Record record = new Record();
record.setRR("www");
record.setValue("192.168.1.1");
AliyunDdnsUtils.updateDomainRecord(config, record);
```

### Spring Boot 集成

```java
@SpringBootApplication
@EnableToolsEncrypt
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@RestController
@RequestMapping("/api/encrypt")
public class EncryptController {
    
    @Autowired
    private Encrypt encrypt;
    
    @PostMapping("/aes/encode")
    public String aesEncode(@RequestParam("data") String data) throws AesException {
        return AesHelper.encode(data);
    }
}
```

配置文件 `application.yml`：

```yaml
tools:
  encrypt:
    symmetrical:
      aes:
        enabled: true
        algorithm: AES
        keyBits: 256
      des:
        enabled: true
    asymmetric:
      rsa:
        enabled: true
        algorithm: RSA
        keyBits: 2048
```

## 模块详解

### bedrock 模块

| 包名 | 功能说明 |
|------|----------|
| `org.tools.bedrock.annotations` | 自定义注解 |
| `org.tools.bedrock.base` | 基础实体类和枚举 |
| `org.tools.bedrock.constant` | 常量定义 |
| `org.tools.bedrock.database` | 数据库信息获取 |
| `org.tools.bedrock.exception` | 统一异常处理 |
| `org.tools.bedrock.http` | HTTP响应和分页 |
| `org.tools.bedrock.uniqueid` | ID生成器 |
| `org.tools.bedrock.util` | 核心工具类 |

### encrypt 模块

| 包名 | 功能说明 |
|------|----------|
| `org.tools.encrypt.basic` | MD5、SHA、JWT、Base64 |
| `org.tools.encrypt.symmetrical` | AES、DES、IDEA |
| `org.tools.encrypt.asymmetric` | RSA、DSA |
| `org.tools.encrypt.digitalsignature` | 数字签名 |

### fortify 模块

| 包名 | 功能说明 |
|------|----------|
| `org.tools.fortify.ddns` | 阿里云DDNS |
| `org.tools.fortify.http` | HTTP请求处理 |
| `org.tools.fortify.util` | JSON、Excel、图像 |

## 参与贡献

1. Fork 本仓库
2. 新建 `feat/${user_name}/${需求}` 分支
3. 提交代码
4. 创建 Pull Request 合并到 `develop${年}${月}${日}` 分支
5. 测试通过后创建 Pull Request 合并到 `release`

## 许可证

本项目采用 MIT 许可证开源。使用者行为与本项目无关，作者不对使用者行为造成的后果承担法律责任。

## 技术支持

- 项目地址：https://gitee.com/sander-o/tools
- 欢迎通过 Issue 提出建议或优化点