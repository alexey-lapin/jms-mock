import { createApp } from "vue";
import {
  createRouter,
  createWebHashHistory,
  createWebHistory,
} from "vue-router";
import VueFinalModal from "vue-final-modal";

import config from "./config.js";

import store from "@/store";

import App from "./App.vue";
import Mocks from "./views/Mocks.vue";
import Connectors from "./views/Connectors.vue";
import Destinations from "./views/Destinations.vue";
import Events from "./views/Events.vue";
import QueueViewer from "./components/destinations/QueueViewer.vue";

import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap-icons/font/bootstrap-icons.css";

const router = createRouter({
  history:
    // eslint-disable-next-line no-constant-condition
    "<>" === ""
      ? createWebHashHistory(config.PUBLIC_PATH)
      : createWebHistory(config.PUBLIC_PATH),
  routes: [
    {
      path: "/",
      redirect: "/mocks",
    },
    {
      path: "/mocks",
      component: Mocks,
    },
    {
      path: "/connectors",
      component: Connectors,
    },
    {
      path: "/destinations",
      component: Destinations,
    },
    {
      path: "/destinations/:queueName",
      component: QueueViewer,
      props: true,
    },
    {
      path: "/events",
      component: Events,
    },
  ],
  linkActiveClass: "active",
});

// eslint-disable-next-line prettier/prettier
createApp(App).use(VueFinalModal()).use(router).use(store).mount("#app");
