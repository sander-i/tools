# Changelog

本项目所有对外可见的变更记录于此。格式遵循 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)。

## [Unreleased]

> 对应开发版本 `1.0.6.7-d01`（根 `pom.xml`）。以下为该版本尚未发布前累计的实际改动；发布时会替换为正式版本号。

### Added

- 新增 `docs/operations-guide.html`：自包含 HTML 操作文档（无外部依赖），含目录导航、能力矩阵、安全边界表、各能力可复制示例、Spring Boot 5 分钟接入指引、FAQ 与安全问题报告渠道；可克隆仓库后用浏览器直接打开，亦支持离线分发与打印（含打印样式）。中英文 README 均已接入入口链接。
- 中英文 README 优化：中文 README 新增"操作文档（HTML）"章节与文档导航链接；英文 README（`README.en.md`）整体重写，与中文版定位对齐（encrypt 旗舰、坐标与示例、文档链接、贡献流程），移除 `Latest Version` 占位符与 Gitee 模板段落。
- 新增 `encrypt/README.md`：能力矩阵、安全边界表、各能力最小示例。
- 新增 `encrypt-spring-boot-starter/README.md`：5 分钟接入指南、自动装配说明、FAQ。
- 新增 `bedrock/README.md`、`fortify/README.md`（配套模块简版说明）。
- 新增 `SECURITY.md`（安全问题报告渠道与保守的响应策略）。
- 新增本 `CHANGELOG.md`。
- 新增 `.github/workflows/ci.yml` 构建验证草案（仓库托管于 Gitee，实际平台落地需按需调整）。
- encrypt 模块主要公开类补充 Javadoc（算法 / 模式 / 密钥 / IV / 线程安全 / 异常行为说明），不改变方法行为。
- encrypt 模块补充最小单元测试（round-trip 与已知答案测试）。

### Changed

- 项目叙事调整：根 README 从"综合工具箱"改为"以 encrypt 为旗舰的 Java 加密库（含配套工具）"，`bedrock`、`fortify` 降级为配套模块。
- **AES 密钥解析行为变更（未发布版本内的变更）**：`AesKeyGenerator.getSecretKey(String, String)` 由"密钥字符串直接取 UTF-8 字节"改为"按 Hex 解码"，与 `getSecretKeyStr` 的 Hex 输出对应。在此前开发版本中使用明文 AES 密钥的调用方升级后需改用 Hex 密钥。
- `ClassUtils.fetchField`：跳过 `static` 字段与 `serialVersionUID`（此前静态字段会导致 `PropertyDescriptor` 构造失败被吞日志），属性复制行为更稳定。
- 构建：父 POM 固定 `com.aliyun:tea` 版本为 1.4.2，避免版本范围解析；`central-publishing-maven-plugin` 升级至 0.7.0；`autoPublish` 改为 false；GPG 签名配置调整。
- 构建：surefire 的 `skipTests` 由硬编码 `true` 改为属性引用（默认值仍为 `true`，行为不变），支持 `-DskipTests=false` 临时执行测试（CI 使用）。

### Removed

- 根 `pom.xml` 移除私有 Nexus 仓库（ailuoli）的 repository / pluginRepository 及相关注释残留。

### Fixed

- Gitee 在线文档生成失败修复：根 POM 此前在 `<repositories>` / `<pluginRepositories>` 中硬编码阿里云镜像，Gitee 构建环境无法解析 `maven.aliyun.com`（Unknown host），导致 `git-commit-id-plugin` 等插件下载失败、文档构建中断。已移除 POM 内全部仓库声明（发布型库的 POM 不应把区域镜像强加给下游），默认走 Maven Central，需要镜像加速的环境在自身 `settings.xml` 配置（README 已补说明）。全模块 `mvn javadoc:javadoc` 已本地验证通过。
- Gitee 文档生成二次失败修复：Gitee 构建环境的内网镜像（`maven.local/repository/central`）缺失第三方插件 `git-commit-id-plugin`（Apache 官方插件均可用）。排查确认 `git.properties` 产物在代码库中零消费，已将该插件移出默认构建、收入可选 profile `-Pgit-info`（保留 failOnNoGitDirectory=false），文档与常规构建不再解析该第三方构件。
- Gitee 文档生成三次失败修复：Gitee 内网镜像缺失 2015 年代老版本插件的传递依赖（`maven-shared-utils:0.7`、`plexus-compiler:2.5` 等，镜像按热度缓存、老古董构件缺失）。构建插件升级至当代主流版本（必然存在于任何缓存镜像）：`maven-compiler-plugin 3.3 → 3.8.1`、`maven-surefire-plugin 2.5 → 3.2.5`、`maven-javadoc-plugin 2.9.1 → 3.2.0`、`maven-source-plugin 3.0.1 → 3.2.1`。JDK 8 / Maven 兼容性与全部构建行为已在本地验证（全量测试 + package 生命周期含 sources/javadoc jar）。
- Gitee 文档生成四次失败修复：Gitee 内网镜像同样缺收 `com.aliyun:*` 运行时依赖（alidns20150109、tea 系列，真实 Central 均存在）。该组依赖为 fortify DDNS 功能必需、无法绕过，改为**随仓库分发**：`local-repo/` 以标准 Maven 仓库布局收录 com.aliyun 完整依赖闭包（约 2.2MB，11 个构件），根 POM 声明 `file://${maven.multiModuleProjectDirectory}/local-repo` 仓库（file 协议不走网络、不受镜像劫持），受限构建环境可自给自足。已通过"移除本机 ~/.m2 缓存后纯靠 local-repo 构建 fortify"验证；`.gitignore` 放行 local-repo 的 jar。

