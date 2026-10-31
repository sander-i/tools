# encrypt

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

★ 旗舰模块：对称 / 非对称加密、密钥协商、摘要、数字签名、JWT、Base64/CRC32 编码。坐标 `io.gitee.sander-o:encrypt`。基于 JDK JCA/JCE，仅在 IDEA、无 `RSA/ECB/` 前缀的 `OAEPWithSHA__256AndMGF1Padding` 变体、RC5、Skipjack 处使用 BouncyCastle Provider（默认 `RSA/ECB/OAEPWithSHA-256AndMGF1Padding` 由 JDK SunJCE 提供）。

**零 Spring 依赖**（2.0.0 起移除 `spring-boot-autoconfigure`），`dependency:tree` 中不应出现 `org.springframework`。配置实体类是纯 POJO，`@ConfigurationProperties` 只存在于 starter 侧。

规模：`src/main/java` 共 143 个 Java 文件（其中 99 个类/枚举，其余为 `package-info.java`），41 个测试类。

## Key Files

| File | Description |
|------|-------------|
| `README.md` | **权威文档**：能力矩阵 + 安全边界表 + 已知风险 + 最小示例 |
| `pom.xml` | 依赖 `bedrock`、`commons-codec`、`java-jwt`、`bcprov-jdk18on:1.78.1` |
| `CipherAbst.java` / `CipherSupplyAbst.java` | Cipher 抽象骨架（`actuator`/`subLevel`/`initSupport`/`initCipher`/`prepCipher`） |
| `CipherSupply.java` | 按算法模式初始化 Cipher，含 DES 的 ECB 分支 |
| `EncryptKeyGenerator.java` | 密钥生成器工厂（无状态，避免切换算法竞态） |
| `entity/Encrypt.java` | 配置聚合根：对称 / 非对称 / 签名子配置 |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `asymmetric/` | `AsymmetricUtils`（RSA 分块加解密）、`CipherAsymmetric`；`asymmetric/rsa/` `RsaHelper`、`RsaKeyGenerator`；`asymmetric/dh/`（`DhHelper`、`DhKeyGenerator`）与 `asymmetric/ecc/`（`EccHelper`、`EccKeyGenerator`）已实现，仅做密钥协商（DH / ECDH），不提供 `encode`/`decode`；`elgamal`/`knapsack`/`rabin` **仅有 package-info 占位，无实现** |
| `basic/` | `BasicUtils`（按算法名算摘要）、`JWTUtils`；`basic/base/Base64Utils`、`basic/crc32/Crc32Utils`、`basic/md5/`、`basic/sha/ShaUtils` |
| `digitalsignature/` | `SignatureUtils`（统一入口）、`digitalsignature/dsa/`（`DsaHelper`、`DsaKeyGenerator`）、`digitalsignature/ecdsa/`（`EcdsaHelper`、`EcdsaKeyGenerator`） |
| `entity/` | 配置实体：`Encrypt`、`EncryptProperties`、`KeyPair`；`entity/asymmetric`、`entity/symmetrical`、`entity/digitalsignature` |
| `enums/` | `AlgorithmEnum`、`AlgorithmModeEnum`、`SignAlgorithmEnum` |
| `exception/` | `EncryptException` 及各算法专属异常（`AesException`/`RsaException`/`DsaException`/`BlowfishException`…），均带 `package-info` |
| `symmetrical/` | `SymmetricalUtils`、`CipherSymmetrical`；`symmetrical/aes/`、`des/`、`idea/`；`symmetrical/des/des3/`（`Des3Helper`、`Des3KeyGenerator`，即 TDEA）、`blowfish/`、`rc/rc2/`、`rc/rc4/`、`rc/rc5/`、`skipjack/` 均已实现；`tdea/` 仅为 TDEA 包别名说明，不含实现类 |

## For AI Agents

### Working In This Directory

