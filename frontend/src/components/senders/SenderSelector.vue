<template>
  <select class="form-select" v-model="editingSenderParam.value">
    <option v-for="sender in senders" :key="sender.name" :value="sender.name">
      {{ sender.name }}
    </option>
  </select>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  props: ["sender-param"],
  data() {
    return {
      editingSenderParam: this.senderParam,
    };
  },
  computed: {
    ...mapGetters(["senders"]),
  },
};
</script>
