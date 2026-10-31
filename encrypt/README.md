# encrypt — Java 加密工具模块

`encrypt` 是 tools 项目的旗舰模块：为 Java 应用提供开箱即用的加密、摘要、签名、JWT 与编码能力。所有工具类均基于 JDK 标准加密体系（JCA/JCE）实现，仅在必要处（IDEA、RSA-OAEP）引入 BouncyCastle 作为 Provider。

**适用场景**：字段级加解密（手机号、身份证等敏感信息）、接口签名 / 验签、令牌（JWT）签发与解析、数据摘要与校验。

**不适用场景**：全盘磁盘加密、TLS/SSL、需要抗量子攻击的场景。

- 坐标：`io.gitee.sander-o:encrypt`（版本见根 [pom.xml](../pom.xml)，当前 `2.0.0`）
- Spring Boot 用户请直接使用 [encrypt-spring-boot-starter](../tools-spring-boot-starter/encrypt-spring-boot-starter/README.md)
- 安全问题请勿公开提交，见 [SECURITY.md](../SECURITY.md)

## 能力矩阵

| 能力 | 入口类 | 算法 | 状态 |
|---|---|---|---|
| 摘要（不可逆） | `MD5Utils` | MD5，输出大写十六进制 | ✅ 可用 |
| 摘要（不可逆） | `ShaUtils` | SHA-1 / SHA-256 / SHA-384 / SHA-512（默认 SHA-256），小写十六进制 | ✅ 可用 |
| 对称加密 | `AesHelper` / `AesKeyGenerator` | AES（默认 `AES/CBC/PKCS5Padding`） | ✅ 可用 |
| 对称加密 | `DesHelper` / `DesKeyGenerator` | DES（默认 `DES`，即 ECB/PKCS5Padding） | ✅ 可用 |
| 对称加密 | `IdeaHelper` / `IdeaKeyGenerator` / `IdeaUtils` | IDEA（ECB/ISO10126Padding，依赖 BouncyCastle） | ✅ 可用 |
| 对称加密 | `BlowfishHelper` / `BlowfishKeyGenerator` | Blowfish（默认 `Blowfish`，即 ECB/PKCS5Padding；密钥 Hex，默认 128 位；JDK Provider） | ✅ 可用（legacy） |
| 对称加密 | `Des3Helper` / `Des3KeyGenerator`（`symmetrical.des.des3`，即 TDEA） | 3DES（默认 `DESede/CBC/PKCS5Padding`；密钥 Hex，默认 168 位；JDK Provider） | ✅ 可用（legacy） |
| 对称加密 | `Rc2Helper` / `Rc2KeyGenerator` | RC2（默认 `RC2`，即 ECB/PKCS5Padding；密钥 Hex，默认 128 位；JDK Provider） | ✅ 可用（legacy） |
| 对称加密 | `Rc4Helper` / `Rc4KeyGenerator` | RC4 流密码（仅 `RC4` 一种模式，无 IV / 无填充；密钥 Hex，默认 128 位；JDK Provider） | ✅ 可用（legacy，不推荐） |
| 对称加密 | `Rc5Helper` / `Rc5KeyGenerator` | RC5（默认 `RC5`，即 ECB/PKCS5Padding；密钥 Hex，默认 128 位；BouncyCastle Provider） | ✅ 可用（legacy） |
| 对称加密 | `SkipjackHelper` / `SkipjackKeyGenerator` | Skipjack（默认 `SKIPJACK`，即 ECB/PKCS5Padding；密钥 Hex，固定 80 位；BouncyCastle Provider） | ✅ 可用（legacy） |
| 非对称加密 | `RsaHelper` / `RsaKeyGenerator` | RSA（默认 `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`） | ✅ 可用 |
| 密钥协商 | `DhHelper` / `DhKeyGenerator` | DH（默认 `DH`，默认 2048 位；只做密钥协商，不做加解密） | ✅ 可用 |
| 密钥协商 | `EccHelper` / `EccKeyGenerator` | ECDH（默认 `EC`，曲线 `secp256r1`；只做密钥协商，不做加解密） | ✅ 可用 |
| 数字签名 | `RsaHelper.sign/verify`、`SignatureUtils` | 默认 `SHA256withRSA` | ✅ 可用 |
| 数字签名 | `DsaHelper` / `DsaKeyGenerator` | DSA（默认 `SHA256withDSA`） | ✅ 可用 |
| 数字签名 | `EcdsaHelper` / `EcdsaKeyGenerator` | ECDSA（默认 `SHA256withECDSA`，曲线 `secp256r1`） | ✅ 可用 |
| JWT | `JWTUtils` | HS256（HMAC256，基于 java-jwt） | ✅ 可用 |
| 编码 | `Base64Utils` | 标准 Base64（非 URL-Safe），UTF-8 | ✅ 可用 |
| 校验 | `Crc32Utils` | CRC32 | ✅ 可用（线程安全） |
| 非对称 | `asymmetric.elgamal`、`asymmetric.knapsack`、`asymmetric.rabin` | — | ⛔ 仅有包占位，暂无实现，请勿依赖 |

