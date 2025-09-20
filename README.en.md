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

Latest version link：https://central.sonatype.com/artifact/io.gitee.sander-o/tools

Add the following dependencies to your：`pom.xml`
```
    <properties>
        <tools.version>Latest Version</tools.version>
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
        <dependency>
            <groupId>io.gitee.sander-o</groupId>
            <artifactId>bedrock</artifactId>
        </dependency>
        <dependency>
            <groupId>io.gitee.sander-o</groupId>
            <artifactId>encrypt</artifactId>
        </dependency>
        <dependency>
            <groupId>io.gitee.sander-o</groupId>
            <artifactId>fortify</artifactId>
        </dependency>
        <dependency>
            <groupId>io.gitee.sander-o.spring.boot</groupId>
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

## Contribution

1. Fork This warehouse。
2. Newly Built `feat/${user_name}/${demand}` Branch。
3. Submit the code。
4. Create Pull Request Incorporated to `develop${year}${month}${day}` Branch。
5. `develop${year}${month}${day}` Created after the test passes Pull Request Incorporated to `release`。

## Stunt

1. Use `Readme_XXX.md` Multilingual documentation is supported, such as: `Readme_en.md`, `Readme_zh.md`。
2. Gitee Official Blog：[blog.gitee.com](https://blog.gitee.com)
3. Explore Gitee Excellent open source projects on the：[https://gitee.com/explore](https://gitee.com/explore)
4. GVP（Gitee Most valuable open source project）：[https://gitee.com/gvp](https://gitee.com/gvp)
5. Gitee Official user manual：[https://gitee.com/help](https://gitee.com/help)
6. Gitee Cover character column：[https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
