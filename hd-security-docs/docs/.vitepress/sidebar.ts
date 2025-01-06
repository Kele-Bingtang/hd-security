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
        { text: '踢人下线', link: '/guide/use/使用 - 踢人下线' },
        { text: '注解鉴权', link: '/guide/use/使用 - 注解鉴权' },
        { text: '路由拦截鉴权', link: '/guide/use/使用 - 路由拦截鉴权' },
        { text: 'Session 会话', link: '/guide/use/使用 - Session 会话' },
        { text: '框架配置', link: '/guide/use/使用 - 框架配置' },
      ],
    },
    {
      text: '开发',
      items: [{ text: '开发指南', link: '/dev' }],
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
