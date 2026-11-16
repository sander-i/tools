# tools

## 介绍

便携工具箱，让编码更易！

本项目的大部分工具方法在网络上均可找到，其核心理念是解决网上搜索查找费时费力、且难以找到真正可用代码的问题。我们致力于提供经过验证、实用性强的工具类代码，避免因错误复制导致的代码质量问题。

## 软件架构

- **tools**
    - **[bedrock](bedrock)**: 提供常用方法、基础对象、常量、特殊符号、JWT、HTTP响应枚举、分页、分布式ID、远程图片转换、文件压缩、图像相似度检测、集合分页、DDNS、Excel导出等基础功能。
    - **[encrypt](encrypt)**: 提供不可逆加密、对称加密、数字签名、非对称加密等安全相关功能。
    - **[fortify](fortify)**: 提供增强功能，如DDNS、HTTP请求处理、JSON工具、文件处理（如Excel、图片压缩）等。
    - **[tools-spring-boot-starter](tools-spring-boot-starter)**: 提供Spring Boot集成支持。
        - **[encrypt-spring-boot-starter](tools-spring-boot-starter/encrypt-spring-boot-starter)**: 实现 `encrypt` 模块的Spring Boot Starter，支持在Spring Boot中通过配置文件注入加密相关Bean。

## 安装教程

### Maven 依赖配置

最新版本链接：https://central.sonatype.com/artifact/io.github.sander-i/tools

在 `pom.xml` 中添加以下依赖：
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

    <!-- 仓库地址 -->
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

## 使用说明

1. 本项目适用于任意用途。
2. 无任何行为限制。
3. 使用者行为与本项目无关。
4. 作者不对使用者行为造成的后果承担法律责任。
5. 本项目目前由个人维护更新，欢迎提出建议或优化点，可通过Issue提交。

## 参与贡献

1. Fork 本仓库。
2. 新建 `feat/${user_name}/${需求}` 分支。
3. 提交代码。
4. 创建 Pull Request 合并到 `develop${年}${月}${日}` 分支。
5. `develop${年}${月}${日}` 测试通过后创建 Pull Request 合并到 `release`。

## 仓库地址

- GitHub（主仓库）：[https://github.com/sander-i/tools](https://github.com/sander-i/tools)
- Gitee（镜像）：[https://gitee.com/sander-o/tools](https://gitee.com/sander-o/tools)

## 开源协议

本项目采用 [MIT License](LICENSE) 开源协议。

## 相关文档

- [贡献指南](CONTRIBUTING.md)
- [更新日志](CHANGELOG.md)
- [贡献者名单](AUTHORS)
- [第三方依赖声明](NOTICE)
