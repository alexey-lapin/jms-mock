import { defineUserConfig } from "vuepress";
import type { DefaultThemeOptions } from "vuepress";

export default defineUserConfig<DefaultThemeOptions>({
  lang: "en-US",
  title: "Hello VuePress",
  description: "Just playing around",
  base: "/jms-mock/",

  themeConfig: {
    logo: "https://vuejs.org/images/logo.png",
    locales: {
      "/": {
        sidebar: {
          "/guide/": [
            {
              isGroup: true,
              text: "Guide",
              children: ["/guide/readme.md", "/guide/getting-started.md"],
            },
          ],
        },
      },
    },
  },
});
