<template>
  <div class="d-flex justify-content-between">
    <h3>Messages in {{ queueName }}</h3>
    <button class="btn btn-outline-danger" @click="onClearButtonClicked">
      Clear
    </button>
  </div>
  <Message
    class="my-2"
    v-for="message in messages"
    :key="message.headers.jms_timestamp"
    :message="message"
  />
</template>

<script>
import Message from "@/components/destinations/Message.vue";

export default {
  components: {
    Message,
  },
  props: ["queue-name"],
  data() {
    return {
      messages: [],
    };
  },
  mounted() {
    this.$store
      .dispatch("browseQueue", this.queueName)
      .then((data) => (this.messages = data));
  },
  methods: {
    onClearButtonClicked() {
      this.$store
        .dispatch("purgeQueue", this.queueName)
        .then(() => (this.messages = []));
    },
  },
};
</script>
