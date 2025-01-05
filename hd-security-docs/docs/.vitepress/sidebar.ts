export default {
  '/guide/': [
    {
      text: '简介',
      items: [{ text: '简介', link: '/guide/index' }],
    },
    {
      text: '环境',
      items: [
        { text: '集成 SpringBoot2', link: '/springBoot2' },
        { text: '集成 SpringBoot3', link: '/springBoot3' },
        { text: '集成 WebFlux', link: '/webFlux' },
      ],
    },
    {
      text: '使用',
      items: [
        { text: '登录认证', link: '/springBoot2' },
        { text: '权限认证', link: '/springBoot2' },
        { text: '踢人下线', link: '/springBoot2' },
        { text: '注解鉴权', link: '/springBoot2' },
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
      items: [{ text: 'Markdown Examples', link: '/markdown-examples' }],
    },
  ],
  '/knowledge/': [
    {
      text: '知识',
      items: [{ text: 'Markdown Examples', link: '/markdown-examples' }],
    },
  ],
};
