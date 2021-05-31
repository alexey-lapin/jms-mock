<template>
  <div class="accordion">
    <div class="accordion-item">
      <h2 class="accordion-header">
        <button
          @click="toggleCollapsed"
          class="accordion-button"
          :class="{ collapsed: isCollapsed }"
          type="button"
        >
          {{ title }}
        </button>
      </h2>
      <div class="accordion-collapse collapse" :class="{ show: !isCollapsed }">
        <div class="accordion-body">
          <slot></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["title"],
  data() {
    return {
      isCollapsed: true,
    };
  },
  methods: {
    toggleCollapsed() {
      this.isCollapsed = !this.isCollapsed;
    },
  },
};
</script>
