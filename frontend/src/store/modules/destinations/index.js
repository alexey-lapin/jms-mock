import config from "@/config.js";

export default {
  state() {
    return {
      queues: [],
    };
  },
  getters: {
    queues(state) {
      return state.queues;
    },
  },
  mutations: {
    refreshQueues(state, items) {
      state.queues = items;
    },
    addQueue(state, item) {
      state.queues.push(item);
    },
    updateQueue(state, item) {
      const index = state.queues.findIndex((i) => i.id === item.id);
      state.queues[index] = item;
    },
    deleteQueue(state, item) {
      state.queues = state.queues.filter((i) => i.id !== item.id);
    },
  },
  actions: {
    fetchQueues(context) {
      return fetch(`${config.API_BASE_PATH}/queues`, {
        method: "GET",
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
          } else {
            throw Error("failed to get queues");
          }
        })
        .then((data) => {
          context.commit("refreshQueues", data);
        });
    },
    createQueue(context, item) {
      return fetch(`${config.API_BASE_PATH}/queues`, {
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
            throw new Error("failed to create queue");
          }
        })
        .then((data) => {
          context.commit("addQueue", data);
        });
    },
    updateQueue(context, { name, item }) {
      return fetch(`${config.API_BASE_PATH}/queues/${name}`, {
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
            throw Error("failed to update queue");
          }
        })
        .then((data) => {
          context.commit("updateQueue", data);
        });
    },
    deleteQueue(context, item) {
      return fetch(`${config.API_BASE_PATH}/queues/${item.name}`, {
        method: "DELETE",
      })
        .then((response) => {
          if (response.ok) {
            return;
          } else {
            throw Error("failed to delete queue");
          }
        })
        .then(() => context.commit("deleteQueue", item));
    },
    countMessages(context, queueName) {
      return fetch(`${config.API_BASE_PATH}/queues/${queueName}/count`, {
        method: "GET",
      }).then((response) => {
        if (response.ok) {
          return response.text();
        } else {
          throw Error("failed to count queue");
        }
      });
    },
    browseQueue(context, queueName) {
      return fetch(`${config.API_BASE_PATH}/queues/${queueName}/browse`, {
        method: "GET",
      }).then((response) => {
        if (response.ok) {
          return response.json();
        } else {
          throw Error("failed to browse queue");
        }
      });
    },
    purgeQueue(context, queueName) {
      return fetch(`${config.API_BASE_PATH}/queues/${queueName}/purge`, {
        method: "POST",
      }).then((response) => {
        if (response.ok) {
          return;
        } else {
          throw Error("failed to purge queue");
        }
      });
    },
  },
};
