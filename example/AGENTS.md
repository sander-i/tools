# example

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

使用示例工程。**独立的 Maven 工程**：有自己的 `groupId`/`version`（`io.gitee.sander-o:example:1.0.0`），**不在根 POM 的 `<modules>` 里**，根构建不会编译它。

示例依赖**已发布的旧版本坐标**（`<io.gitee.sander-o-version>1.0.6</io.gitee.sander-o-version>`、`<tools.version>1.0.6</tools.version>`），而不是本仓库源码，因此改动主工程代码后示例可能无法反映最新 API。

## Key Files

| File | Description |
|------|-------------|
| `pom.xml` | 聚合示例工程：Spring Boot 2.7.1 + Spring Cloud 2021.0.6（MyBatis-Plus 3.4.3.1 等多数依赖在子 POM 中被注释，未实际启用） |
| `example-bedrock/` | bedrock 用法示例（`ExampleLists`、`ExampleBaseUtils`） |
| `example-encrypt/` | encrypt 用法示例（`ExampleBase64Utils`） |
| `example-encrypt-spring-boot-starter/` | Spring Boot 接入示例：`ExampleEncryptApplication` + REST 控制器 + `application.yml` |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `example-bedrock/` | 基础工具调用演示，纯 `main` 方法 |
| `example-encrypt/` | 加密工具调用演示，纯 `main` 方法 |
| `example-encrypt-spring-boot-starter/` | 可运行的 Spring Boot 应用，含对称/非对称控制器与配置样例 |

## For AI Agents

### Working In This Directory

- **不要把它加进根 `<modules>`**，除非明确要让它参与主构建——它依赖旧版本坐标，加入后可能因 API 差异编译失败。
- 示例代码用 `main` 方法演示，无需测试；`example-encrypt-spring-boot-starter` 是唯一可启动的应用。
- `example-encrypt-spring-boot-starter/src/main/resources/application.yml` 里的 RSA 公私钥是**演示密钥**，DES 密钥 `12345678`、AES 密钥 `BeYourself` 同样仅作演示。不要把它们当成可用配置复制到生产。
- 若要更新示例以匹配 2.0.0 API，需同时改 `pom.xml` 中的版本属性（当前停留在 1.0.6）。
- 示例代码注释为中文，保持同样风格。

### Testing Requirements

示例工程无测试。如需验证可运行性：

```bash
cd example && mvn -B -q compile
# 或启动应用
cd example/example-encrypt-spring-boot-starter && mvn spring-boot:run
```

注意：示例依赖已发布构件，需要网络能解析 1.0.6 版本。

### Common Patterns

- 工具类示例：`main` 方法直接调用静态工具，`System.out.println` 输出结果。
- Spring Boot 示例：`@SpringBootApplication` + `@RestController`，配置通过 `application.yml` 的 `tools.encrypt.*` 注入。

## Dependencies

### Internal

已发布的 `bedrock` / `encrypt` / `encrypt-spring-boot-starter` **1.0.6**（非本仓库源码）。

### External

Spring Boot 2.7.1、Spring Cloud 2021.0.6、MyBatis-Plus 3.4.3.1、Lombok、JUnit 4.13.2。

## Manual Notes