- `SymmetricalUtils.decode` 解密输出此前使用 JVM 默认 charset，在非 UTF-8 平台（如中文 Windows GBK）上含中文的明文 round-trip 乱码；现与加密侧对称，统一 UTF-8。
- `MD5Utils.encode(String)`、`ShaUtils.encode`、`SignatureUtils` 签名/验签的原文取字节由 JVM 默认 charset 统一改为 UTF-8（非 ASCII 输入的摘要/签名值会与旧版不同；ASCII 输入不受影响）。
- `RsaProperties` 默认签名算法由 `SHA3-256withRSA` 改为 `SHA256withRSA`：前者在 JDK 8 默认 Provider（SunRsaSign）不可用，默认配置在纯 JDK 8 环境装载即失败。
- `RsaHelper.load(KeyPair)` 此前忽略 KeyPair/`Rsa` 实体上的 algorithm / algorithmMode / signAlgorithm / keyBits 配置，现已生效，并按新的位数/模式联动重算分块大小（新增 `RsaProperties.recalcMaxBlocks()`）。
- `Rsa` 实体默认签名算法由弱算法 `MD5withRSA` 改为 `SHA256withRSA`。
- `CipherSupply.initCipher` 的 `OAEPWithSHA-256AndMGF1Padding` 分支缺失 `break`，成功 init 后仍落入 default 抛异常，该模式实际不可用；已补上。
- `DsaHelper.sign/verify` 的字符串密钥重载向 `KeyFactory` 误传签名算法名（应为密钥算法名 `DSA`），导致必然抛 `NoSuchAlgorithmException`；已修复，并澄清 `DsaKeyGenerator` 对应参数的 Javadoc。
- `DsaProperties` 初始化块把默认签名算法误设到 `algorithmMode` 字段、`signAlgorithm` 为 null，导致 `DsaHelper.load` 抛 NPE；已修正为设置 `signAlgorithm`。
- `Crc32Utils` 共享静态 `CRC32` 实例，非线程安全且多次调用结果累积；已改为每次调用使用独立实例。
- `AliyunDdnsUtils` 新增接受 `Client` 参数的重载（`readDomainRecords` / `updateDomainRecord` / `updateDomainRecordBatch`），支持复用连接与测试注入；原签名行为不变，`updateDomainRecordBatch` 顺带改为整个批次复用同一 Client。`AliyunDdnsUtilsTest` 改用注入 mock 的重载，不再直连阿里云 API。
- **Signature 并发安全**：`SignatureUtils.sign/verify` 由共享静态 `Signature` 实例改为每次调用创建独立实例，`RsaHelper` / `DsaHelper` 的签名验签路径随之并发安全（`load` 保留算法可用性预检但不再持有共享对象；移除 `RsaHelper` 的静态 `signature`/`publicCipher`/`privateCipher` 与 `DsaHelper` 的静态 `signature` 遗留字段）。新增多线程混合算法的并发回归测试。
- starter 样例 `application.yml` 配置修正：DES 密钥 `BeYourself`（10 字节，超过 DES 要求的 8 字节，加密时抛 `InvalidKeyException`）改为 8 字节密钥 `BeYour18` 并去除无意义的 `key-bits: 128` 与重复的 `id-card` 分组；AES 密钥 `BeYourself`（非合法 Hex）改为合法 Hex 密钥并补充 CBC 模式必需的 `iv-parameter`。均已通过 round-trip 测试验证。
- `RsaKeyGenerator.getKeyPair()`（及 `getPublicKey()` / `getPrivateKey()` / `getPublicKeyStr()` / `getPrivateKeyStr()`）在未先调用过 `getPublicKey(String)` 等方法时直接 NPE（`properties` 未懒加载初始化，仅靠调用顺序侥幸可用）；`init()` 现首先执行 `initProperties()`。
- `Base64Utils.encode(String)` 使用 JVM 默认 charset，与 `decode` 的 UTF-8 不对称（GBK 平台含中文 round-trip 乱码）；统一为 UTF-8。
- `AsymmetricUtils.decryptAfter` 使用 JVM 默认 charset（同款乱码问题，此前仅修复了对称路径）；统一为 UTF-8。
- `DsaHelper.load(KeyPair)` 只装载密钥字符串、不解析密钥对象，随后 `load(DsaProperties)` 必然抛"publicKey|privateKey 不能为空"；已按字符串解析出密钥对象后装载。
- `DsaKeyGenerator.getDsaProperties()` 误调用 `RsaKeyGenerator`，向 DSA 配置填充 RSA 密钥（复制粘贴错误）；已改为 DSA 密钥生成。
- `IdeaHelper` 的密钥解析误调用 `AesKeyGenerator`（跨算法调用），已改用 `IdeaKeyGenerator`。为对齐语义，`IdeaKeyGenerator.getSecretKey(String, String)` 的密钥字符串由 UTF-8 原文改为 **Hex 解码**（与 `AesKeyGenerator` 及 `getSecretKeyStr` 输出对称）——**行为变更**：直接调用该重载并传入原文密钥的使用方需改用 Hex 密钥。
- `IdeaUtils` 的 Base64 由 commons-codec（宽容解析，非法字符不报错）对齐为 **JDK 严格实现**，与 `SymmetricalUtils` 一致——**行为变更**：非法 Base64 密文现在抛 `IllegalArgumentException`（与 `SymmetricalUtils` / `Base64Utils` 行为一致，未经包装直接抛出）。
- 类名拼写修正：`CipherSupplyExcepiton` → `CipherSupplyException`（公开类型名，引用方需同步更名）。
- 预留密钥处理方法的场景落地与清理：`DesProperties.setDesKeyFixedLength()` 升为 public 并接入 `DesHelper.load(DesProperties)`——仅提供字符串密钥（无 `SecretKey` 对象）时自动按 UTF-8 原文构造密钥对象，补冒烟测试；`AesProperties.setAesKeyFixedLength()` 删除（raw 字节语义与 AES 全链路 Hex 密钥约定冲突，且无任何调用方）。
- `EncryptKeyGenerator` 并发切换算法修复：`getKeyGenerator` / `getKeyPairGenerator` 改为无状态工厂，每次调用返回新生成的实例——此前静态单例缓存的 check-then-act 竞态会导致并发切换算法时拿到错误算法的生成器，且共享的 JCA 生成器被并发 `init` 互相污染配置；同步将 Aes / Des / Idea 三个 KeyGenerator 的 `getSecretKey()` 改为每次调用独立创建生成器（删除静态共享实例与锁外 `generateKey`），整条密钥生成路径线程安全。新增并发切换算法回归测试。
- 跨算法复制粘贴文案 / 日志修正（无行为变化）：`RsaHelper` 禁用异常消息 "Aes enable: false" → "RSA enable: false"；`AesKeyGenerator` 类注释 "des密钥生成工具"、`AesProperties` 类注释 "des加密配置文件" 与 "密钥长度固定8bit"、`DesProperties` "密钥长度固定8bit"（实为 8 字节）等注释修正；`EncryptAutoConfiguration.aes()` 装载失败日志 "des error" → "aes error"；`IdeaUtils.decode` 日志中的 `ENCRYPT_MODE` → `DECRYPT_MODE`；`IdeaHelper.init` 参数名 `aes` → `idea`。

