import config from "@/config.js";

export default {
  state() {
    return {
      receivers: [],
    };
  },
  getters: {
    receivers(state) {
      return state.receivers;
    },
  },
  mutations: {
    refreshReceivers(state, items) {
      state.receivers = items;
    },
    addReceiver(state, item) {
      state.receivers.push(item);
    },
    updateReceiver(state, item) {
      const index = state.receivers.findIndex((i) => i.id === item.id);
      state.receivers[index] = item;
    },
    deleteReceiver(state, item) {
      state.receivers = state.receivers.filter((i) => i.id !== item.id);
    },
  },
  actions: {
    fetchReceivers(context) {
      return fetch(`${config.API_BASE_PATH}/receivers`)
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to get receivers");
          }
        })
        .then((data) => {
          context.commit("refreshReceivers", data);
        });
    },
    createReceiver(context, item) {
      return fetch(`${config.API_BASE_PATH}/receivers`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(item),
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw new Error("failed to create receiver");
          }
        })
        .then((data) => {
          context.commit("addReceiver", data);
        });
    },
    updateReceiver(context, { name, item }) {
      return fetch(`${config.API_BASE_PATH}/receivers/${name}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(item),
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to update receiver");
          }
        })
        .then((data) => {
          context.commit("updateReceiver", data);
        });
    },
    deleteReceiver(context, item) {
      return fetch(`${config.API_BASE_PATH}/receivers/${item.name}`, {
        method: "DELETE",
      })
        .then((response) => {
          if (response.ok) {
            return;
          } else {
            throw Error("failed to delete receiver");
          }
        })
        .then(() => context.commit("deleteReceiver", item));
    },
    toggleReceiver(context, item) {
      return fetch(`${config.API_BASE_PATH}/receivers/${item.name}/toggle`, {
        method: "POST",
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to toggle receiver");
          }
        })
        .then((data) => {
          context.commit("updateReceiver", data);
        });
    },
  },
};
