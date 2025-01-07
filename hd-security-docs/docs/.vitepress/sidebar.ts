export default {
  '/guide/': [
    {
      text: '简介',
      items: [{ text: '简介', link: '/guide/index' }],
    },
    {
      text: '环境',
      items: [
        { text: '集成 Spring Boot', link: '/guide/env/环境集成 - Spring Boot' },
        { text: '集成 WebFlux', link: '/guide/env/环境集成 - Spring WebFlux' },
      ],
    },
    {
      text: '使用',
      items: [
        { text: '登录认证', link: '/guide/use/使用 - 登录认证' },
        { text: '权限认证', link: '/guide/use/使用 - 权限认证' },
        { text: '登出下线', link: '/guide/use/使用 - 登出下线' },
        { text: '注解鉴权', link: '/guide/use/使用 - 注解鉴权' },
        { text: '路由拦截鉴权', link: '/guide/use/使用 - 路由拦截鉴权' },
        { text: 'Session 会话', link: '/guide/use/使用 - Session 会话' },
        { text: '框架配置', link: '/guide/use/使用 - 框架配置' },
        { text: '自定义 Token', link: '/guide/use/使用 - 自定义 Token' },
        { text: '临时 Token 认证', link: '/guide/use/使用 - 临时 Token 认证' },
        { text: '记住我模式', link: '/guide/use/使用 - 记住我模式' },
        { text: '二级认证', link: '/guide/use/使用 - 二级认证' },
        { text: '身份切换', link: '/guide/use/使用 - 身份切换' },
        { text: '账号封禁', link: '/guide/use/使用 - 账号封禁' },
        { text: '会话查询', link: '/guide/use/使用 - 会话查询' },
        { text: 'Http Basic 认证', link: '/guide/use/使用 - Http Basic 认证' },
        { text: '全局侦听器', link: '/guide/use/使用 - 全局侦听器' },
        { text: '全局过滤器', link: '/guide/use/使用 - 全局过滤器' },
        { text: '多账号认证', link: '/guide/use/使用 - 多账号认证' },
      ],
    },
    {
      text: '插件',
      items: [
        { text: 'AOP 注解鉴权', link: '/guide/plugin/插件 - AOP 注解鉴权' },
        { text: 'Token 集成 JWT', link: '/guide/plugin/插件 - Token 集成 JWT' },
        { text: '持久层集成 Redis', link: '/guide/plugin/插件 - 持久层集成 Redis' },
        { text: '持久层拓展', link: '/guide/plugin/插件 - 持久层拓展' },
      ],
    },
  ],
  '/design/': [
    {
      text: '设计',
      items: [{ text: '设计', link: '/design/index' }],
    },
  ],
  '/knowledge/': [
    {
      text: '知识',
      items: [{ text: '知识', link: '/knowledge/index' }],
    },
  ],
};
