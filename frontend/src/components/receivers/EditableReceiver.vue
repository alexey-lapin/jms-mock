<template>
  <form>
    <div class="mb-3">
      <label for="receiver-name" class="form-label">Name</label>
      <input
        type="text"
        class="form-control"
        id="receiver-name"
        v-model="localReceiver.name"
      />
    </div>
    <div class="mb-3">
      <label for="receiver-destination" class="form-label">Destination</label>
      <input
        type="text"
        class="form-control"
        id="receiver-destination"
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
  props: ["receiver"],
  emits: ["receiver-form-submitted", "receiver-form-cancelled"],
  data() {
    return {
      ...this.init(this.receiver),
    };
  },
  methods: {
    init(receiver) {
      const localReceiver = {
        id: receiver.id,
        name: receiver.name,
        parameters: receiver.parameters,
      };
      const destinationParam = this.getDestinationParam(localReceiver);
      return { localReceiver, destinationParam };
    },
    getDestinationParam(receiver) {
      let param = receiver.parameters.find(
        (item) => item.key === "destination"
      );
      if (!param) {
        param = {
          key: "destination",
          value: "",
        };
        receiver.parameters.push(param);
      }
      return param;
    },
    submit() {
      this.$emit("receiver-form-submitted", this.localReceiver);
    },
    cancel() {
      this.$emit("receiver-form-cancelled");
    },
  },
};
</script>
