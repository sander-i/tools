# encrypt-spring-boot-starter

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

为 `encrypt` 提供 Spring Boot 自动装配：读取 `application.yml` 的 `tools.encrypt.*` 配置，启动时自动调用 `AesHelper.load(...)` / `DesHelper.load(...)` / `IdeaHelper.load(...)` / `RsaHelper.load(...)`，并把各算法配置注册为 Bean。坐标 `io.gitee.sander-o.spring.boot:encrypt-spring-boot-starter`，适配 Spring Boot 2.7.x，Java 8+。

规模：6 个主代码文件，2 个测试类。

## Key Files

| File | Description |
|------|-------------|
| `README.md` | **权威文档**：5 分钟接入、完整 yml 示例、当前限制、FAQ |
| `pom.xml` | 依赖 `spring-boot-autoconfigure` + `encrypt`（版本由父 POM `dependencyManagement` 提供） |
| `src/main/java/.../autoconfiguration/EncryptAutoConfiguration.java` | 核心自动配置：各算法 `@Bean` + `@ConditionalOnProperty` + 版本 banner |
| `src/main/java/.../EncryptProperties.java` | `@ConfigurationProperties(prefix = "tools.encrypt")`，继承 encrypt 的 `Encrypt` POJO |
| `src/main/java/.../OnEncryptPropertiesCondition.java` | 条件：环境里存在任意 `tools.encrypt.` 前缀属性才装配 |
| `src/main/java/.../EnableToolsEncrypt.java` | 可选显式启用注解（`@Import(EncryptAutoConfiguration.class)`） |
| `src/main/resources/META-INF/spring.factories` | Boot 2.7 注册入口 |
| `src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` | 新版注册入口（两处同时保留，2.7 均能加载） |
| `src/main/resources/META-INF/tools-version.properties` | `version=@project.version@`，构建期 filtering 注入，供启动 banner 读取 |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `src/main/java/org/tools/spring/boot/encrypt/` | 配置绑定、条件、启用注解 |
| `src/main/java/org/tools/spring/boot/encrypt/autoconfiguration/` | 自动配置类 |
| `src/main/resources/META-INF/` | 注册元数据与版本属性 |
| `src/test/resources/` | 测试用 `application.yml`（演示密钥已从 main resources 移出） |

## For AI Agents

### Working In This Directory

- **启动类零注解即生效**：只要 classpath 有本 starter 且存在任意 `tools.encrypt.*` 配置，`EncryptAutoConfiguration` 就装配。`@EnableToolsEncrypt` 是可选的显式 `@Import`，**无配置时加了注解也不装配**（`OnEncryptPropertiesCondition` 会拦住）。这个双条件契约有测试固化（`EncryptAutoConfigurationSmokeTest`），改动时别破坏。
- **不要在 `src/main/resources` 下放 `application.yml`**。2.0.0 曾因此出过事故：旧版 jar 内的演示密钥被 Spring Boot 从依赖 jar 加载，导致无自配的应用被演示密钥意外激活。样例配置只放 `src/test/resources`。
- **配置绑定注解只在本模块**，`encrypt` 模块的实体是纯 POJO。不要把 `@ConfigurationProperties` 加回 encrypt 侧。
- **版本号不要写死**。banner 读 `META-INF/tools-version.properties`，该文件由 Maven resource filtering（根 POM `<delimiters><delimit>$</delimit>`）注入 `@project.version@`。新增资源文件需注意过滤规则。
- 两个自动配置注册文件要同步维护（`spring.factories` 与 `AutoConfiguration.imports`）。
- 不要手工添加 `spring-configuration-metadata.json` / `spring-autoconfigure-metadata.properties` / `MANIFEST.MF`——2.0.0 已删除手工版本，改由注解处理器生成。

### Testing Requirements

```bash
mvn -B -DskipTests=false -pl tools-spring-boot-starter/encrypt-spring-boot-starter -am test
```

JUnit 4。两个测试：`EncryptPropertiesBindingTest`（Binder 绑定）、`EncryptAutoConfigurationSmokeTest`（有配置装配 / 无配置零装配）。

### Common Patterns

- 条件化 Bean：`@ConditionalOnClass(Encrypt.class)` + `@Conditional(OnEncryptPropertiesCondition.class)` + 每算法 `@ConditionalOnProperty(prefix="tools.encrypt.xxx", value="enabled", havingValue="true")` + `@ConditionalOnMissingBean`。
- 配置校验：Bean 创建时先调 `checkEncrypt(...)` 检查配置完整性，不完整返回 null 并记日志。
- 启动 banner 打印各算法启用状态。

## Dependencies

### Internal

`encrypt`（及其传递的 `bedrock`）。

### External

`spring-boot-autoconfigure` 2.7.18（含 `spring-boot-configuration-processor`、`spring-boot-autoconfigure-processor`，均 optional）。

## Manual Notes
