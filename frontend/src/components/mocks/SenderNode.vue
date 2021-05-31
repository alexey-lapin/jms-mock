<template>
  <label class="form-label">Sender Name</label>
  <SenderSelector :sender-param="senderParam" />
</template>

<script>
import SenderSelector from "@/components/senders/SenderSelector.vue";

export default {
  components: {
    SenderSelector,
  },
  props: ["sender-param"],
};
</script>