- **安全行为改动必须同步 `README.md` 的安全边界表与 CHANGELOG**。该表记录的是「当前实际行为」，不是安全承诺；DES 默认 ECB、JWT 硬编码默认密钥这两条已知风险是有意保留的（改动属 breaking change，需先讨论）。
- **统一约定**：明文/解密输出 UTF-8，密文 Base64。禁止使用 JVM 默认 charset——历史上多个 GBK 平台乱码 bug 都源于此。
- **线程安全**：`XxxHelper` 每次调用新建 Cipher/Signature（并发安全）；但静态 `properties` 由 `load` 替换，`load` 与 `encode/decode` 并发不安全。新增代码不得引入共享的可变 Cipher 实例。
- 密钥语义差异（易错点）：
  - AES / IDEA / Blowfish / 3DES / RC2 / RC4 / RC5 / Skipjack：密钥为 **Hex 字符串**（AES 16/24/32 字节；IDEA 32 个 Hex 字符；Blowfish 4~56 字节且位数须为 8 的倍数；RC2 5~128 字节；RC4 5~128 字节；RC5 默认 16 字节；3DES 16/24 字节；Skipjack 固定 10 字节）
  - DES：密钥为**原始字符串**，UTF-8 后必须恰好 8 字节（与 3DES 的 Hex 语义不同）
  - RSA / DSA / ECDSA / DH / ECC：Base64(X.509) 公钥 / Base64(PKCS#8) 私钥
- DH / ECDH 只做密钥协商，不提供 `encode`/`decode`；原始共享秘密须经 HKDF/SHA-256 派生后才能作对称密钥。
- RSA 默认 `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`（JDK SunJCE；仅无 `RSA/ECB/` 前缀的变体走 BC），单块明文上限 `(keyBits/8) - 66`，超长自动分段。`RSA/ECB/NoPadding` 已 `@Deprecated`。
- 签名默认 `SHA256withRSA`（`RsaProperties` 初始化块设定；`SHA3-256withRSA` 在 JDK 8 默认 Provider 不可用）。
- JWT 的 `JWTUtils.setSign` 有内置默认签名串 `TOOLS&DH$*001`（演示用），生产必须显式传 `sign`。
- 占位包（无实现，仅 `elgamal`/`knapsack`/`rabin`）已在 README 能力矩阵标 ⛔，不要误以为可用；也不要顺手删掉，它们保留了包结构规划。DH/ECC 已实现但只做密钥协商，共享秘密必须经 HKDF/SHA-256 派生后才能作对称密钥。

### Testing Requirements

```bash
mvn -B -DskipTests=false -pl encrypt -am test
```

JUnit 4 冒烟测试，覆盖各 Helper 的全部公开方法（`load`/`encode`/`decode`/`getProperties`）与并发用例（`SignatureUtilsConcurrencyTest`）。新增算法或重载应同时补 round-trip + 已知答案断言。

### Common Patterns

- Helper 三件套：`XxxHelper`（静态门面）+ `XxxKeyGenerator`（密钥生成/解析）+ `XxxProperties`（配置实体）。
- 配置实体继承链：`EncryptProperties`（算法/模式/密钥/分块/签名）→ `AesProperties`/`DesProperties`/`IdeaProperties`/`BlowfishProperties`/`Des3Properties`/`Rc2Properties`/`Rc4Properties`/`Rc5Properties`/`SkipjackProperties`/`RsaProperties`/`DsaProperties`/`EcdsaProperties`/`DhProperties`/`EccProperties`。
- 异常统一包装并保留 cause：JCE 的 `InvalidKeyException` 等不得直接抛给调用方。
- 分组机制：`load(KeyPair, name)` 支持同一 JVM 内多组密钥切换。

## Dependencies

### Internal

`bedrock`（`BaseUtils` 等基础工具）。

### External

| 依赖 | 版本 | 用途 |
|---|---|---|
| BouncyCastle `bcprov-jdk18on` | 1.78.1 | IDEA、`OAEPWithSHA__256AndMGF1Padding`、RC5、Skipjack Provider（版本就地声明） |
| commons-codec | 1.15 | Base64 等编码 |
| java-jwt (auth0) | 4.4.0 | JWT 签发/校验 |

## Manual Notes
