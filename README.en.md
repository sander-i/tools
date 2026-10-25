# tools — A Verified Java Cryptography Library with `encrypt` as the Flagship

A verified, adopt-on-demand Java cryptography library (with companion utilities): symmetric / asymmetric encryption, digests, signatures and JWT out of the box, or one-step integration via a Spring Boot Starter. `bedrock` (basic utilities) and `fortify` (enhancements) are optional companion modules and are never forced into your dependency tree.

> **Documentation**: [📖 Operations Guide (HTML)](docs/operations-guide.html) · [📚 Online API docs](https://apidoc.gitee.com/sander-o/tools) (Javadoc hosted on Gitee) · [encrypt module](encrypt/README.md) · [encrypt-spring-boot-starter quick start](tools-spring-boot-starter/encrypt-spring-boot-starter/README.md) · [Security policy](SECURITY.md) · [Changelog](CHANGELOG.md) · [中文文档](README.md)

## 📖 Operations Guide (HTML)

A self-contained operations guide (no external dependencies) covering the capability matrix, security boundary table, copy-paste examples for every capability, the Spring Boot integration walkthrough and a FAQ. Three ways to read it:

| Option | How |
|---|---|
| Clone and open locally | `git clone https://gitee.com/sander-o/tools.git`, then open **`docs/operations-guide.html`** in a browser |
| Gitee web UI | Open `docs/operations-guide.html` in the repo, download it via the "RAW" button and open in a browser |
| Offline distribution | Single self-contained file — download, e-mail or print directly (print stylesheet included) |

## Quick Start

### 1. Add `encrypt`

```xml
<dependency>
    <groupId>io.gitee.sander-o</groupId>
    <artifactId>encrypt</artifactId>
    <version>2.0.0</version>
</dependency>
```

> The version is read from the root `pom.xml` (currently `2.0.0`, a development version that may change before release; use the version actually published on [Maven Central](https://central.sonatype.com/artifact/io.gitee.sander-o/tools)).

### 2. Three lines of code

```java
// Digest: MD5 (uppercase hexadecimal)
String md5 = MD5Utils.encode("password");

// Symmetric encryption: AES/CBC/PKCS5Padding, hex key, 16-byte IV
SecretKey key = AesKeyGenerator.getSecretKey();
AesProperties props = new AesProperties();
props.setSecretKey(key);
props.setIvParameter("0123456789abcdef");
AesHelper.load(props);
String ciphertext = AesHelper.encode("hello");
```

### 3. One-step Spring Boot integration

```xml
<dependency>
    <groupId>io.gitee.sander-o.spring.boot</groupId>
    <artifactId>encrypt-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
```

No annotation required — configure `tools.encrypt.*` in `application.yml` and the auto-configuration takes effect; `@EnableToolsEncrypt` is an optional explicit switch. See the [5-minute guide](tools-spring-boot-starter/encrypt-spring-boot-starter/README.md).

> **Upgrade note**: `2.0.0` is a breaking release (HTTP response structure & error codes, base object field renames, nation codes, `encrypt` no longer depends on Spring, etc.) — check the [CHANGELOG](CHANGELOG.md) migration notes before upgrading.

## Why `encrypt`

- **Backed by tests**: the core encryption paths are covered by a smoke-test suite covering every public method (see [testing](encrypt/README.md#测试)).
- **Clear boundaries**: algorithm / mode / padding / key / IV / charset behavior is documented per capability — see the [security boundary table](encrypt/README.md#安全边界).
- **Adopt on demand**: need only crypto? Add `encrypt` only. Need Spring Boot auto-configuration? Add the starter. `bedrock` and `fortify` are optional companions, not prerequisites (only `encrypt` itself depends on a few basic utilities from `bedrock`, which is pulled in transitively).
- **Covers the common cases**: AES / DES / IDEA, RSA, DSA, MD5 / SHA family, digital signatures, JWT, Base64, CRC32.

## Project Layout

```
tools
├── encrypt           # ★ Flagship module: cryptography & security (the primary entry point)
│   ├── asymmetric    # Asymmetric encryption (RSA)
│   ├── basic         # Digests & encoding (MD5, SHA, JWT, Base64, CRC32)
│   ├── digitalsignature # Digital signatures (DSA)
│   └── symmetrical   # Symmetric encryption (AES, DES, IDEA)
├── tools-spring-boot-starter
│   └── encrypt-spring-boot-starter  # Spring Boot auto-configuration
├── bedrock           # Companion module: basic utilities
└── fortify           # Companion module: enhancements
```

## Companion Modules

- **bedrock** — `io.gitee.sander-o:bedrock`: collection / map / date / file / image utilities, unified HTTP response, distributed IDs. `encrypt` depends on a few of its basic utilities, which are pulled in transitively. See [bedrock/README.md](bedrock/README.md).
- **fortify** — `io.gitee.sander-o:fortify`: Excel generation (`ExcelUtils` + `ExcelSheet`), image compression (`ImageUtils`), Alibaba Cloud DDNS (`AliyunDdnsUtils`) and more. See [fortify/README.md](fortify/README.md).

## JDK Support

All modules are published as **Java 8 bytecode** (class major 52) with a minimum runtime of **JDK 8**, so they run as-is on JDK 8 / 17 / 21 (forward compatibility of older bytecode on newer JVMs is guaranteed by the JVM specification). How each version is verified, and the current status:

| Verification path | JDK | Status |
|---|---|---|
| Local smoke run (full unit-test suite, all module smoke cases included) | Oracle JDK 1.8.0_121, Amazon Corretto 17.0.14 | ✅ Executed, all green; artifact bytecode spot-checked as major 52 |
| CI matrix (temurin) | 8 / 17 / 21 | `.github/workflows/ci.yml` draft (repo hosted on Gitee; adapt syntax when porting to its pipeline — the core commands stay the same) |

| Module | Bytecode target | Notes |
|---|---|---|
| bedrock | Java 8 | Pure JDK implementation |
| encrypt | Java 8 | BouncyCastle 1.78.1 (multi-release jar, supports 8–21) |
| fortify | Java 8 | fastjson2 / Alibaba Cloud SDK / POI all work on 17/21 |
| encrypt-spring-boot-starter | Java 8 | Spring Boot 2.7.18 + Spring 5.3.39 (JDK 21 officially supported since Spring 5.3.30) |

Cross-compile guard: building on JDK 9+ automatically activates the `jdk9-cross-compile` profile (`maven.compiler.release=8`), which statically prevents accidental use of JDK 9+ APIs so artifacts built on any JDK still run on Java 8; on JDK 8 the build keeps the existing `source/target=1.8` path.

Running the smoke tests (same command CI uses; the root POM defaults to `skipTests=true`, so it must be enabled explicitly):

```bash
# Run with the current JDK
mvn -B -DskipTests=false clean test

# Run with another installed JDK (Git Bash / Linux syntax)
JAVA_HOME=/path/to/jdk17 PATH=/path/to/jdk17/bin:$PATH mvn -B -DskipTests=false clean test
```

## Installation (all modules)

To pull in every module, import the BOM and declare what you need:

```xml
<properties>
    <!-- Use the version actually published on Maven Central -->
    <tools.version>2.0.0</tools.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.gitee.sander-o</groupId>
            <artifactId>tools</artifactId>
            <version>${tools.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- Cryptography (flagship) -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>encrypt</artifactId>
    </dependency>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>io.gitee.sander-o.spring.boot</groupId>
        <artifactId>encrypt-spring-boot-starter</artifactId>
    </dependency>
    <!-- Basic utilities (companion, optional) -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>bedrock</artifactId>
    </dependency>
    <!-- Enhancements (companion, optional) -->
    <dependency>
        <groupId>io.gitee.sander-o</groupId>
        <artifactId>fortify</artifactId>
    </dependency>
</dependencies>
```

## Usage Samples

See the [operations guide](docs/operations-guide.html) for complete copy-paste examples of every capability, including the security boundary table and FAQ.

## Contributing / Support / License

- Questions and suggestions: [Gitee Issues](https://gitee.com/sander-o/tools/issues). **Do not** submit security issues publicly — see [SECURITY.md](SECURITY.md).
- Workflow: fork the repo, create a `feat/${user_name}/${demand}` branch, submit a Pull Request against `release`.
- Licensed under the [MIT License](LICENSE).

<!-- TODO(badge): add CI / version badges once their availability is confirmed. -->
