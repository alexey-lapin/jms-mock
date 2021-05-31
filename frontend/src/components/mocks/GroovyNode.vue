<template>
  <label class="form-label">Script</label>
  <prism-editor
    class="my-editor"
    v-model="scriptParameter.value"
    :highlight="highlighter"
    line-numbers
  ></prism-editor>
  <!-- <Editor :code="editingParameter" :highlighter="highlighter" /> -->
</template>

<script>
// import Editor from "../Editor.vue";
import { highlight, languages } from "prismjs/components/prism-core";
import "prismjs/components/prism-clike";
import "prismjs/components/prism-javascript";
import "prismjs/components/prism-json";
import "prismjs/components/prism-groovy";
import { PrismEditor } from "vue-prism-editor";
import "prismjs/themes/prism-tomorrow.css";
import "vue-prism-editor/dist/prismeditor.min.css";

export default {
  components: {
    PrismEditor,
    // Editor,
  },
  props: ["script-parameter"],
  data() {
    return {
      editingParameter: this.scriptParameter,
    };
  },
  methods: {
    highlighter(code) {
      return highlight(code, languages.groovy);
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
  height: 300px;
}

.prism-editor__textarea:focus {
  outline: none;
}
</style>
