# tools

**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

`io.gitee.sander-o:tools` 是一个可发布到 Maven Central 的 Java 工具库聚合工程（`packaging=pom`，版本 `2.0.0`）。定位为「以 encrypt 为旗舰的 Java 加密库（含配套工具）」：`encrypt` 是主入口，`bedrock`（基础工具）与 `fortify`（增强能力）为可选配套模块，`tools-spring-boot-starter` 提供 Spring Boot 自动装配。

所有模块统一以 **Java 8 字节码**（class major 52）发布，最低运行环境 JDK 8，需在 JDK 8 / 17 / 21 上可运行。

## Key Files

| File | Description |
|------|-------------|
| `pom.xml` | 根 POM：模块聚合、依赖版本统一管理（`dependencyManagement`）、GPG 签名与 Central 发布插件、`jdk9-cross-compile` profile |
| `README.md` | 中文主文档：能力矩阵、快速开始、项目结构、JDK 支持、安装教程 |
| `README.en.md` | 英文版，与中文版定位对齐 |
| `CHANGELOG.md` | Keep-a-Changelog 格式；`2.0.0` 为破坏性变更版本，含迁移指引 |
| `SECURITY.md` | 安全问题报告渠道 |
| `pendingMatters.md` | 手工维护的待办清单（各模块后续工作） |
| `LICENSE` | MIT |
| `mvnw` / `mvnw.cmd` | Maven Wrapper 脚本，但 `.mvn/wrapper/maven-wrapper.jar` **缺失**，`./mvnw` 无法运行；实际请用系统 Maven（本地实测 3.9.9） |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `bedrock/` | 配套模块：基础工具，纯 JDK 实现，零第三方依赖（见 `bedrock/AGENTS.md`） |
| `encrypt/` | ★ 旗舰模块：加密 / 摘要 / 签名 / JWT / 编码（见 `encrypt/AGENTS.md`） |
| `fortify/` | 配套模块：Excel、图像压缩、阿里云 DDNS、请求体复用过滤器、JSON（见 `fortify/AGENTS.md`） |
| `tools-spring-boot-starter/` | Spring Boot Starter 聚合 POM（见 `tools-spring-boot-starter/AGENTS.md`） |
| `example/` | 示例工程（**独立 Maven 工程，不在根 `<modules>` 内**，版本 `1.0.0`）（见 `example/AGENTS.md`） |
| `docs/` | 自包含 HTML 操作文档 `operations-guide.html`（见 `docs/AGENTS.md`） |
| `document/` | 早期 bedrock 目录说明 `BEDROCK-README.md`，内容已过时，权威说明以 `bedrock/README.md` 为准 |
| `local-repo/` | 随仓库分发的 Maven 仓库（file://），仅存 com.aliyun SDK 依赖闭包（25 个 jar），供受限构建环境直读 |
| `.github/workflows/ci.yml` | GitHub Actions 草案（仓库实际托管于 Gitee）；JDK 8/17/21 三腿矩阵 |

## For AI Agents

### Working In This Directory

- **模块依赖方向**：`encrypt → bedrock`，`fortify → bedrock`，`encrypt-spring-boot-starter → encrypt`。禁止反向依赖，禁止让 `bedrock` 引入第三方库（当前 bedrock 主代码零第三方 import，仅用 Lombok/slf4j）。
- **Java 8 兼容是硬约束**：不得使用 JDK 9+ API（`List.of`、`String.isBlank`、`var` 等）。在 JDK 9+ 上构建时 `jdk9-cross-compile` profile 会自动启用 `maven.compiler.release=8`，误用会直接编译失败；在 JDK 8 上构建则走 `source/target=1.8`，**不会**有这层保护，需自行注意。
- **依赖版本只在根 `pom.xml` 的 `dependencyManagement` 中定义**，模块 POM 内声明依赖时不写 `<version>`（`bcprov-jdk18on`、`alidns20150109` 例外，其版本就地声明）。
- 根 POM **不声明远程仓库**（唯一例外是 `local-repo/` 的 file:// 仓库）。不要在 POM 里硬编码阿里云等区域镜像。
- `logback` 被标记 `<optional>`，故意不向下游传递（JDK 8 线无 CVE 修复版）；本仓库自身编译测试仍用 1.2.13。
- 根 POM 的 GPG 配置硬编码了本机路径 `C:/Program Files/Git/usr/bin/gpg.exe` 与密钥指纹 `F8EEA10AA6FAB4BE14948C97A4915B1CAF3B8074`，只在发布时需要；改动发布配置前先确认不会影响普通构建。
- 代码注释、Javadoc、README、CHANGELOG 均为中文；新增公开类/方法应补 Javadoc（`@param`/`@return`/`@throws`），否则 javadoc 打包会告警。
- 每个 Java 包都有 `package-info.java` 描述包职责，新增包应补上。
- `example/` 不参与根构建，改动它不会影响主工程验证。

