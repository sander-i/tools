# encrypt-spring-boot-starter — 5 分钟接入

`encrypt-spring-boot-starter` 为 [encrypt 模块](../../../encrypt/README.md) 提供 Spring Boot 自动装配：在 `application.yml` 中配置 `tools.encrypt.*`，启动时自动完成 AES / DES / IDEA / RSA 的密钥装载（`XxxHelper.load(...)`），业务代码直接调用 `AesHelper`、`RsaHelper` 等静态工具类即可。

- 坐标：`io.gitee.sander-o.spring.boot:encrypt-spring-boot-starter`（版本随根 `pom.xml`，当前 `2.1.0`）
- 适配：Spring Boot 2.7.x（同时通过 `spring.factories` 与 `AutoConfiguration.imports` 注册，2.7 均可加载）
- Java：1.8+

## 5 分钟接入

### 第 1 步：引入依赖

```xml
<dependency>
    <groupId>io.gitee.sander-o.spring.boot</groupId>
    <artifactId>encrypt-spring-boot-starter</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 第 2 步：（可选）显式启用

自动装配默认开启：只要 classpath 引入本 starter 且存在任意 `tools.encrypt.*` 配置，`EncryptAutoConfiguration` 即生效，**启动类无需任何注解**。若希望在代码上显式声明启用，可加 `@EnableToolsEncrypt`：

```java
@SpringBootApplication
@EnableToolsEncrypt   // 可选：显式导入自动配置，效果与默认自动装配相同
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 第 3 步：配置 `application.yml`

```yaml
tools:
  encrypt:
    symmetrical:
      aes:
        enabled: true                          # 是否启用 AES 自动装配
        algorithm: AES                         # 算法
        algorithm-mode: AES/CBC/PKCS5Padding   # 模式/填充
        key-bits: 256                          # 密钥位数
        iv-parameter: "0123456789abcdef"       # CBC 模式必须为 16 字节（UTF-8）
        secret-key: "e5f7a1c93b8d24670fa1c93b8d24670f"   # Hex 字符串，16/24/32 字节对应 AES-128/192/256
        group-enabled: true                    # 多密钥分组（按 name 切换）
        group:
          - name: phone
            secret-key: "a1b2c3d4e5f60718293a4b5c6d7e8f90"
            iv-parameter: "abcdef0123456789"
          - name: id-card
            secret-key: "11223344556677889900aabbccddeeff"
            iv-parameter: "1122334455667788"
      des:
        enabled: true
        # DES 密钥为原始字符串，UTF-8 编码后必须恰好 8 字节（如 BeYour18）；
        # 默认模式 DES 即 ECB/PKCS5Padding，IV 不生效，见"当前限制"
        secret-key: "BeYour18"
        group-enabled: true
        group:
          - name: pwd
            secret-key: "BeYour18"
          - name: id-card
            secret-key: "12345678"
      idea:
        enabled: false
    asymmetric:
      rsa:
        enabled: true
        algorithm: RSA
        algorithm-mode: RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING
        sign-algorithm: SHA256withRSA          # 签名/验签算法（JDK 8 默认 Provider 不支持 SHA3-256withRSA）
        key-bits: 2048
        public-key: "MIGfMA0GCSqGSIb3DQEBAQUAA4GN...（Base64 X.509 公钥）"
        private-key: "MIICdgIBADANBgkqhkiG9w0BAQ...（Base64 PKCS#8 私钥）"
        group-enabled: false
```

### 第 4 步：在业务代码中使用

```java
@RestController
@RequestMapping("/api/encrypt")
public class EncryptController {

    /** AES 加密（使用全局激活配置） */
    @GetMapping("/aes/encode")
    public String aesEncode(@RequestParam String data) throws AesException {
        return AesHelper.encode(data);
    }

    /** AES 解密 */
    @GetMapping("/aes/decode")
    public String aesDecode(@RequestParam String data) throws AesException {
        return AesHelper.decode(data);
    }

    /** RSA 签名验签 */
    @GetMapping("/rsa/sign")
    public String rsaSign(@RequestParam String data) throws RsaException {
        return RsaHelper.sign(data);
    }
}
```

### 第 5 步：切换密钥分组（可选）

配置了 `group-enabled: true` 时，可用 `XxxHelper.load(properties对象, "分组名")` 在运行期切换激活密钥（进程内共享，切换影响后续所有未显式传参的调用）：

