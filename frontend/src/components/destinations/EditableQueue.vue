<template>
  <form>
    <div class="mb-3">
      <label for="queue-name" class="form-label">Name</label>
      <input
        type="text"
        class="form-control"
        id="queue-name"
        v-model="localQueue.name"
      />
    </div>
    <div class="mb-3">
      <label for="queue-destination" class="form-label">Destination</label>
      <input
        type="text"
        class="form-control"
        id="queue-destination"
        v-model="destinationParam.value"
      />
    </div>
    <div class="d-flex justify-content-end">
      <button @click="cancel" type="button" class="btn btn-secondary">
        Cancel
      </button>
      <button @click="submit" type="button" class="btn btn-primary ms-2">
        Save
      </button>
    </div>
  </form>
</template>

<script>
export default {
  props: ["queue"],
  emits: ["queue-form-submitted", "queue-form-cancelled"],
  data() {
    return {
      ...this.init(this.queue),
    };
  },
  methods: {
    init(queue) {
      const localQueue = {
        id: queue.id,
        name: queue.name,
        parameters: queue.parameters,
      };
      const destinationParam = this.getDestinationParam(localQueue);
      return { localQueue, destinationParam };
    },
    getDestinationParam(queue) {
      let param = queue.parameters.find((item) => item.key === "destination");
      if (!param) {
        param = {
          key: "destination",
          value: "",
        };
        queue.parameters.push(param);
      }
      return param;
    },
    submit() {
      this.$emit("queue-form-submitted", this.localQueue);
    },
    cancel() {
      this.$emit("queue-form-cancelled");
    },
  },
};
</script>
