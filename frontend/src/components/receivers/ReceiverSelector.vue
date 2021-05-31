<template>
  <select class="form-select" v-model="editingReceiverParam.value">
    <option
      v-for="receiver in receivers"
      :key="receiver.name"
      :value="receiver.name"
    >
      {{ receiver.name }}
    </option>
  </select>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  props: ["receiver-param"],
  data() {
    return {
      editingReceiverParam: this.receiverParam,
    };
  },
  computed: {
    ...mapGetters(["receivers"]),
  },
};
</script>
