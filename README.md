# tools

#### 介绍

便携工具箱，让编码更易！

[bedrock](bedrock) 提供常用方法、基础对象、常量、特殊符号、JWT、HTTP响应枚举、分页、分布式ID、远程图片转换

[encrypt](encrypt) 不可逆加密、对称加密、数字签名、非对称加密

[tools-spring-boot-starter](tools-spring-boot-starter) 管理集成 spring-boot-starter 项目

[encrypt-spring-boot-starter](tools-spring-boot-starter%2Fencrypt-spring-boot-starter) 实现 [encrypt](encrypt) 模块
集成 spring-boot-starter，在 springboot 配置文件 properties|yml 配置密钥等属性，实现自动注入

#### 软件架构

- tools
    - bedrock
    - encrypt
    - tools-spring-boot-starter
        - encrypt-spring-boot-starter

#### 安装教程

```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.tools</groupId>
            <artifactId>tools</artifactId>
            <version>latest-version</version>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.tools</groupId>
        <artifactId>tools</artifactId>
    </dependency>

    <dependency>
        <groupId>org.tools</groupId>
        <artifactId>bedrock</artifactId>
    </dependency>

    <dependency>
        <groupId>org.tools</groupId>
        <artifactId>encrypt</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.tools.spring.boot</groupId>
        <artifactId>tools-spring-boot-starter</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.tools.spring.boot</groupId>
        <artifactId>encrypt-spring-boot-starter</artifactId>
    </dependency>
</dependencies>
```

#### 使用说明

1. 此项目支持任意用途
2. 此项目没有任何行为限制
3. 使用者的行为与本项目无关
4. 作者对使用者造成的一切后果不承担法律责任

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

#### 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5. Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
