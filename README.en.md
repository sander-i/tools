# tools

## Introduce

Portable toolbox to make coding easier!

Most of the tooling methods for this project are available on the web, Its core philosophy is to solve the problem of time-consuming and laborious online searches and the difficulty of finding truly usable code. We are committed to providing proven, useful tool code，Avoid code quality issues caused by incorrect copying. 

## Software architecture

- **tools**
  - **[bedrock](bedrock)**: Provides basic functions such as common methods, basic objects, constants, special symbols, JWT, HTTP response enumeration, pagination, distributed ID, remote image conversion, file compression, image similarity detection, collection pagination, DDNS, Excel export, and more.
  - **[encrypt](encrypt)**: Provide security-related functions such as irreversible encryption, symmetric encryption, digital signatures, and asymmetric encryption.
  - **[fortify](fortify)**: Offers enhanced features such as DDNS, HTTP request handling, JSON tools, file handling (e.g., Excel, image compression), and more.
  - **[tools-spring-boot-starter](tools-spring-boot-starter)**: Spring Boot integration support is available.
    - **[encrypt-spring-boot-starter](tools-spring-boot-starter/encrypt-spring-boot-starter)**: Spring Boot Starter implements the 'encrypt' module to support injecting encryption-related beans through configuration files in Spring Boot.

## Installation tutorial

### Maven Dependency configuration

Latest version link：https://central.sonatype.com/artifact/io.github.sander-i/tools

Add the following dependencies to your：`pom.xml`
```
    <properties>
        <tools.version>Latest Version</tools.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>io.github.sander-i</groupId>
                <artifactId>tools</artifactId>
                <version>${tools.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>io.github.sander-i</groupId>
            <artifactId>bedrock</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.sander-i</groupId>
            <artifactId>encrypt</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.sander-i</groupId>
            <artifactId>fortify</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.sander-i.spring.boot</groupId>
            <artifactId>encrypt-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <!-- Warehouse address -->
    <repositories>
        <repository>
            <id>maven2</id>
            <url>https://repo1.maven.org/maven2</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
```

## Instructions

1. This item is suitable for any purpose。
2. There are no restrictions on behavior。
3. User behavior has nothing to do with this project。
4. The author is not legally responsible for the consequences of the user's actions。
5. This project is currently maintained and updated by individuals, and suggestions or optimization points are welcome, which can be submitted through Issues。

## Upgrading from 1.x to 2.0

2.0.0 contains two breaking changes that must be applied together:

| Item | 1.x | 2.0 |
|---|---|---|
| Maven groupId | `io.gitee.sander-o` | `io.github.sander-i` |
| Java package root | `org.tools` | `io.github.sander_i` |
| Starter groupId | `io.gitee.sander-o.spring.boot` | `io.github.sander-i.spring.boot` |

The package name uses `sander_i` with an underscore because Java package names cannot contain hyphens, while the groupId `sander-i` does.

Upgrade steps:

1. Replace the groupId in `pom.xml`
2. Replace all `import org.tools.` with `import io.github.sander_i.` in your sources
3. Rebuild

1.x releases remain available on Maven Central but are no longer maintained.

## Contribution

1. Fork this repository。
2. Create a branch named `feat/${user_name}/${demand}`。
3. Commit your code。
4. Open a Pull Request against the `develop${year}${month}${day}` branch。
5. Once tests pass on `develop${year}${month}${day}`, a Pull Request is opened against `release`。

## Repository

- GitHub (primary): [https://github.com/sander-i/tools](https://github.com/sander-i/tools)
- Gitee (mirror): [https://gitee.com/sander-o/tools](https://gitee.com/sander-o/tools)

## License

This project is licensed under the [MIT License](LICENSE).

## Documents

- [Contributing Guide](CONTRIBUTING.en.md) / [中文](CONTRIBUTING.md)
- [Changelog](CHANGELOG.en.md) / [中文](CHANGELOG.md)
- [Authors](AUTHORS.en.md) / [中文](AUTHORS)
- [Notice](NOTICE.en) / [中文](NOTICE)
- [Security Policy](SECURITY.md)
- [Code of Conduct](CODE_OF_CONDUCT.md)
