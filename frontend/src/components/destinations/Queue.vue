<template>
  <div class="card my-2">
    <div class="card-body">
      <div class="d-flex align-items-center">
        <div class="flex-grow-1">
          <span>{{ queue.name }}</span>
        </div>
        <RouterLink :to="'/destinations/' + queue.name">
          <span class="badge bg-primary font-monospace">{{
            messageCount
          }}</span>
        </RouterLink>
        <DeleteIcon @icon-click="deleteQueue" />
      </div>
    </div>
  </div>
</template>

<script>
import DeleteIcon from "@/components/icon/DeleteIcon.vue";

export default {
  components: {
    DeleteIcon,
  },
  props: ["queue"],
  data() {
    return {
      isQueueEditing: false,
      messageCount: "?",
    };
  },
  mounted() {
    this.$store
      .dispatch("countMessages", this.queue.name)
      .then((data) => (this.messageCount = data));
  },
  methods: {
    toggleQueueEditing() {
      this.isQueueEditing = !this.isQueueEditing;
    },
    onQueueFormSubmitted(queue) {
      this.$store
        .dispatch("updateQueue", {
          name: this.queue.name,
          item: queue,
        })
        .then(() => {
          this.toggleQueueEditing();
        });
    },
    deleteQueue() {
      this.$store.dispatch("deleteQueue", this.queue);
    },
  },
};
</script>
