# Changelog

本项目所有对外可见的变更记录于此。格式遵循 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)。

## [Unreleased]

## [2.1.0] - 2026-10-31

### Added

- **encrypt 新增 9 个算法实现**：对称加密 `BlowfishHelper` / `Des3Helper`（3DES，位于 `symmetrical.des.des3`）/ `Rc2Helper` / `Rc4Helper` / `Rc5Helper` / `SkipjackHelper`，非对称密钥协商 `DhHelper`（DH）与 `EccHelper`（ECDH），数字签名 `EcdsaHelper`（ECDSA），各含对应 `KeyGenerator`、`Properties`、算法专属异常类与 JUnit 4 冒烟测试（round-trip + 已知答案断言）。`des3` 与 `tdea` 合并为同一实现（`tdea` 仅保留包别名说明，无重复类）；DH/ECC 只做密钥协商、不提供 `encode`/`decode`，原始共享秘密须经 HKDF/SHA-256 派生后才能作对称密钥；ElGamal / Knapsack / Rabin 仍为包占位。能力矩阵、安全边界表、`encrypt/README.md`、`encrypt/AGENTS.md` 与 HTML 操作文档同步更新（Blowfish/3DES/RC2/RC4/RC5/Skipjack 标注 legacy，不推荐用于新设计）。关键参数约定：RC5 固定 version 1.0（16）/ 12 轮 / 32 位字（`RC5ParameterSpec(16, 12, 32)`，与标准实现互操作）；RC2 密钥 40~1024 位、ECB 与 CBC 均按密钥长度计算 effective key bits；3DES 仅支持 `DESede/CBC/PKCS5Padding`；Blowfish 密钥位数须为 8 的倍数；DH/ECDH 与 ECDSA 的公私钥解析统一使用 `Base64.decodeBase64(String)`。
- **bedrock 新增 `TimeZoneUtils.convertTimeZone*` 方法**：修正历史拼写错误（`covert` → `convert`），共 8 个方法（`convertTimeZoneStr` / `convertTimeZone` / `convertTimeZoneUTCStr` / `convertTimeZoneUTC` 各 2 个重载）。旧 `covertTimeZone*` 方法保留并标记 `@Deprecated`，委托到新方法，二进制兼容。

### Changed

- **对称 Helper 的异常类型细化**：`AesHelper` / `DesHelper` / `BlowfishHelper` / `Des3Helper` / `Rc2Helper` / `Rc4Helper` / `Rc5Helper` / `SkipjackHelper` 的 `encode` / `decode` 在 JCE 失败或密文非法 Base64 时改抛算法专属异常（如 `Rc2Exception`），与既有 `throws` 声明一致；此前实际抛父类 `SymmetricalException`。新异常仍继承 `SymmetricalException`，`catch (SymmetricalException)` 不受影响，异常链保留原始 JCE cause。
- **`Assert.isNotNull(Object)` 异常契约统一**：对象为 null 时由 `BusinessException` 改为 `IllegalArgumentException`（消息「对象不能为空」），与 `isNotNull(Object, String, Object...)` 重载保持一致。库内无调用方受影响。

### Fixed

- **encrypt 配置装载与密钥解析的边界修复**：对称 Helper 的 `load(KeyPair)` 不再用 null 覆盖默认算法/模式/IV/位数（裸 `KeyPair` 也能按默认配置工作）；`load(XxxProperties)` 增加 `algorithmMode` 合法性校验，非法模式在装载阶段即抛算法异常（此前延迟到 `encode` 抛 `NoSuchElementException`）；RC2/RC4/RC5 的字符串密钥在解析阶段校验长度范围；ECDSA 的 `load(KeyPair)` 补公私钥非空预检、`signAlgorithm` 为空时抛 `EcdsaException`。
- **encrypt 消除 JVM 默认字符集依赖**：`RsaKeyGenerator` / `DsaKeyGenerator` 的公私钥 Base64 解码由 `Base64.decodeBase64(str.getBytes())` 改为 `Base64.decodeBase64(str)`；`SignatureUtils` 的验签 Base64 解码同样调整，签名输出由 `new String(Base64.encodeBase64(...))` 改为 `Base64.encodeBase64String(...)`。在默认字符集非 ASCII 兼容的平台（EBCDIC / UTF-16）上，此前会解析出损坏的密钥或签名，现统一按 UTF-8/ASCII 处理。无行为变更（ASCII 平台结果一致）。
- **encrypt 日志不再输出密钥材料**：`SymmetricalUtils` 与 `IdeaUtils` 的 `encode` / `decode` 失败日志删除 `secretKey`、`ivParameter` 与明/密文字节数组，仅保留算法、模式与异常堆栈。
- **bedrock `HttpUtilsTest` 改为本地可运行**：4 个用例原本依赖 `https://httpbin.org`，离线与 CI 环境必败；现改用 JDK 内置 `com.sun.net.httpserver.HttpServer` 随机端口本地回环，覆盖 GET、带参 GET、Map POST 与字符串 POST。

