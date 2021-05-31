<template>
  <div class="card">
    <div class="card-body">
      <EditableSender
        @sender-form-submitted="onSenderFormSubmitted"
        @sender-form-cancelled="onSenderFormCancelled"
        :sender="localSender"
      />
    </div>
  </div>
</template>

<script>
import { v4 as uuid } from "uuid";
import EditableSender from "@/components/senders/EditableSender.vue";

export default {
  components: {
    EditableSender,
  },
  emits: ["sender-created", "sender-cancelled"],
  data() {
    return {
      localSender: {
        id: uuid(),
        name: "",
        parameters: [],
      },
    };
  },
  methods: {
    onSenderFormSubmitted(sender) {
      this.$store
        .dispatch("createSender", sender)
        .then(() => this.$emit("sender-created"));
    },
    onSenderFormCancelled() {
      this.$emit("sender-cancelled");
    },
  },
};
</script>
