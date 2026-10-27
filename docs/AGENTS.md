# docs

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

自包含的 HTML 操作文档（约 24 KB，无外部依赖、无 CDN 引用），可在浏览器直接打开、离线分发或打印。内容与 `README.md`、`encrypt/README.md` 的能力矩阵和安全边界表保持一致。

## Key Files

| File | Description |
|------|-------------|
| `operations-guide.html` | 单文件操作手册：目录导航、能力矩阵、安全边界表、各能力可复制示例、Spring Boot 接入、FAQ、打印样式 |

## Subdirectories

无。

## For AI Agents

### Working In This Directory

- **改动加密行为或 API 时，此文件需要同步**。它与 `encrypt/README.md` 的安全边界表、根 `README.md` 的能力矩阵是同一份事实的三个副本。
- 保持**单文件自包含**：不引外部 CSS/JS/CDN，否则离线分发和 Gitee RAW 下载查看会失效。
- 中文内容，HTML 结构与现有章节锚点保持稳定（README 通过 `docs/operations-guide.html` 链接引用它，外部也可能直接链接锚点）。
- 保留打印样式（`@media print`）。

### Testing Requirements

无自动化测试。改完后用浏览器打开确认排版、锚点跳转与代码块可复制。

### Common Patterns

- 章节：文档导航 → 能力矩阵 → 安全边界 → 各能力示例 → Spring Boot 接入 → FAQ。
- 代码示例与 `encrypt/README.md` 的「最小示例」保持一致，避免两处说法冲突。

## Dependencies

### Internal

事实来源：`../README.md`、`../encrypt/README.md`、`../CHANGELOG.md`。

### External

无。

## Manual Notes
