<template>
  <h3>Queues</h3>
  <Queue v-for="queue in queues" :key="queue.name" :queue="queue" />
  <div v-if="!isQueueCreating" class="card my-2">
    <div class="card-body d-flex justify-content-center">
      <AddIcon @click="toggleQueueCreating" />
    </div>
  </div>
  <NewQueue
    @queue-created="queueCreated"
    @queue-cancelled="queueCancelled"
    v-if="isQueueCreating"
  />
</template>

<script>
import { mapGetters } from "vuex";
import NewQueue from "@/components/destinations/NewQueue.vue";
import Queue from "@/components/destinations/Queue.vue";
import AddIcon from "@/components/icon/AddIcon.vue";

export default {
  components: {
    NewQueue,
    Queue,
    AddIcon,
  },
  data() {
    return {
      isQueueCreating: false,
    };
  },
  computed: {
    ...mapGetters(["queues"]),
  },
  methods: {
    toggleQueueCreating() {
      this.isQueueCreating = !this.isQueueCreating;
    },
    queueCreated() {
      this.toggleQueueCreating();
    },
    queueCancelled() {
      this.toggleQueueCreating();
    },
  },
};
</script>