### Tests

- encrypt 模块测试改造为**冒烟测试**套件（遵循仓库"Xxx 冒烟测试"惯例）：覆盖 encrypt 全部公开方法（工具类 / Helper / KeyGenerator / `SymmetricalUtils` / `AsymmetricUtils` / `CipherUtils` / 算法枚举 / `EncryptKeyGenerator`），旧 println 风格无断言用例与临时 round-trip 用例已合并为按类组织的断言式冒烟测试；`SignatureUtilsConcurrencyTest` 作为并发补充保留。
- 修复 `ImageUtilsTest` 压缩用例无效输入问题：`TEST_IMAGE_BYTES` 原为仅含 PNG 魔数的 8 字节伪图片，导致 6 个 `compressPicByScale` 用例全部走异常分支、从未验证压缩功能；现改为程序生成的真实 PNG，并新增"压缩结果可被 `ImageIO` 解码"断言与"非法输入优雅失败（原样返回）"负向用例。`ImageUtils` 主代码行为正确（非法输入记录错误日志并返回原数组），未改动。

### Notes

- 本版本无公开 API 签名变更；上述 AES 密钥解析行为变更仅影响未发布的开发版本之间的升级。
- 文档中的已知安全限制（DES 默认 ECB、JWT 默认签名串等）以 [encrypt/README.md 安全边界表](encrypt/README.md#安全边界) 为准，行为修改将另行记录。
