<template>
  <prism-editor
    class="my-editor"
    :class="classes"
    v-model="editable"
    :highlight="highlighter"
    :read-only="readonly"
    line-numbers
  ></prism-editor>
</template>

<script>
import { PrismEditor } from "vue-prism-editor";
import "vue-prism-editor/dist/prismeditor.min.css";
import "prismjs/components/prism-clike";
import "prismjs/components/prism-groovy";
import "prismjs/components/prism-handlebars";
import "prismjs/components/prism-javascript";
import "prismjs/components/prism-json";
import "prismjs/components/prism-markup";
import "prismjs/components/prism-markup-templating";
import "prismjs/themes/prism-tomorrow.css";

export default {
  components: {
    PrismEditor,
  },
  props: {
    modelValue: String,
    classes: String,
    highlighter: Function,
    readonly: {
      type: Boolean,
      default: false,
    },
  },
  emits: ["update:modelValue"],
  computed: {
    editable: {
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
.my-editor {
  background: #2d2d2d;
  color: #ccc;

  font-family: Fira code, Fira Mono, Consolas, Menlo, Courier, monospace;
  font-size: 14px;
  line-height: 1.5;
  padding: 5px;
}

.prism-editor__textarea:focus {
  outline: none;
}
</style>
