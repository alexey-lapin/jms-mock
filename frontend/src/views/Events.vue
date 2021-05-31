<template>
  <div class="mt-3">
    <div v-for="event in events" :key="event.id">
      <div class="alert" :class="alertClass(event)">
        {{ new Date(event.createdAt).toLocaleTimeString() }} |
        {{ event.message }}
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  computed: {
    ...mapGetters(["events"]),
  },
  methods: {
    alertClass(event) {
      if (event.eventType === "INFO") {
        return { "alert-info": true };
      } else if (event.eventType === "WARN") {
        return { "alert-warning": true };
      } else if (event.eventType === "ERROR") {
        return { "alert-danger": true };
      } else {
        return {};
      }
    },
  },
};
</script>
