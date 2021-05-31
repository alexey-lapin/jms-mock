<template>
  <label class="form-label">Seconds</label>
  <input type="number" class="form-control" v-model="editingParam.value" />
</template>

<script>
export default {
  props: ["param"],
  data() {
    return {
      editingParam: this.param,
    };
  },
};
</script>
