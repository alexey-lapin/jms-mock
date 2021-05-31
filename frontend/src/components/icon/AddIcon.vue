<template>
  <i @click="handleClick" class="p-1 bi-plus-square"></i>
</template>

<script>
export default {
  emits: ["icon-click"],
  methods: {
    handleClick() {
      this.$emit("icon-click");
    },
  },
};
</script>

<style scoped>
i:hover {
  color: green;
}
</style>