```java
Aes aes = new Aes();          // 从 yml 绑定的同一结构重新构造，或自行组装
// ...aes 填充 group 列表...
AesHelper.load(aes, "phone"); // 激活 phone 分组
```

## 自动装配了什么

`EncryptAutoConfiguration`（`@ConditionalOnClass(Encrypt.class)` + `@Conditional(OnEncryptPropertiesCondition.class)`）会按配置注册以下 Bean，并在注册前完成对应 `XxxHelper.load(...)`：

| Bean | 类型 | 装配条件 |
|---|---|---|
| `rsa` | `RsaProperties` | `tools.encrypt.asymmetric.rsa.enabled=true` |
| `des` | `DesProperties` | `tools.encrypt.symmetrical.des.enabled=true` |
| `aes` | `AesProperties` | `tools.encrypt.symmetrical.aes.enabled=true` |
| `idea` | `IdeaProperties` | `tools.encrypt.symmetrical.idea.enabled=true` |

- 所有 Bean 均带 `@ConditionalOnMissingBean`，可自行声明同类型 Bean 覆盖默认装配。
- 配置绑定由 starter 的 `EncryptProperties`（继承 encrypt 的 `Encrypt` 实体树，`@ConfigurationProperties("tools.encrypt")`）完成；`encrypt` 模块本身不依赖 Spring。`@EnableToolsEncrypt` 为可选的显式 `@Import`，自动装配不依赖它。
- 不配置某个算法的 `enabled=true`，该算法的 Bean 与 `load` 都不会发生，对应 Helper 处于未装载状态（调用其 `encode/decode` 会抛出空指针或未初始化异常）。

## 密钥来源建议

- **不要**把生产密钥提交到代码仓库或打包进 jar。建议通过环境变量、启动参数、配置中心或 KMS 注入，例如：
  ```yaml
  tools:
    encrypt:
      symmetrical:
        aes:
          secret-key: ${TOOLS_AES_KEY}
  ```
- AES 密钥请使用 Hex 随机串（`AesKeyGenerator.getSecretKeyStr(AesKeyGenerator.getSecretKey())` 可生成 128 位随机 Hex 密钥）。
- RSA 密钥对可用 `RsaKeyGenerator.getPublicKeyStr() / getPrivateKeyStr()` 生成后妥善保管。

## 从配置层面避免不安全组合

**当前限制 / TODO**（截至 `2.0.0`）：

- starter **不会**拒绝不安全组合：DES 默认 `DES` 模式即 ECB、IV 不生效；AES 未配置 IV 时 CBC 模式会在运行期才报错。starter 目前没有启动期校验拦截这些组合 —— TODO：计划在 `EncryptAutoConfiguration` 增加启动期校验（拒绝 DES/ECB 与过短密钥），在此之前请参照 [encrypt 安全边界表](../../../encrypt/README.md#安全边界) 自查配置。
- 配置中的 `key-bits` 不会改变 `secret-key` 的实际长度（密钥长度由 Hex 解码后的字节数决定），两者不一致时以 `secret-key` 为准。

## 常见问题

**Q：配置了 `tools.encrypt.*` 但调用 `AesHelper.encode` 报空指针 / 未初始化？**
确认两件事：对应算法 `enabled: true`；`secret-key` 已配置。任一缺失该算法不会被装配（`@EnableToolsEncrypt` 并非必需）。配置请写在**你自己的** `application.yml` 里——2.0.0 起 starter 不再打包任何 `application.yml`（旧版本打包的示例配置会被 Spring Boot 从依赖 jar 加载，存在用演示密钥意外激活加密的风险，已移除）。

**Q：加解密报 `BadPaddingException` 或解出乱码？**
两端 `algorithm-mode`、密钥、IV 必须完全一致。最常见的错误：一端用 AES/CBC/PKCS5Padding、另一端默认模式；或 IV 长度不是 16 字节；或 DES 两端密钥字节数不同。

**Q：AES 报 `InvalidKeyException: Key length not 128/192/256 bits`？**
`secret-key` 是 Hex 字符串，解码后必须为 16/24/32 字节。检查是否误填了明文字符串（如 `BeYourself` 不是合法 Hex，会先抛解码异常；长度不足的 Hex 则报密钥长度错误）。

**Q：多环境密钥不同怎么办？**
按 Spring Profile 放置不同 `application-{profile}.yml`，或使用分组 + `load(keyPair, name)` 运行期切换。

## 安全问题

安全问题请勿公开提交 Issue，见 [SECURITY.md](../../../SECURITY.md)。
