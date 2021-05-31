<template>
  <form>
    <div class="mb-3">
      <label for="sender-name" class="form-label">Name</label>
      <input
        type="text"
        class="form-control"
        id="sender-name"
        v-model="localSender.name"
      />
    </div>
    <div class="mb-3">
      <label for="sender-destination" class="form-label">Destination</label>
      <input
        type="text"
        class="form-control"
        id="sender-destination"
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
  props: ["sender"],
  emits: ["sender-form-submitted", "sender-form-cancelled"],
  data() {
    return {
      ...this.init(this.sender),
    };
  },
  methods: {
    init(sender) {
      const localSender = {
        id: sender.id,
        name: sender.name,
        parameters: sender.parameters,
      };
      const destinationParam = this.getDestinationParam(localSender);
      return { localSender, destinationParam };
    },
    getDestinationParam(sender) {
      let param = sender.parameters.find((item) => item.key === "destination");
      if (!param) {
        param = {
          key: "destination",
          value: "",
        };
        sender.parameters.push(param);
      }
      return param;
    },
    submit() {
      this.$emit("sender-form-submitted", this.localSender);
    },
    cancel() {
      this.$emit("sender-form-cancelled");
    },
  },
};
</script>
