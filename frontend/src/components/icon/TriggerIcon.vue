<template>
  <i
    @click="handleClick"
    class="p-1 bi-lightning-charge"
    :class="{ 'is-enabled': isEnabled }"
  ></i>
</template>

<script>
export default {
  props: ["is-enabled"],
  emits: ["icon-click"],
  methods: {
    handleClick() {
      if (this.isEnabled) {
        this.$emit("icon-click");
      }
    },
  },
};
</script>

<style scoped>
i:not(.is-enabled) {
  color: lightgrey;
}

i.is-enabled:hover {
  color: yellow;
}
</style>
