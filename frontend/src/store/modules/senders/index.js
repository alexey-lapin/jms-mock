import config from "@/config.js";

export default {
  state() {
    return {
      senders: [],
    };
  },
  getters: {
    senders(state) {
      return state.senders;
    },
  },
  mutations: {
    refreshSenders(state, items) {
      state.senders = items;
    },
    addSender(state, item) {
      state.senders.push(item);
    },
    updateSender(state, item) {
      const index = state.senders.findIndex((i) => i.id === item.id);
      state.senders[index] = item;
    },
    deleteSender(state, item) {
      state.senders = state.senders.filter((i) => i.id !== item.id);
    },
  },
  actions: {
    fetchSenders(context) {
      return fetch(`${config.API_BASE_PATH}/senders`)
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to get senders");
          }
        })
        .then((data) => {
          context.commit("refreshSenders", data);
        });
    },
    createSender(context, item) {
      return fetch(`${config.API_BASE_PATH}/senders`, {
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
            throw new Error("failed to create sender");
          }
        })
        .then((data) => {
          context.commit("addSender", data);
        });
    },
    updateSender(context, { name, item }) {
      return fetch(`${config.API_BASE_PATH}/senders/${name}`, {
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
            throw Error("failed to update sender");
          }
        })
        .then((data) => {
          context.commit("updateSender", data);
        });
    },
    deleteSender(context, item) {
      return fetch(`${config.API_BASE_PATH}/senders/${item.name}`, {
        method: "DELETE",
      })
        .then((response) => {
          if (response.ok) {
            return;
          } else {
            throw Error("failed to delete sender");
          }
        })
        .then(() => context.commit("deleteSender", item));
    },
  },
};
