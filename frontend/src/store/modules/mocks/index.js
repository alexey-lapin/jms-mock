import config from "@/config.js";

export default {
  state() {
    return {
      mocks: [],
    };
  },
  getters: {
    mocks(state) {
      return state.mocks;
    },
  },
  mutations: {
    refreshMocks(state, items) {
      state.mocks = items;
    },
    addMock(state, item) {
      state.mocks.push(item);
    },
    updateMock(state, item) {
      const index = state.mocks.findIndex((i) => i.id === item.id);
      state.mocks[index] = item;
    },
    deleteMock(state, item) {
      state.mocks = state.mocks.filter((i) => i.id !== item.id);
    },
  },
  actions: {
    fetchMocks(context) {
      return fetch(`${config.API_BASE_PATH}/mocks`, {
        method: "GET",
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to get mocks");
          }
        })
        .then((data) => {
          context.commit("refreshMocks", data);
        });
    },
    createMock(context, item) {
      return fetch(`${config.API_BASE_PATH}/mocks`, {
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
            throw new Error("failed to create mock");
          }
        })
        .then((data) => {
          context.commit("addMock", data);
        });
    },
    updateMock(context, { name, item }) {
      return fetch(`${config.API_BASE_PATH}/mocks/${name}`, {
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
            throw Error("failed to update mock");
          }
        })
        .then((data) => {
          context.commit("updateMock", data);
        });
    },
    deleteMock(context, item) {
      return fetch(`${config.API_BASE_PATH}/mocks/${item.name}`, {
        method: "DELETE",
      })
        .then((response) => {
          if (response.ok) {
            return;
          } else {
            throw Error("failed to delete mock");
          }
        })
        .then(() => context.commit("deleteMock", item));
    },
    toggleMock(context, item) {
      return fetch(`${config.API_BASE_PATH}/mocks/${item.name}/toggle`, {
        method: "POST",
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to toggle mock");
          }
        })
        .then((data) => {
          context.commit("updateMock", data);
        });
    },
    getHistory(context, item) {
      return fetch(
        `${config.API_BASE_PATH}/mocks/${item.name}/trigger/history`,
        {
          method: "GET",
        }
      ).then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw Error("failed to get mock trigger history");
        }
      });
    },
    deleteHistory(context, item) {
      return fetch(
        `${config.API_BASE_PATH}/mocks/${item.name}/trigger/history`,
        {
          method: "DELETE",
        }
      ).then((response) => {
        if (response.ok) {
          return;
        } else {
          throw Error("failed to delete mock trigger history");
        }
      });
    },
  },
};
