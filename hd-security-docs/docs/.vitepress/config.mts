import { defineConfig } from "vitepress";
import { teekConfig } from "./teekConfig";

const description = ["Hd Security 使用文档", "认证框架"].toString();

// https://vitepress.dev/reference/site-config
export default defineConfig({
  extends: teekConfig,
  base: "/",
  title: "Hd Security",
  description: description,
  cleanUrls: true,
  lastUpdated: true,
  lang: "zh-CN",
  head: [
    ["meta", { name: "author", content: "Teeker" }],
    ["meta", { name: "description", description }],
    ["meta", { name: "keywords", description }],
  ],
  markdown: {
    // 开启行号
    lineNumbers: true,
    image: {
      // 默认禁用；设置为 true 可为所有图片启用懒加载。
      lazyLoading: true,
    },
    // 更改容器默认值标题
    container: {
      tipLabel: "提示",
      warningLabel: "警告",
      dangerLabel: "危险",
      infoLabel: "信息",
      detailsLabel: "详细信息",
    },
  },
  rewrites: {
    "guide/use/使用 - 登出下线.md": "aaa/aa.md",
  },
  ignoreDeadLinks: true,
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    logo: "/logo.svg",
    darkModeSwitchLabel: "主题",
    sidebarMenuLabel: "菜单",
    returnToTopLabel: "返回顶部",
    lastUpdatedText: "上次更新时间",
    outline: {
      level: [2, 4],
      label: "本页导航",
    },
    docFooter: {
      prev: "上一页",
      next: "下一页",
    },
    nav: [
      { text: "首页", link: "/" },
      { text: "指南", link: "/guide/intro" },
      { text: "设计", link: "/design/login-overview" },
      { text: "API", link: "/api/login" },
    ],

    socialLinks: [
      { icon: "github", link: "https://github.com/Kele-Bingtang/hd-security" },
    ],

    search: {
      provider: "local",
    },
    editLink: {
      text: "在 GitHub 上编辑此页",
      pattern:
        "https://github.com/Kele-Bingtang/hd-security/edit/master/hd-security-docs/docs/:path",
    },
  },
});
