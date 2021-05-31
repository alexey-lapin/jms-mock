<template>
  <div class="form-check form-check-inline form-switch">
    <input class="form-check-input" type="checkbox" v-model="isEnabled" />
  </div>
</template>

<script>
export default {
  props: ["modelValue"],
  emits: ["update:modelValue"],
  computed: {
    isEnabled: {
      get() {
        return this.modelValue;
      },
      set(value) {
        this.$emit("update:modelValue", value);
      },
    },
  },
};
</script>

<style scoped>
.form-switch {
  margin-right: 0;
}
</style>
