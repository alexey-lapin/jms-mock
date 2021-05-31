<template>
  <TheHeader />
  <div class="container mt-3">
    <main>
      <RouterView />
    </main>
  </div>
  <TheFooter />
</template>

<script>
import config from "./config.js";
import TheHeader from "./components/TheHeader.vue";
import TheFooter from "./components/TheFooter.vue";

export default {
  name: "App",
  components: {
    TheHeader,
    TheFooter,
  },
  mounted() {
    this.$store
      .dispatch("fetchMocks")
      .catch(() => console.log("failed to get mocks"));
    this.$store
      .dispatch("fetchReceivers")
      .catch(() => console.log("failed to get receivers"));
    this.$store
      .dispatch("fetchSenders")
      .catch(() => console.log("failed to get senders"));
    this.$store
      .dispatch("fetchQueues")
      .catch(() => console.log("failed to get queues"));

    const source = new EventSource(`${config.API_BASE_PATH}/events`);
    source.onmessage = (event) => {
      const payload = JSON.parse(event.data);
      if (payload.eventType !== "PING") {
        this.$store.commit("addEvent", payload);
      }
    };
  },
};
</script>