## [2.0.0] - 2026-10-25

> **本版本为破坏性变更版本（2.0）**：HTTP 响应体结构与错误码重构、基础对象字段更名与类型调整、民族编码对齐国标、encrypt 移除 Spring 依赖。升级前请先阅读文末"迁移指引"。

### Added

- **JDK 多版本支持（8 / 17 / 21）**：产物统一保持 Java 8 字节码（class major 52），最低运行环境 JDK 8；CI 验证矩阵扩展为 temurin 8/17/21 三腿全量构建 + 单元测试（此前仅 JDK 8）。中英文 README 新增「JDK 版本支持」章节（模块 × 字节码目标 × 验证版本矩阵、Spring 5.3.39 于 JDK 21 的支持说明）。新增自动激活 profile `jdk9-cross-compile`（构建 JDK ≥ 9 时设 `maven.compiler.release=8`）：交叉编译静态禁止误用 JDK 9+ API（如 `String.isBlank`、`List.of`），保证任何环境构建的产物均可运行于 Java 8；JDK 8 上构建不激活，维持 `source/target=1.8` 既有路径。已本地验证 JDK 8 与 corretto 17 全量测试通过、产物字节码均为 major 52、profile 按 JDK 正确激活/不激活（JDK 21 由 CI 矩阵覆盖）。
- **HTTP 统一响应体新增字段**：`RR` 增加 `timestamp`（epoch 毫秒，构造时自动填充）与 `traceId`（链路追踪 ID，调用方按需注入）。
- **starter 配置绑定承接与测试固化**：新增 `EncryptProperties`（继承 encrypt 的 `Encrypt` 实体树，持有 `@ConfigurationProperties("tools.encrypt")`）；新增 `EncryptPropertiesBindingTest`（Binder 绑定冒烟）与 `EncryptAutoConfigurationSmokeTest`（有配置自动装配 / 无配置零装配的启用契约测试）。
- **starter 启动 banner 版本号动态注入**：`printWelcome` 不再写死版本号，改为读取 `META-INF/tools-version.properties`（构建期经 Maven filtering 注入 `@project.version@`），后续升版本只需改根 POM。
- 新增 `BaseBeanSmokeTest`（字段访问器、toString 回归、serializable 版 JDK round-trip 含 Timestamp 纳秒断言）；`RSerializationSmokeTest` 扩充 message 合并语义与 timestamp 断言。
- 全仓库代码风格统一：类字段块之间、枚举常量之间统一空行分隔。
- 新增唯一键工具（bedrock `uniqueid` 包，纯 JDK 实现，基础包不引入任何第三方依赖）：`SnowflakeUtils` 雪花 ID（1+41+10+12 位，时钟回拨等待/拒绝策略，支持 workerId/dataCenterId 反解析）与 `TddlUtils` TDDL 风格字符串主键（17 位毫秒时间戳 + 2 位机器标识 + 3 位序列，共 22 位，同一机器标识共享序列状态）。均附并发唯一性冒烟测试；`uniqueid` 各 package-info 补充包描述。
- 文档导航新增 Gitee 在线 API 文档入口（https://apidoc.gitee.com/sander-o/tools），同步更新中英文 README 与 HTML 操作文档。
- 全项目公开类/方法 Javadoc 补全：扫描 bedrock / encrypt / fortify / starter 全部公开方法，为缺失描述的 182 个公开方法（响应构建工厂、分页、HTTP 工具、日期时区、加密 Helper 重载、枚举查找、过滤器、Excel 填充、自动配置 Bean 等）补充中文 Javadoc；含块注释内示例代码的扫描排除与"注释位于注解之上"的规范修正。无任何行为变更。
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