## 安全边界

下表描述代码**当前实际行为**，不是安全承诺。标注 ⚠️ 的组合可用但存在已知风险，标注 TODO 的为尚未核实项。

| 算法 | 模式 / 填充 | 密钥要求 | IV 处理 | 字符集 | 线程安全 | 异常行为 |
|---|---|---|---|---|---|---|
| MD5 | — | — | — | 入参 `String` 固定按 UTF-8 取字节 | ✅ 每次 `encode` 新建 MessageDigest | 空串返回 `""`；`null` 抛 NPE；算法缺失抛 `EncryptException` |
| SHA | — | — | — | 同上（UTF-8） | ✅ | `null` 抛 NPE；未知算法抛 `NoSuchAlgorithmException` |
| AES | 默认 `AES/CBC/PKCS5Padding`；模式可在配置中改 | 密钥必须是 **Hex 字符串**，解码后为 16/24/32 字节（AES-128/192/256）；256 位需 JCE 无限制策略（JDK8u161+ 默认支持） | CBC 模式下 `ivParameter` 按 **UTF-8 取字节后必须为 16 字节**，否则 JCE 抛异常；ECB 模式下 IV 被忽略 | 明文与解密输出均为 UTF-8；密文 Base64 | `load` 与 `encode/decode` 并发不安全；`encode/decode` 之间线程安全（每次新建 Cipher） | 入参校验失败 / JCE 异常统一包装为 `AesException`（异常链保留） |
| DES | 默认 `DES`（= `DES/ECB/PKCS5Padding`）；可选 `DES/ECB/NoPadding`、`DESede/CBC/PKCS5Padding` | 密钥为**原始字符串**，按 UTF-8 取字节，DES 要求恰好 8 字节 | ⚠️ 默认模式即 ECB，`ivParameter` 被忽略且不生效 | 明文与解密输出均为 UTF-8；密文 Base64 | 同 AES | 统一包装为 `DesException` |
| IDEA | `IDEA` 或 `IDEA/ECB/ISO10126Padding`（BC Provider） | 密钥为 **Hex 字符串**（32 个 Hex 字符 = 128 位，与 AES 约定一致；此前 String 重载为 UTF-8 原文，已对齐） | ECB 模式 IV 被忽略 | 明文与解密输出均为 UTF-8；密文 Base64 | 同 AES | 统一包装为 `IdeaException` |
| Blowfish | 默认 `Blowfish`（= ECB/PKCS5Padding）；可选 `Blowfish/CBC/PKCS5Padding` | 密钥为 **Hex 字符串**，解码后 4~56 字节，默认 128 位（JDK Provider） | CBC 下 IV 按 UTF-8 后必须为 8 字节；ECB 忽略 | 同上 | 同 AES | 统一包装为 `BlowfishException`。**legacy，不推荐用于新设计** |
| 3DES（DESede / TDEA） | 仅 `DESede/CBC/PKCS5Padding`（其它模式 `load` 直接拒绝） | 密钥为 **Hex 字符串**，解码后 16 字节（32 Hex）或 24 字节（48 Hex），默认 168 位（JDK Provider）；16 字节自动扩展为 K1‖K2‖K1。**与 DES 的「UTF-8 原始 8 字节字符串」语义不同** | CBC 下 IV 按 UTF-8 后必须为 8 字节 | 同上 | 同 AES | 统一包装为 `Des3Exception`。**legacy，不推荐用于新设计** |
| RC2 | 默认 `RC2`（= ECB/PKCS5Padding）；可选 `RC2/CBC/PKCS5Padding` | 密钥为 **Hex 字符串**，解码后 5~128 字节（40~1024 位，SunJCE 下限 40 位），默认 128 位（JDK Provider） | CBC 下 IV 按 UTF-8 后必须为 8 字节；ECB 忽略 | 同上 | 同 AES | 统一包装为 `Rc2Exception`。ECB 与 CBC 均按密钥长度计算 effective bits。**legacy，不推荐用于新设计** |
| RC4 | 仅 `RC4` 一种模式（流密码） | 密钥为 **Hex 字符串**，默认 128 位（JDK Provider） | **无 IV / 无填充 / 无分组**，密文长度恒等于明文长度 | 同上 | 同 AES | 统一包装为 `Rc4Exception`。**流密码，无 IV/无填充，已不安全的协议遗留** |
| RC5 | 默认 `RC5`（= ECB/PKCS5Padding）；可选 `RC5/CBC/PKCS5Padding`（BC Provider） | 密钥为 **Hex 字符串**，默认 128 位；轮数 / 分组长度 / 字长固定为 12 / 8 字节 / 32 位，调用双方须一致 | CBC 下 IV 按 UTF-8 后必须为 8 字节；ECB 忽略 | 同上 | 同 AES | 统一包装为 `Rc5Exception`。**legacy，不推荐用于新设计** |
| Skipjack | 默认 `SKIPJACK`（= ECB/PKCS5Padding）；可选 `SKIPJACK/CBC/PKCS5Padding`（BC Provider） | 密钥为 **Hex 字符串**，固定 80 位（10 字节） | CBC 下 IV 按 UTF-8 后必须为 8 字节；ECB 忽略 | 同上 | 同 AES | 统一包装为 `SkipjackException`。**legacy，不推荐用于新设计** |
| RSA 加解密 | 默认 `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`（JDK SunJCE）；仅无 `RSA/ECB/` 前缀的 `OAEPWithSHA__256AndMGF1Padding` 变体走 BC；`RSA/ECB/NoPadding` 已 `@Deprecated` | 密钥为 **Base64(X.509) 公钥 / Base64(PKCS#8) 私钥**；默认 2048 位，OAEP 下单块明文上限 `(keyBits/8) - 66` 字节，超长自动分段 | 未使用 | 明文与解密输出均为 UTF-8；密文 Base64 | 加解密与签名/验签均线程安全（每次调用新建实例） | 统一包装为 `RsaException` |
| RSA 签名 | 默认 `SHA256withRSA`（可选 `MD5withRSA`、`SHA3-256withRSA`——后者需 BouncyCastle，JDK 8 默认 Provider 不支持） | 同上 | — | 原文固定按 UTF-8 取字节 | ✅ 每次调用使用独立 `Signature` 实例 | 统一包装为 `DigitalSignatureException` |
| DSA | 默认 `SHA256withDSA` | Base64 公私钥；默认 1024 位 | — | 同上 | ✅ 同上 | 统一包装为 `EncryptException` |
| DH | 密钥协商，算法 `DH`，默认 2048 位（JDK Provider） | Base64(X.509) 公钥 / Base64(PKCS#8) 私钥 | — | — | `generateSharedSecret` 每次新建 `KeyAgreement`，可并发；`load` 与读取并发不安全 | 统一包装为 `DhException`。**不是加密算法，只做密钥协商；协商结果必须经 HKDF/SHA-256 派生后才能作对称密钥** |
| ECDH（ECC） | 密钥协商，算法 `EC`，默认曲线 `secp256r1`（P-256），256 位（JDK SunEC） | Base64(X.509) 公钥 / Base64(PKCS#8) 私钥 | — | — | 同上 | 统一包装为 `EccException`。**不是加密算法，只做密钥协商；协商结果必须经 HKDF/SHA-256 派生后才能作对称密钥** |
| ECDSA | 默认 `SHA256withECDSA`；默认曲线 `secp256r1`（JDK Provider） | Base64(X.509) 公钥 / Base64(PKCS#8) 私钥 | — | 原文固定按 UTF-8 取字节 | ✅ 每次调用使用独立 `Signature` 实例 | 统一包装为 `DigitalSignatureException` / `EcdsaException` |
| JWT | HS256 | 签名密钥为任意字符串；**类内置默认值 `TOOLS&DH$*001`，仅用于演示，生产必须显式传入** | — | Claim 值仅支持 String（`v.asString()`，非字符串 Claim 解析后为 null） | ✅（无共享状态） | 签名/过期校验失败抛 auth0 `JWTVerificationException` |
| Base64 | 标准 RFC 4648 | — | — | UTF-8 | ✅ | 非法 Base64 输入抛 `IllegalArgumentException` |
| CRC32 | — | — | — | — | ✅ 每次调用使用独立实例 | — |

**已知风险与限制（如实记录，行为未改动）**：

1. ⚠️ `DES` 默认模式为 ECB 且 IV 不生效（`CipherSupply.initCipher` 的 `case DES` 分支）。为保证既有密文可解，默认模式未改动；建议在配置中显式指定 IV 生效的模式并自行确认安全性，短期内建议优先使用 AES。
2. ⚠️ `JWTUtils.setSign` 存在硬编码默认签名串 `TOOLS&DH$*001`，未显式传参时将使用它签发/校验令牌。请始终使用带 `sign` 参数的重载（移除默认值属 breaking change，待定）。
3. ⚠️ `AesHelper` 等基于静态 `properties` 的设计意味着一份 JVM 内只有一份激活配置；多组密钥请使用 `load(KeyPair, name)` 的分组机制，或直接使用 `XxxKeyGenerator` + `SymmetricalUtils` 显式传参。
4. TODO：RSA 分段加密对超长明文的分段边界在部分 Provider 下与标准实现存在差异，尚未做互操作矩阵验证。
5. legacy 算法：Blowfish、3DES、RC2、RC4、RC5、Skipjack 均为历史遗留算法，**不推荐用于新设计**。其中 RC4 是流密码，无 IV、无填充，密文长度等于明文长度，属于已被证明不安全的协议遗留；其余算法也存在密钥/分组偏小或模式默认 ECB 的问题，仅用于兼容既有系统。
6. 密钥语义差异：`des3` 的密钥为 **Hex 字符串**（16/24 字节），而 `DES` 的密钥为 **UTF-8 编码后的原始 8 字节字符串**，两者不可混用；`tdea` 与 `3DES`、`Triple DES`、`DESede` 是同一算法的不同称呼，实现统一位于 `symmetrical.des.des3`，`symmetrical.tdea` 仅为包别名说明，不含实现类。
7. DH / ECDH **不是加密算法**，只负责密钥协商：`generateSharedSecret` 返回的是原始共享秘密，未经 KDF 派生的结果在数学上不均匀，**必须先经 HKDF/SHA-256 等密钥派生函数处理**，才能作为 AES 等对称算法的密钥使用。

> 已修复项（`2.0.0`）：解密输出字符集（此前用 JVM 默认 charset 导致 GBK 平台乱码）已统一为 UTF-8；MD5/SHA/签名原文取字节同样统一为 UTF-8；`RsaProperties` 默认签名算法由 JDK 8 不支持的 `SHA3-256withRSA` 改为 `SHA256withRSA`；`RSA` 实体配置（算法/模式/签名算法/密钥位数）此前在 `load(KeyPair)` 时被忽略，现已生效并联动重算分块大小；`OAEPWithSHA-256AndMGF1Padding` 分支缺失 `break` 导致该模式不可用已修复；DSA 字符串密钥重载向 `KeyFactory` 误传签名算法名已修复；`DsaProperties` 默认签名算法误设到 `algorithmMode` 字段已修正；CRC32 共享静态实例（非线程安全）已改为每次调用独立实例；`SignatureUtils` 签名/验签（含 RsaHelper/DsaHelper 路径）由共享静态 `Signature` 改为每次调用独立实例（并发安全）；`Base64Utils.encode(String)` 与 `AsymmetricUtils.decryptAfter` 的平台 charset 问题（GBM/GBK 平台乱码）统一为 UTF-8；`DsaHelper.load(KeyPair)` 此前不解析密钥对象、必然抛"publicKey|privateKey 不能为空"已修复；`RsaKeyGenerator.getKeyPair()` 等入口未初始化默认配置时 NPE 已修复；starter 样例 yml 的 DES 密钥修正为 8 字节（"BeYour18"）并验证 round-trip；`DsaKeyGenerator.getDsaProperties()` 误用 `RsaKeyGenerator` 向 DSA 配置填充 RSA 密钥已改为 DSA 密钥生成；`IdeaHelper` 密钥解析误调 `AesKeyGenerator` 已改用 `IdeaKeyGenerator`（其 String 重载密钥语义随之由 UTF-8 原文对齐为 Hex）；`RsaHelper` 的 "Aes enable: false" 等跨算法复制粘贴文案/日志已修正。详见 [CHANGELOG](../CHANGELOG.md)。

## 最小示例

> 以下示例均针对上述表格中"可用"的能力编写。`import` 从略，类名均可通过 `org.tools.encrypt.*` 定位。

### MD5 / SHA 摘要

```java
String md5 = MD5Utils.encode("password");            // 大写十六进制
String lower = MD5Utils.encode("password", true);    // 小写（注意：实现采用 BigInteger 路径，可能丢失前导零）
String sha256 = ShaUtils.encode("password");         // 默认 SHA-256，小写十六进制
String sha512 = ShaUtils.encode("SHA-512", "password");
```

### Base64 / CRC32

```java
String encoded = Base64Utils.encode("Hello World");  // 标准_Base64，UTF-8
String decoded = Base64Utils.decode(encoded);

long crc = Crc32Utils.encode("data".getBytes());
```

### AES 对称加解密

```java
// 1. 生成随机密钥（也可用自定义 Hex 字符串，长度对应 AES-128/192/256）
SecretKey key = AesKeyGenerator.getSecretKey();
String hexKey = AesKeyGenerator.getSecretKeyStr(key); // 便于持久化存储的 Hex 字符串

// 2. 加载配置：CBC 模式下 IV 必须是 16 字节（UTF-8 字节数）
AesProperties props = new AesProperties();
props.setSecretKey(key);
props.setIvParameter("0123456789abcdef");
AesHelper.load(props);

// 3. 加解密（密文为 Base64）
String ciphertext = AesHelper.encode("敏感数据");
String plaintext  = AesHelper.decode(ciphertext);     // "敏感数据"

// 也可以不依赖全局配置，指定密钥重载：
String c2 = AesHelper.encode(hexKey, "敏感数据");
String p2 = AesHelper.decode(hexKey, c2);
```

### DES 对称加解密

```java
DesProperties props = new DesProperties();
props.setSecretKeyStr("8bytekey");                       // UTF-8 编码后必须恰好 8 字节
props.setSecretKey(DesKeyGenerator.getSecretKey("DES", "8bytekey"));
// props.setAlgorithmMode("DES/ECB/NoPadding");          // 可选模式，见边界表
DesHelper.load(props);

String ciphertext = DesHelper.encode("敏感数据");
String plaintext  = DesHelper.decode(ciphertext);
```

### RSA 非对称加解密 + 签名

```java
// 1. 生成密钥对（默认 2048 位），返回 Base64(X.509) 公钥 / Base64(PKCS#8) 私钥
RsaKeyGenerator.getKeyPair();                         // 内部生成并持有当前密钥对
String publicKey = RsaKeyGenerator.getPublicKeyStr();
String privateKey = RsaKeyGenerator.getPrivateKeyStr();

// 2. 加载配置
Rsa rsa = new Rsa();
rsa.setPublicKey(publicKey);
rsa.setPrivateKey(privateKey);
RsaHelper.load(rsa);

// 3. 加解密（默认 OAEPWithSHA-256AndMGF1Padding，超长明文自动分段）
String ciphertext = RsaHelper.encode("敏感数据");
String plaintext  = RsaHelper.decode(ciphertext);

// 4. 签名 / 验签（默认 SHA256withRSA；线程安全，见边界表）
String sign = RsaHelper.sign("待签名内容");
boolean pass = RsaHelper.verify("待签名内容", sign);
```

### DSA 签名

```java
DsaProperties props = new DsaProperties();
props.setPublicKeyStr("...");                         // Base64(X.509)
props.setPrivateKeyStr("...");                        // Base64(PKCS#8)
DsaHelper.load(props);

String sign = DsaHelper.sign("待签名内容");
boolean pass = DsaHelper.verify("待签名内容", sign);
```

### JWT

```java
Map<String, String> payload = new HashMap<>();
payload.put("userId", "123");

// 始终显式传入签名密钥；类内置默认值仅供演示
String token = JWTUtils.generatorToken(payload, "your-strong-secret");
Map<String, String> parsed = JWTUtils.parseToken(token, "your-strong-secret"); // {userId=123}

// 带过期时间（签名 / 过期校验失败会抛 JWTVerificationException）
String expiring = JWTUtils.generatorToken(payload, new Date(System.currentTimeMillis() + 3600_000L), "your-strong-secret");
```

## 与 encrypt-spring-boot-starter 的关系

`encrypt` 是纯工具库；`encrypt-spring-boot-starter` 在其上提供 Spring Boot 自动装配：通过 `application.yml` 的 `tools.encrypt.*` 配置 AES / DES / IDEA / RSA，自动完成各 `XxxHelper.load(...)` 并注册配置 Bean。启用 / 禁用算法、多密钥分组、密钥来源建议均见 [starter 文档](../tools-spring-boot-starter/encrypt-spring-boot-starter/README.md)。

## 测试

测试位于 `src/test/java`，为**冒烟测试**套件（JUnit 4，遵循仓库"Xxx 冒烟测试"惯例）：encrypt 模块的**每个公开方法**（工具类、Helper、KeyGenerator、底层 Utils、算法枚举）至少有一个冒烟用例，核心路径为 round-trip / 已知答案断言；并发行为另由 `SignatureUtilsConcurrencyTest` 覆盖。

> 注意：根 `pom.xml` 默认 `skipTests=true`（只编译不执行）。执行测试请使用：
>
> ```bash
> mvn -B -DskipTests=false -pl encrypt -am test
> ```
>
> 此前版本的既有失败 `RsaHelperTest.testEncode`（默认签名算法 `SHA3-256withRSA` 在 JDK 8 不可用）已通过将默认签名算法改为 `SHA256withRSA` 修复，当前全部测试通过。

## 安全问题报告

发现安全问题请勿公开提交 Issue，报告渠道与响应策略见 [SECURITY.md](../SECURITY.md)。
