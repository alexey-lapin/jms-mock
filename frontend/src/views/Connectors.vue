<template>
  <div class="row">
    <div class="col">
      <Receivers />
    </div>
    <div class="col">
      <Senders />
    </div>
  </div>
</template>

<script>
import Receivers from "@/components/receivers/Receivers.vue";
import Senders from "@/components/senders/Senders.vue";

export default {
  components: {
    Receivers,
    Senders,
  },
};
</script>
