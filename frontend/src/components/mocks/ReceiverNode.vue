<template>
  <label class="form-label">Receiver Name</label>
  <ReceiverSelector :receiver-param="receiverParam" />
</template>

<script>
import ReceiverSelector from "@/components/receivers/ReceiverSelector.vue";

export default {
  components: {
    ReceiverSelector,
  },
  props: ["receiver-param"],
};
</script>
