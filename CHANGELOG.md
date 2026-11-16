# 更新日志

本文档记录项目的重要变更。

## [2.0.0-SNAPSHOT] - 进行中

### 新增
- 图像工具模块全面优化
- 新增 `ThumbnailUtil` 缩略图生成工具
- 新增图像常量类 `ImageConstants` 和 `FileUtilConstants`
- 新增开源合规文档（CONTRIBUTING、AUTHORS、NOTICE、CHANGELOG、SECURITY、CODE_OF_CONDUCT）
- 新增 GitHub Issue 与 Pull Request 模板
- 加密模块新增 8 个算法实现：
  - 3DES（`Des3Helper`、`Des3KeyGenerator`）
  - Blowfish（`BlowfishHelper`、`BlowfishKeyGenerator`）
  - RC2（`Rc2Helper`、`Rc2KeyGenerator`）
  - RC4 流加密（`Rc4Helper`、`Rc4KeyGenerator`）
  - ECC 椭圆曲线与 ECDH 密钥协商（`EccKeyGenerator`、`EcdhHelper`）
  - DH 密钥交换（`DhHelper`）
  - ECDSA 数字签名（`EcdsaHelper`、`EcdsaKeyGenerator`，支持 SHA256/384/512withECDSA）
  - SHA-384 / SHA-512 便捷方法（`ShaUtils.encodeSha384`、`encodeSha512`）

### 变更
- **破坏性变更**：Java 包根从 `org.tools` 迁移到 `io.github.sander_i`
- **破坏性变更**：Maven groupId 从 `io.gitee.sander-o` 迁移到 `io.github.sander-i`
- 统一图像工具到 `bedrock` 模块
- 精简 `fortify` 模块的 `ImageUtils`（-472行）
- 增强 `bedrock` 模块的 `ImageUtil`（+441行）
- 优化图像特征、哈希、直方图、像素、结构工具类
- 提升测试覆盖率（ThumbnailUtilTest +244行，ImageUtilTest +165行）
- 主仓库迁移到 GitHub，Gitee 保留为镜像

### 修复
- `example` 模块 `SymmetricalController.desLoad()` 返回类型不匹配
- `SymmetricalUtils` 解密未指定字符集，非 ASCII 明文（如中文）解密后乱码
- `SymmetricalUtils` 非法 Base64 密文未包装为 `SymmetricalException`，直接抛 `IllegalArgumentException`

### 构建
- 使用 build-helper-maven-plugin 自动生成构建年份
- 版权信息动态化：`Copyright © 2023-${build.year} sander`
- 移除已废弃的 OSSRH staging 配置

### 安全
- 所有提交与标签邮箱更换为 GitHub Noreply 地址
- 清理 git 历史中的明文凭据

---

## [1.0.6.6] - 2025-11-04

### 新增
- BigDecimalUtils 工具类
- HttpResponse 对象调整
- 维护中断期间新功能合并

### 变更
- JsonUtils 优化
- 加密工具实现优化
- RSA 算法安全升级
- ddns 工具优化

---

## [1.0.6.5] - 2025-08-20

### 新增
- Assert 断言工具
- 冒烟测试补充
- 集合工具优化

### 变更
- javadoc 异常处理
- 过期对象处理
- 基础异常对象继承关系变更
- 加密工具注解启动支持

---

## [1.0.6.4] - 2025-02-04

### 新增
- 断言工具、冒烟测试
- FeatureUtil 特性工具

### 变更
- SpecialSymbolConst 调整
- 加密工具实现注解启动
- 基础异常对象继承对象变更

---

## [1.0.6.3] - 2024-08-20

### 新增
- FeatureUtil（特性）工具
- DateUtils 工具优化
- Maps 工具优化

### 变更
- R 对象无参构造方法 bug 修复
- 日期、集合工具类优化

---

## [1.0.6.2] - 2024-07-20

### 新增
- bedrock-resp 对象优化
- 1.0.6.2 版本升级

### 变更
- 响应对象结构优化
- 版本管理规范化

---

## [1.0.6] - 2024-06-20

### 新增
- CopyUtil 工具优化
- BaseUtils 工具优化
- 1.0.6 版本升级

---

## [1.0.5] - 2024-05-20

### 新增
- encrypt 模块升级
- BaseUtils 工具优化
- README 文件 groupId 错误修复

---

## [1.0.4] - 2024-04-20

### 新增
- CopyUtil 工具优化
- 1.0.4 版本升级

---

## [1.0.3] - 2023-XX-XX

### 新增
- DateUtil 工具新增功能
- 项目结构调整
- 1.0.3 版本升级

---

## [1.0.2] - 2023-XX-XX

### 新增
- 项目结构调整
- 1.0.2 版本升级

---

## [1.0.1] - 2023-XX-XX

### 新增
- 基础工具类
- 1.0.1 版本升级

---

## [1.0.0] - 2023-XX-XX

### 新增
- 项目初始化
- 基础模块：bedrock、encrypt、fortify、tools-spring-boot-starter
- 常用工具类实现

---

## 版本说明

本项目遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范：

- **主版本号** (X.0.0): 不兼容的 API 修改
- **次版本号** (1.X.0): 向下兼容的功能性新增
- **修订号** (1.0.X): 向下兼容的问题修正

SNAPSHOT 版本表示开发中的不稳定版本，随时可能更改。
