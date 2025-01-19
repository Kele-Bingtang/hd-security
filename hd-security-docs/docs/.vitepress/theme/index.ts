import DefaultTheme from "vitepress/theme";
import MyLayout from "./components/layout.vue";
import "./styles/index.scss";

export default {
  extends: DefaultTheme,
  Layout: MyLayout,
};
