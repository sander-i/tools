# tools

#### Description
Portable toolbox to make coding easier!
Most of the tools and methods of the project are searchable on the Internet, and the concept of this project is to solve the problem that searching on the Internet is time-consuming and laborious, and it is not very easy to use

(More reflected in the fact that the article posted by one person is wrong, and other people directly copy the original resulting in a bunch of garbage code articles, and it is very difficult to find the code you want)

[bedrock](bedrock) It provides common methods, basic objects, constants, special symbols, JWT, HTTP response enumeration, pagination, distributed ID, remote image conversion, collection pagination, ddns, and Excel export

[encrypt](encrypt) Irreversible encryption, symmetric encryption, digital signature, asymmetric encryption

[tools-spring-boot-starter](tools-spring-boot-starter) Management Compilation spring-boot-starter Project

[encrypt-spring-boot-starter](tools-spring-boot-starter%2Fencrypt-spring-boot-starter) Implement [encrypt](encrypt) Module
Integration spring-boot-starter，At springboot profiles properties|yml Configure attributes such as keys for automatic injection

#### Software Architecture

- tools
  - [bedrock](bedrock)
  - [encrypt](encrypt)
  - [fortify](fortify)
  - [tools-spring-boot-starter](tools-spring-boot-starter)
    - [encrypt-spring-boot-starter](tools-spring-boot-starter%2Fencrypt-spring-boot-starter)

#### Installation

```
    <properties>
        <tools.version>1.0.6.1</tools.version>
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

#### Instructions

1. This project can be used for any purpose
2. There are no restrictions on the behavior of this project
3. The user's actions are not related to this project
4. The author shall not be liable for any consequences caused by the user
5. This project is currently maintained and updated by sander personally, if you are interested or have suggestions and optimization points, you can directly contact or create a new issue

#### Contribution

1. Fork this repository
2. Create feat/${user_name}/${demand} branch
3. Commit your code
4. Create Pull Request Apply for merger develop${year}${month}${day}
5. develop${year}${month}${day} Create a new file after the test is passed Pull Request Apply for merger release
6. Merge and release on the 20th of each month, verify the function and assess the risk, and officially release the version on the 21st

#### Gitee Feature

1.  You can use Readme\_XXX.md to support different languages, such as Readme\_en.md, Readme\_zh.md
2.  Gitee blog [blog.gitee.com](https://blog.gitee.com)
3.  Explore open source project [https://gitee.com/explore](https://gitee.com/explore)
4.  The most valuable open source project [GVP](https://gitee.com/gvp)
5.  The manual of Gitee [https://gitee.com/help](https://gitee.com/help)
6.  The most popular members  [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
