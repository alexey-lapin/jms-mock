<template>
  <div class="card">
    <div class="card-body">
      <EditableReceiver
        @receiver-form-submitted="onReceiverFormSubmitted"
        @receiver-form-cancelled="onReceiverFormCancelled"
        :receiver="localReceiver"
      />
    </div>
  </div>
</template>

<script>
import { v4 as uuid } from "uuid";
import EditableReceiver from "@/components/receivers/EditableReceiver.vue";

export default {
  components: {
    EditableReceiver,
  },
  emits: ["receiver-created", "receiver-cancelled"],
  data() {
    return {
      localReceiver: {
        id: uuid(),
        name: "",
        parameters: [],
      },
    };
  },
  methods: {
    onReceiverFormSubmitted(receiver) {
      this.$store
        .dispatch("createReceiver", receiver)
        .then(() => this.$emit("receiver-created"));
    },
    onReceiverFormCancelled() {
      this.$emit("receiver-cancelled");
    },
  },
};
</script>
