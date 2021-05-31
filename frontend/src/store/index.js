import { createStore } from "vuex";
import mocks from "@/store/modules/mocks";
import receivers from "@/store/modules/receivers";
import senders from "@/store/modules/senders";
import destinations from "@/store/modules/destinations";

export default createStore({
  modules: {
    mocks,
    receivers,
    senders,
    destinations,
  },
  state() {
    return {
      events: [],
    };
  },
  mutations: {
    addEvent(state, event) {
      state.events.unshift(event);
      state.events.length = Math.min(state.events.length, 50);
    },
  },
  getters: {
    events(state) {
      return state.events;
    },
  },
});
