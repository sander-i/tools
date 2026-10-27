# tools-spring-boot-starter

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

Spring Boot Starter 聚合工程（`packaging=pom`，`groupId=io.gitee.sander-o.spring.boot`）。当前只含一个子模块 `encrypt-spring-boot-starter`。注意 `groupId` 与主工程 `io.gitee.sander-o` **不同**。

`tools-spring-boot-starter` 自身只做聚合与 `dependencyManagement`（管理 bedrock / encrypt / encrypt-spring-boot-starter 的版本）；`fortify-spring-boot-starter` 目录下只有 `fortify-spring-boot-starter.iml` 与 `target/`，**无源码、无 pom.xml**，是历史遗留的空目录，未在 `<modules>` 中。

## Key Files

| File | Description |
|------|-------------|
| `pom.xml` | 聚合 POM，`<modules>` 仅 `encrypt-spring-boot-starter`，`dependencyManagement` 固定三个本工程构件版本 |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `encrypt-spring-boot-starter/` | 加密自动装配（见 `encrypt-spring-boot-starter/AGENTS.md`） |
| `fortify-spring-boot-starter/` | 仅残留 `fortify-spring-boot-starter.iml` 与 `target/`，无源码无 POM，未在 `<modules>` 中 |

## For AI Agents

### Working In This Directory

- 新增 starter 时：加目录 + 在 `<modules>` 注册 + 在 `dependencyManagement` 补条目，三处都要改。
- `groupId` 是 `io.gitee.sander-o.spring.boot`，不是 `io.gitee.sander-o`；引用本模块坐标时不要写错。
- 版本随根 POM（`${project.version}`），不要在子模块硬编码版本号。

### Testing Requirements

聚合 POM 本身无代码。验证请到子模块：

```bash
mvn -B -DskipTests=false -pl tools-spring-boot-starter/encrypt-spring-boot-starter -am test
```

### Common Patterns

聚合 POM 不声明依赖，只做版本管理与模块聚合。

## Dependencies

### Internal

`encrypt`、`bedrock`（经 `dependencyManagement` 统一版本）。

### External

Spring Boot 2.7.18（经子模块传递）。

## Manual Notes
