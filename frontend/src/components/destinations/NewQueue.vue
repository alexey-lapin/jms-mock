<template>
  <div class="card">
    <div class="card-body">
      <EditableQueue
        @queue-form-submitted="onQueueFormSubmitted"
        @queue-form-cancelled="onQueueFormCancelled"
        :queue="localQueue"
      />
    </div>
  </div>
</template>

<script>
import { v4 as uuid } from "uuid";
import EditableQueue from "@/components/destinations/EditableQueue.vue";

export default {
  components: {
    EditableQueue,
  },
  emits: ["queue-created", "queue-cancelled"],
  data() {
    return {
      localQueue: {
        id: uuid(),
        name: "",
        parameters: [],
      },
    };
  },
  methods: {
    onQueueFormSubmitted(queue) {
      this.$store
        .dispatch("createQueue", queue)
        .then(() => this.$emit("queue-created"));
    },
    onQueueFormCancelled() {
      this.$emit("queue-cancelled");
    },
  },
};
</script>
