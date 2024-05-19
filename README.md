# tools

#### 介绍

便携工具箱，让编码更易！
项目的大部分工具方法在网上均可以搜到，本项目的理念就是为了解决网上搜索查找费时费力还不是很好用

（更多的体现在一个人发的文章是错的，其它人直接原样copy导致一堆垃圾代码文章，寻找自己想要的代码费劲的很）

[bedrock](bedrock) 提供常用方法、基础对象、常量、特殊符号、JWT、HTTP响应枚举、分页、分布式ID、远程图片转换、集合分页、ddns、Excel导出

[encrypt](encrypt) 不可逆加密、对称加密、数字签名、非对称加密

[tools-spring-boot-starter](tools-spring-boot-starter) 管理集成 spring-boot-starter 项目

[encrypt-spring-boot-starter](tools-spring-boot-starter%2Fencrypt-spring-boot-starter) 实现 [encrypt](encrypt) 模块
集成 spring-boot-starter，在 springboot 配置文件 properties|yml 配置密钥等属性，实现自动注入

#### 软件架构

- tools
    - [bedrock](bedrock)
    - [encrypt](encrypt)
    - [fortify](fortify)
    - [tools-spring-boot-starter](tools-spring-boot-starter)
        - [encrypt-spring-boot-starter](tools-spring-boot-starter%2Fencrypt-spring-boot-starter)

#### 安装教程

pom.xml
```
    <properties>
        <tools.version>1.0.6</tools.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.tools</groupId>
                <artifactId>tools</artifactId>
                <version>${tools.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.tools</groupId>
            <artifactId>bedrock</artifactId>
        </dependency>
        <dependency>
            <groupId>org.tools</groupId>
            <artifactId>encrypt</artifactId>
        </dependency>
        <dependency>
            <groupId>org.tools</groupId>
            <artifactId>fortify</artifactId>
        </dependency>
        <dependency>
            <groupId>org.tools.spring.boot</groupId>
            <artifactId>encrypt-spring-boot-starter</artifactId>
        </dependency>
    </dependencies>

    <!-- 仓库地址 -->
    <repositories>
        <repository>
            <id>ailuoli</id>
            <url>http://ailuoli.com.cn:17993/repository/maven-public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>ailuoli-plugin</id>
            <url>http://ailuoli.com.cn:17993/repository/maven-public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </pluginRepository>
    </pluginRepositories>
```


maven settings.xml
```
    <mirrors>
        <!-- http支持 -->
        <mirror>
            <id>insecure-repo</id>
            <mirrorOf>external:http:*</mirrorOf>
            <url>http://ailuoli.com.cn:17993/repository/maven-public/</url>
            <blocked>false</blocked>
        </mirror>
    </mirrors>
```

#### 使用说明

1. 此项目支持任意用途
2. 此项目没有任何行为限制
3. 使用者的行为与本项目无关
4. 作者对使用者造成的一切后果不承担法律责任
5. 本项目目前由sander个人进行维护迭代更新，如果感兴趣或者有建议和优化点可以直接联系或新建 Issue

#### 参与贡献

1. Fork 本仓库
2. 新建 feat/${user_name}/${demand} 分支
3. 提交代码
4. 新建 Pull Request 申请合并 develop${year}${month}${day}
5. develop${year}${month}${day} 测试通过后新建 Pull Request 申请合并 release
6. 每月20日进行合并 release 验证功能评估风险 21日进行正式发版

#### 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5. Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
