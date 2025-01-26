# 简介

Hd Security 是一个轻量级 Java 权限认证框架，它学习自 [sa-token](https://sa-token.cc/)，换而言之，是我在阅读 sa-token 的源码时，通过敲代码的方式一步一步学习的，因此 Hd Security 有的功能 sa-token 都有。

sa-token 的社区更加活跃，所以我建议您使用 sa-token 框架，而本框架更适合学习原理，阅读本框架，你将学习到很多的框架设计理念和权限设计理念。

Hd Security 主要解决登录认证、权限认证、分布式 Session 会话、微服务网关鉴权等一系列权限相关问题。

Hd Security 使用文档基本也是按照 sa-token 的使用文档进行编写，只是 API 相对不一样。因为在学习的过程中，加入了一些自己的见解，设计上略微不同。

因此建议读者去阅读 [设计](/design/login-overview) 部分，我建设文档的目的主要是书写 Hd Security 框架的设计思路，而不是 API 使用，这也是 hd-security 文档没有体现的内容。

## Hd Security 功能特性

Hd Security 主要提供了以下功能：

- 登录认证 —— 单端登录、多端登录、同端互斥登录、七天内免登录
- 权限认证 —— 权限认证、角色认证、会话二级认证
- 踢人下线 —— 根据账号id踢人下线、根据Token值踢人下线
- 注解式鉴权 —— 优雅的将鉴权与业务代码分离
- 路由拦截式鉴权 —— 根据路由拦截鉴权，可适配 restful 模式
- Session 会话 —— 全端共享 Session，单端独享 Session，自定义 Session，方便的存取值
- 持久层扩展 —— 可集成 Redis，重启数据不丢失
- 前后台分离 —— APP、小程序等不支持 Cookie 的终端也可以轻松鉴权
- Token 风格定制 —— 内置六种 Token 风格，还可：自定义 Token 生成策略
- 记住我模式 —— 适配 [记住我] 模式，重启浏览器免验证
- 二级认证 —— 在已登录的基础上再次认证，保证安全性
- 模拟他人账号 —— 实时操作任意用户状态数据
- 临时身份切换 —— 将会话身份临时切换为其它账号
- 同端互斥登录 —— 像QQ一样手机电脑同时在线，但是两个手机上互斥登录
- 账号封禁 —— 登录封禁、按照业务分类封禁、按照处罚阶梯封禁
- 密码加密 —— 提供基础加密算法，可快速 MD5、SHA1、SHA256、AES 加密
- 会话查询 —— 提供方便灵活的会话查询接口
- Http Basic 认证 —— 一行代码接入 Http Basic、Digest 认证
- 全局侦听器 —— 在用户登陆、注销、被踢下线等关键性操作时进行一些AOP操作
- 全局过滤器 —— 方便的处理跨域，全局设置安全响应头等操作
- 多账号体系认证 —— 一个系统多套账号分开鉴权（比如商城的 User 表和 Admin 表）
- 分布式会话 —— 提供共享数据中心分布式会话方案
- 微服务网关鉴权 —— 适配 Gateway、ShenYu、Zuul 等常见网关的路由拦截认证
- 临时 Token 认证 —— 解决短时间的 Token 授权问题
- 独立 Redis —— 将权限缓存与业务缓存分离
- jwt 集成 —— 提供三种模式的 jwt 集成方案，提供 token 扩展参数能力
- 参数签名 —— 提供跨系统 API 调用签名校验模块，防参数篡改，防请求重放
- 自动续签 —— 提供两种 Token 过期策略，灵活搭配使用，还可自动续签
- 开箱即用 —— 提供 SpringBoot、WebFlux、Solon 等常见框架集成包，开箱即用
- 最新技术栈 —— 适配最新技术栈：支持 SpringBoot 3.x，jdk 17



## Hd Security 介绍

Hd Security 旨在以简单、优雅的方式完成系统的权限认证部分，大多数功能都可以一行代码解决。

以登录认证为例，你只需要：

```java
// 会话登录，参数填登录人的账号id 
HdHelper.login(10001)
```

无需实现任何接口，无需创建任何配置文件，只需要这一句静态代码的调用，便可以完成会话登录认证。

如果一个接口需要登录后才能访问，我们只需调用以下代码：

```java
// 校验当前客户端是否已经登录，如果未登录则抛出 `HdSecurityLoginException` 异常
HdHelper.checkLogin();
```

踢人下线：

```java
// 将账号 id 为 10001 的会话踢下线 
HdHelper.kickout(10001);
```

注解拦截鉴权：

```java
// 注册到 Spring Boot 拦截器
registry.addInterceptor(new HdSecurityAnnotationInterceptor()).addPathPatterns("/**");

// 注解鉴权：只有具备 `user:add` 权限的会话才可以进入方法
@HdCheckPermission("user:add")
public String insert(SysUser user) {
    // ... 
    return "新增用户成功";
}
```

Spring Boot 路由拦截鉴权：

```java
// 根据路由划分模块，不同模块不同鉴权 
registry.addInterceptor(new HdSecurityFunctionInterceptor(handler -> {
    HdRouter.match("/user/**", r -> HdHelper.authorizeHelper().checkPermission("user"));
    HdRouter.match("/admin/**", r -> HdHelper.authorizeHelper().checkPermission("admin"));
    // 更多模块... 
})).addPathPatterns("/**");
```



## Hd Security 目录

```markdown
── hd-security
	├──	hd-security-bom								// [依赖] hd-security 模块依赖
    ├── hd-security-core                         	// [核心] hd-security 核心模块
    ├── hd-security-dependencies                 	// [依赖] hd-security 依赖版本信息
    ├── hd-security-starter                      	// [整合] hd-security 与其它框架整合
        ├── hd-security-javax-servlet               // [整合] hd-security 整合 Javax-Servlet 容器实现类包
        ├── hd-security-jakarta-servlet              // [整合] hd-security 整合 Jakarta-Servlet 容器实现类包
        ├── hd-security-spring-boot2-starter          // [整合] hd-security 整合 Spring Boot2 快速集成 
        ├── hd-security-spring-boot3-starter         // [整合] hd-security 整合 Spring Boot3 快速集成 
        ├── hd-security-reactor-spring-boot2-starter  // [整合] hd-security 整合 Spring Boot2 Reactor 响应式编程 快速集成 
        ├── hd-security-reactor-spring-boot3-starter // [整合] hd-security 整合 Spring Boot3 Reactor 响应式编程 快速集成
    ├── hd-security-plugin                       	// [插件] hd-security 插件合集
        ├── hd-security-repository-redis            // [插件] hd-security 整合 Redis (支持 jdk、jackson、fastjson、fastjson2 序列化方式，支持权限缓存与业务缓存分离)
        ├── hd-security-repository-redisson         // [插件] hd-security 整合 Redisson
        ├── hd-security-spring-aop                   // [插件] hd-security 整合 SpringAOP 注解鉴权
        ├── hd-security-jwt                          // [插件] hd-security 整合 jwt 登录认证
    ├── hd-security-demo                         	// [示例] hd-security 示例合集
    ├── hd-security-docs                          	// [文档] hd-security 开发文档 
    ├──pom.xml                               		 // [依赖] 顶级pom文件 
```

