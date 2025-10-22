import { defineTeekConfig } from "vitepress-theme-teek/config";

export const teekConfig = defineTeekConfig({
  author: { name: "Teeker", link: "https://github.com/Kele-Bingtang" },
  footerInfo: {
    copyright: {
      createYear: 2024,
      suffix: "Hd Security",
    },
  },
  codeBlock: {
    copiedDone: TkMessage => TkMessage.success("复制成功！"),
  },
  articleShare: { enabled: true },
  vitePlugins: {
    sidebarOption: {
      initItems: false,
    },
  },
});
