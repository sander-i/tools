# bedrock

**Parent context:** `../AGENTS.md`
**Generated:** 2026-10-27 · **Updated:** 2026-10-27

## Purpose

配套基础模块：集合 / 映射 / 日期 / 文件 / 图像工具、HTTP 统一响应与分页、分布式 ID、基础实体与枚举、异常体系、MySQL 元数据常量。坐标 `io.gitee.sander-o:bedrock`。

**关键约束：主代码零第三方依赖**。仅使用 JDK 标准库 + Lombok + slf4j（`JsonUtil` 是手写的轻量 JSON，不引 fastjson/jackson）。新增功能不得引入第三方库——这是本模块作为 `encrypt` 传递依赖时保持轻量的前提。

规模：`src/main/java` 共 93 个 Java 文件（其中 68 个类/枚举，其余为 `package-info.java`），26 个测试类。

## Key Files

| File | Description |
|------|-------------|
| `pom.xml` | 只继承父 POM，无自身依赖声明 |
| `README.md` | 模块说明与示例（版本号随根 POM） |
| `src/main/java/org/tools/bedrock/util/BaseUtils.java` | 判空工具，几乎所有其他类都依赖它 |
| `src/main/java/org/tools/bedrock/util/date/DateUtils.java` | 最大的工具类（约 542 行），日期格式化 / 解析 / 比较 |
| `src/main/java/org/tools/bedrock/http/resp/RS.java` | 响应状态枚举，错误码规范来源 |
| `src/main/java/org/tools/bedrock/base/BaseBean.java` | 通用实体基类（审计字段 `creator/createdAt/updater/updatedAt`，时间用 `java.sql.Timestamp`） |

## Subdirectories

| Directory | Purpose |
|-----------|---------|
| `annotations/` | `@Inherit` 可继承标记注解 |
| `base/` | 基础实体与枚举：`BaseBean`、`Address`、`Identity`、`ExpiryDate`、`ExcelSheet`、`Sex`、`Nation`、`TimeSeason` |
| `base/image/` | 图像基础对象：`Point`、`SimilarityConfig`、`SimilarityResult`、`ImageSimilarityResult` |
| `base/serializable/` | 与 `base/` 同名对象的 JDK 序列化变体（字段结构一致，仅实现 `Serializable` + `serialVersionUID`）；枚举不重复提供 |
| `constant/` | `Constants`、`SpecialSymbolConsts`（旧版） |
| `constant/v2/` | `SpecialSymbolConst` 枚举形式的符号常量（新版，优先使用） |
| `database/mysql/` | `information_schema` 的 COLUMNS / SCHEMATA / TABLES 字段名常量 |
| `database/oracle/`、`database/sqlserver/` | 预留，仅有 package-info |
| `definer/`、`factory/` | 预留，仅有 package-info |
| `exception/` | `BaseException` → `BusinessException` / `ToolsException`；`ErrorEnum` |
| `http/` | `HttpConsts`、`HttpEnum`、`HttpResult`、`Paging`；`http/mysql`、`http/oracle` 为分页方言 |
| `http/resp/` | 统一响应体：`S` 接口、`RS` 枚举、`RR`（无数据）、`R<T>`（带数据） |
| `uniqueid/` | `snowflake/SnowflakeUtils`、`tddl/TddlUtils`、`uuid/UniqueUtils` |
| `util/` | 通用工具（见下） |
| `util/date/` | 日期时间工具与线程安全 DateFormat |
| `util/file/` | `FileUtil`；`util/file/image/` 图像算法（哈希、直方图、SSIM、特征、相似度融合） |
| `util/network/` | `HttpUtils`（HttpURLConnection）、`NetworkUtils`（域名/IP 查询） |

## For AI Agents

### Working In This Directory

- **不要引入第三方依赖**（含测试范围之外的任何 lib）。判空用 `BaseUtils`，集合操作自己写。
- **`base/` 与 `base/serializable/` 必须保持字段同步**。修改 `BaseBean`/`Address`/`Identity`/`ExpiryDate` 的字段时，两个包同时改；只读工具类用非序列化版，RPC/缓存用序列化版。
- `Nation` 枚举编码遵循 **GB/T 3304**（01 汉 … 56 基诺），与身份证民族字段同源。旧代码若用「旧编码 = 新编码 − 1、汉族 56」的映射，迁移时需转换。
- `RR`/`R` 的 `message` 合并规则：自定义提示非空则覆盖，为空回退 `S.getMsg()`；`success` 仅当状态为 `RS.OK` 时 true。`R<T>` 的泛型约束 `T extends Serializable` 只校验顶层类型，嵌套字段图不可序列化仍会在运行期失败。
- 图像工具（`util/file/image/`）依赖 `java.awt`/`javax.imageio`，在无头环境需 `-Djava.awt.headless=true`。
- 新增包必须补 `package-info.java`（全模块每个包都有）。
- 预留包（`database/oracle`、`database/sqlserver`、`definer`、`factory`）只有 package-info，不要误以为有实现。

### Testing Requirements

```bash
mvn -B -DskipTests=false -pl bedrock test
```

JUnit 4。测试多为冒烟测试（如 `ListsTest`、`DateUtilsTest`），图像测试使用 `src/test/resources/static/` 下的真实 JPG 图片。

### Common Patterns

- 工具类：私有构造器 + 静态方法。
- 线程安全 DateFormat：`ThreadSafeDateFormat` 基类用 `ThreadLocal` 持有实例，`ThreadSafeSimpleDateFormat` 是其实现。
- 分页：`Paging.initPaging` 做通用计算（越界裁剪），`MysqlPaging`/`OraclePaging` 只负责方言 SQL 片段。
- 雪花 ID：1+41+10+12 位；时钟回拨有等待/拒绝两种策略；支持 workerId/dataCenterId 反解析。
- TDDL ID：17 位毫秒时间戳 + 2 位机器标识 + 3 位序列（共 22 位），同机器标识共享序列状态。

## Dependencies

### Internal

无（本模块是依赖树的叶子）。

### External

仅 Lombok（provided/optional 语义）与 slf4j-api。测试：JUnit 4、Mockito。

## Manual Notes
