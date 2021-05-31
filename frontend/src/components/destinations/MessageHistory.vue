<template>
  <div v-for="message in messages" :key="message.headers.id">
    <div @click="onClick(message)" class="card mb-2">
      <div class="card-body">
        <span>{{ getDate(message.createdAt) }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["mock", "counter"],
  emits: ["message-clicked"],
  data() {
    return {
      messages: [],
    };
  },
  mounted() {
    this.getHistory();
  },
  watch: {
    counter() {
      this.getHistory();
    },
  },
  methods: {
    onClick(message) {
      const headers = Object.entries(message.headers ?? {})
        .filter((item) => item[0] !== "id" && item[0] !== "timestamp")
        .map((item) => {
          return { key: item[0], value: item[1] };
        });
      this.$emit("message-clicked", {
        payload: message.payload,
        headers: headers,
      });
    },
    getHistory() {
      this.$store
        .dispatch("getHistory", this.mock)
        .then((data) => (this.messages = data));
    },
    getDate(value) {
      return new Date(value).toLocaleString();
    },
  },
};
</script>