- **依赖漏洞修复升级**（全部保持 JDK 8 兼容）：logback `1.1.7→1.2.13`（CVE-2017-5929 / CVE-2021-42550 / CVE-2023-6378）、slf4j `1.7.21→1.7.36`（1.7 线终版）、BouncyCastle `bcprov-jdk15on 1.70 → bcprov-jdk18on 1.78.1`（CVE-2023-33201 / CVE-2024-29857 / CVE-2024-30171 / CVE-2024-30172；1.71 起构件更名 jdk18on、包名不变，加密代码零改动），并**排除 aliyun SDK 传递的 jdk15on 1.65**（openapiutil 经 alidns20150109 与 tea-openapi 两条路径引入，统一排除后由 jdk18on 1.78.1 提供同包名类）、java-jwt `4.3.0→4.4.0`（传递的 jackson-databind 由存在 CVE-2022-42003/42004 的 2.13.3 升至安全版本）、spring-boot-dependencies `2.7.1→2.7.18`（2.7 线终版，修复 spring-framework / tomcat / jackson 多项传递 CVE）、POI `5.2.2→5.2.5`（5.2 线安全终版）。
- **logback 不再向下游传递（optional）**：CVE-2024-12798 / CVE-2024-12801 的官方修复版 logback 1.5.14 要求 Java 11，JDK 8 的 1.2 线（止于 1.2.13）无修复版。本库代码零直接引用 logback（全部经 slf4j），故 logback-core / logback-classic 标记 `<optional>`——发布构件不再携带日志后端，由消费方选择自己的（已修复）版本；本仓库自身编译与测试仍使用 1.2.13。**升级提示**：下游若依赖本库传递获得 logback，升级后需自行声明日志后端。
- **fortify 依赖面收敛**（漏洞排查发现的两处无谓暴露）：① `spring-context` 依赖移除——全模块（含测试）零 `org.springframework` 引用，纯历史残留，连带清掉传递的 spring-core/aop/beans/expression；② `tomcat-embed-core`（9.0.83，存在 CVE-2024-50379/56337、CVE-2025-24813 等）移除——BodyReader 系列 Filter/Wrapper 仅使用 `javax.servlet` API，改以规范方式声明 `jakarta.servlet:jakarta.servlet-api:4.0.4`（provided，版本随 Boot BOM，运行期由消费方容器提供），不再把整个 Tomcat 传递给下游。
- **spring-framework 传递版本 5.3.31 → 5.3.39**（5.3 线终版）：starter 经 spring-boot 2.7.18 传递的 spring-core/context 升至已修复版本。实现方式为在 Boot BOM **之前**导入 `spring-framework-bom:5.3.39`——BOM import 场景下属性覆盖（`<spring-framework.version>`）不生效，那是 starter-parent 继承机制专属。
- **fastjson `1.2.83 → fastjson2 `2.0.53`**（fortify 模块）：fastjson 1.x 已 EOL，整体迁移至 fastjson2。代码适配：import 由 `com.alibaba.fastjson` 切换为 `com.alibaba.fastjson2`；`JSONUtils.jsonToObj(String, Object)` 中 fastjson1 独有的 `JSONObject.getInnerMap()` 改为直接使用 JSONObject 本身（fastjson2 的 JSONObject 已继承 LinkedHashMap）。**公开 API 类型变更**：`JSONUtils` 对外返回的 `JSONObject` 由 fastjson 类型切换为 fastjson2 类型。
- **【破坏性】HTTP 统一响应体重构（bedrock `http/resp`）**：`msg` 与 `message` 双字段合并为单一 `message`（自定义提示非空则覆盖、为空回退状态定义标准提示）；`RS` 错误码对齐阿里巴巴《Java 开发手册》五字符规范——`OK("0")` 保留为唯一成功码，原 `1001-1007` 数字码全部废弃（`FAIL→B0001`、`ARGUMENT_ILLEGAL→A0400`、`ARGUMENT_ABSENCE→A0410`、`NO_PERMISSION→A0301`、`DATA_DUPLICATE→C0341`、`GET_LOCK_ERROR→B0900` 自定义扩展），新增 `CLIENT_ERROR A0001`、`THIRD_PARTY_ERROR C0001`、`NOT_LOGIN A0200`、`LOGIN_EXPIRED A0230`、`RATE_LIMIT A0501`、`SERVICE_TIMEOUT B0100`、`DB_ERROR C0300`；`RR` 新增 protected 三参构造供子类使用。
- **【破坏性】base 基础对象规范化**：`Nation` 民族编码对齐 GB/T 3304（01 汉 … 56 基诺，与身份证民族字段同源；旧编码 = 新编码 − 1、汉族由 56 改为 1；拼写修正 `SALTAR→DONGXIANG`、`GINO→JINUO`）；`Sex` 删除同值重复条目 `MAN`/`GIRL`；`BaseBean` 审计字段更名 `createdBy→creator`、`createdTime→createdAt`、`updatedBy→updater`、`updatedTime→updatedAt`，时间类型统一为 `java.sql.Timestamp`（与时区无关、JDBC 原生互转），删除丢失 `remark` 的手写 `toString` 交给 Lombok；`Identity.birth` 由 `Date` 改为 `LocalDate`；serializable 孪生包同步。
- **【破坏性】`DateType` 枚举删除**：其常量与 `java.util.Calendar` 完全一致属冗余中间层，`DateUtils.addDate` 签名改为 `(Date source, int type, int val)` 直接收 Calendar 字段常量（顺带修复 `AM_PM` 原先无 switch 分支必抛异常的问题）。
- **【破坏性】encrypt / encrypt-spring-boot-starter 职责边界整改**：`encrypt` 移除 `spring-boot-autoconfigure` 依赖（`dependency:tree` 零 Spring，非 Spring 项目可干净引入）；7 个实体类（`Encrypt`/`Asymmetric`/`Rsa`/`Symmetrical`/`Des`/`Aes`/`Idea`）移除 `@ConfigurationProperties` 回归纯 POJO；`CipherAbst` 的 `org.springframework.util.Assert` 换为 JDK `IllegalArgumentException` 校验（异常类型不变）；starter 侧以 `EncryptProperties` 承接配置绑定（`@ConfigurationPropertiesScan` 改为 `@EnableConfigurationProperties`）；**starter 不再打包 `application.yml`**（旧版 jar 内的演示密钥配置会被 Spring Boot 从依赖 jar 加载，无自配的应用将被演示密钥意外激活加密；样例移至 `src/test/resources`）；删除冒名 `spring.boot.autoconfigure` 的手工 `MANIFEST.MF`、空的 `spring-autoconfigure-metadata.properties` 与空 `{}` 的手工 `spring-configuration-metadata.json`（均由构建期处理器生成正式版本）、零引用的 `EncryptScanPackage`。
- 启用契约明确：引入 starter + 存在任意 `tools.encrypt.*` 配置即自动装配（启动类零注解）；`@EnableToolsEncrypt` 为可选显式 `@Import`，无配置时加了注解也不装配。
- `ClassUtils.fetchField` 对缺 getter/setter 字段（如 final 只读属性）的预期跳过由 WARN+堆栈 降为单行 debug；`TransformUtils.strToByte` 非法字符集由 ERROR 降为 WARN（均为日志观测面调整，无行为变化）。
- `DateUtils.getSeasonDiffer` 由恒返 `STANDARD_TIME` 的桩实现改为按 `inDaylightTime` 实时判定；对应测试由日期敏感断言改为规则断言。
- `SimilarityResult.toString` 增加 `individualSimilarities` 判空；serializable/`Address` 内部类 `Area`/`County` 由 private 改 public（getter 暴露私有类型无法引用）；修复 `Paging` 与 serializable/`Identity` 的 Javadoc 坏链接（javadoc 打包校验通过）。
- 项目叙事调整：根 README 从"综合工具箱"改为"以 encrypt 为旗舰的 Java 加密库（含配套工具）"，`bedrock`、`fortify` 降级为配套模块。
- **AES 密钥解析行为变更（未发布版本内的变更）**：`AesKeyGenerator.getSecretKey(String, String)` 由"密钥字符串直接取 UTF-8 字节"改为"按 Hex 解码"，与 `getSecretKeyStr` 的 Hex 输出对应。在此前开发版本中使用明文 AES 密钥的调用方升级后需改用 Hex 密钥。
- `ClassUtils.fetchField`：跳过 `static` 字段与 `serialVersionUID`（此前静态字段会导致 `PropertyDescriptor` 构造失败被吞日志），属性复制行为更稳定。
- 构建：父 POM 固定 `com.aliyun:tea` 版本为 1.4.2，避免版本范围解析；`central-publishing-maven-plugin` 升级至 0.7.0；`autoPublish` 改为 false；GPG 签名配置调整。
- 构建：surefire 的 `skipTests` 由硬编码 `true` 改为属性引用（默认值仍为 `true`，行为不变），支持 `-DskipTests=false` 临时执行测试（CI 使用）。
- 构建：为支持 JDK 17/21 构建与测试，升级构建链——lombok 1.18.24 → 1.18.34（1.18.30 起才支持 JDK 21 注解处理）、mockito 3.12.4 → 4.11.0（仍要求 Java 8，完整支持 17/21；主代码无直接 mockito 用法变更）、byte-buddy 1.11.22 → 1.14.9（主代码零直接引用，仅测试链消费）、maven-compiler-plugin 3.8.1 → 3.13.0。JDK 8 上全量测试经升级后验证通过。

### Removed

- 移除 `RS.SUCCESS("1000")`（命名成功语义但 `build` 只认 `RS.OK`，行为矛盾）与 `RS.ERROR`（语义被 `FAIL/B0001` 覆盖）；移除 `Sex` 重复条目 `MAN`/`GIRL`；移除 `DateType` 枚举；移除 `EncryptScanPackage`、starter 手工 `MANIFEST.MF`、空 metadata 文件与 jar 内 `application.yml`（详见 Changed）。
- 根 `pom.xml` 移除私有 Nexus 仓库（ailuoli）的 repository / pluginRepository 及相关注释残留。

### Removed

- 移除全部 `@Deprecated` 类与枚举值（引用同步清理，无 breaking 影响确认）：`bedrock/util/JsonUtils`（及其测试，手写 JSON 拼接实现）、`bedrock/http/resp/v2/R`（连同空的 `resp/v2` 包）、`encrypt/CipherUtils`（及其测试，底层执行器已由 `XxxHelper` + `SymmetricalUtils`/`AsymmetricUtils` 覆盖）、`AlgorithmModeEnum.RSA_ECB_NO_PADDING`（弱模式，实体注释同步清理，`RsaProperties.recalcMaxBlocks` 与 `CipherSupply` 对应分支移除）。

### Fixed

- **http 基础对象规范修复**（含 2 个功能缺陷）：① `RR.fail()` / `R.ok()` / `R.fail()` 此前返回全空或 success=true 的对象（`RR.fail` 传入 `success=true`），已修正；`build(S)` / `R.build(S, ...)` 不再硬编码 `success=true`，按 code 是否匹配 `RS.OK` 推导，并新增 `RR.build(S, boolean)` 显式重载。② `HttpResult.data` / `R.data` 此前标记 `transient`，JDK 序列化会丢失业务数据，已移除。③ 分页计算重复代码上提至 `Paging.initPaging` 基类（子类仅保留方言差异的 `getEndRecord`），`pageSize<=0` 由算术异常改为明确的 `IllegalArgumentException`；删除 `OraclePaging.main()` 演示入口与冗余 `@ToString`。

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

- **迁移指引（破坏性变更对照）**：
  - 错误码：`1001→B0001`、`1003→A0410`、`1004→A0400`、`1005→C0341`、`1006→A0301`、`1007→B0900`；`SUCCESS(1000)`、`ERROR(1002)` 已删除。前端/调用方按旧码对接的需同步映射表。
  - 响应体：JSON 中不再有 `msg` 字段，统一读 `message`；新增 `timestamp`/`traceId` 字段（多余字段前端可忽略）。
  - `BaseBean` 子类：审计字段 getter/setter 相应更名（如 `getCreatedBy()→getCreator()`）；老版本序列化流与新类不兼容（serialVersionUID 已更新）。
  - `Nation`：旧码 = 新码 − 1（汉族 56→1），按旧码持久化的数据需迁移。
  - `DateUtils.addDate`：`DateType.X` 改传 `Calendar.X`。
  - encrypt 下游：不再传递 Spring 依赖；曾自行扫描 `org.tools.encrypt.entity` 或注入实体 bean 的，改注入 starter 的 `EncryptProperties`。
  - starter 下游：必须在自己的 `application.yml` 显式配置 `tools.encrypt.*`（不再能"继承"starter 内置演示配置——那本就是事故行为）。
- 文档中的已知安全限制（DES 默认 ECB、JWT 默认签名串等）以 [encrypt/README.md 安全边界表](encrypt/README.md#安全边界) 为准，行为修改将另行记录。