### Testing Requirements

根 POM 默认 `skipTests=true`（只编译测试类，不执行），**所有测试命令必须显式开启**：

```bash
# 全量编译 + 测试
mvn -B -DskipTests=false clean test

# 单模块
mvn -B -DskipTests=false -pl encrypt -am test

# 指定其它 JDK（Git Bash 写法）
JAVA_HOME=/path/to/jdk17 PATH=/path/to/jdk17/bin:$PATH mvn -B -DskipTests=false clean test
```

测试框架为 **JUnit 4**（`org.junit.Test` / `org.junit.Assert`）+ Mockito 4.11.0。现有 55 个测试类全部是 JUnit 4 风格冒烟/回归测试，新增测试沿用同一风格，不要引入 JUnit 5。

### Common Patterns

- 工具类一律 `final` 语义 + 私有构造器（`private XxxUtils() {}`），静态方法入口。
- 对称加密 Helper（`AesHelper`/`DesHelper`/`IdeaHelper`）采用**静态全局配置**：`load(...)` 替换进程内唯一激活配置；`load` 与 `encode/decode` 并发不安全，`encode/decode` 之间线程安全（每次新建 Cipher）。多组密钥用 `load(KeyPair, name)` 分组或显式传 `SecretKey` 重载。
- 加解密统一约定：明文 UTF-8，密文 Base64；异常统一包装为模块专属异常（`AesException`/`RsaException` 等）并保留异常链。
- HTTP 响应对象（bedrock `http/resp`）：`S` 接口 → `RS` 枚举（阿里《Java 开发手册》五字符错误码，`OK("0")` 为唯一成功码）→ `RR`（无数据）/ `R<T>`（带数据）。

## Dependencies

### Internal

模块层级见上；`example/` 依赖已发布的 `1.0.6` 版本坐标，不依赖本仓库源码。

### External

| 依赖 | 版本 | 用于 |
|---|---|---|
| Spring Boot | 2.7.18 | starter；同时导入 `spring-framework-bom` 5.3.39（须在 Boot BOM 之前） |
| BouncyCastle `bcprov-jdk18on` | 1.78.1 | encrypt（IDEA、RSA-OAEP） |
| java-jwt | 4.4.0 | encrypt JWT |
| fastjson2 | 2.0.53 | fortify（fastjson 1.x 已 EOL） |
| Apache POI | 5.2.5 | fortify Excel |
| Lombok | 1.18.34 | 全模块 |
| slf4j / logback | 1.7.36 / 1.2.13 | 日志（logback optional） |
| JUnit / Mockito | 4.13.2 / 4.11.0 | 测试 |

## Manual Notes

`@AGENTS.md` 层级依赖 OMC 的目录上下文注入钩子（PostToolUse）生效：从被访问文件向上查找最近的 `AGENTS.md`，每会话注入一次。若设置 `DISABLE_OMC=1` 或 `OMC_SKIP_HOOKS=post-tool-use`，嵌套文件不会被加载，只有根 `AGENTS.md` 与 `CLAUDE.md` 生效。
