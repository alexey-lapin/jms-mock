<template>
  <Collapsible title="Headers">
    <Headers v-model="headers" />
  </Collapsible>
  <Editor v-model="payload" :highlighter="highlight" classes="mt-2 mh-300" />
  <div class="d-flex justify-content-end mt-2">
    <button
      @click="resetData"
      type="button"
      class="btn btn-outline-secondary me-2"
    >
      Reset
    </button>
    <button @click="sendTriggerSignal" type="button" class="btn btn-primary">
      Send
      <span
        v-if="isSending"
        class="ms-2 spinner-border spinner-border-sm"
        role="status"
      ></span>
    </button>
  </div>
</template>

<script>
import { highlight, languages } from "prismjs/components/prism-core";
import "prismjs/components/prism-clike";
import "prismjs/components/prism-javascript";
import "prismjs/components/prism-json";
import "prismjs/components/prism-groovy";
import "prismjs/themes/prism-tomorrow.css";
import "vue-prism-editor/dist/prismeditor.min.css";
import config from "@/config.js";
import Collapsible from "@/components/Collapsible.vue";
import Headers from "@/components/Headers.vue";
import Editor from "@/components/Editor.vue";

export default {
  components: {
    Collapsible,
    Headers,
    Editor,
  },
  props: ["mock", "message"],
  emits: ["message-sent"],
  data() {
    return {
      payload: "",
      headers: [],
      isSending: false,
    };
  },
  watch: {
    message(value) {
      this.payload = value.payload ?? "";
      this.headers = value.headers ?? [];
    },
  },
  methods: {
    highlight(code) {
      return highlight(code, languages.json);
    },
    resetData() {
      this.payload = "";
      this.headers = [];
    },
    sendTriggerSignal() {
      this.isSending = true;
      fetch(`${config.API_BASE_PATH}/mocks/${this.mock.name}/trigger`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          parameters: [
            {
              key: "payload",
              value: this.payload,
            },
            ...this.headers,
          ],
        }),
      })
        .then(() => (this.isSending = false))
        .then(() => this.$emit("message-sent"));
    },
  },
};
</script>

<style scoped>
.mh-300 {
  max-height: 300px;
}
</style>
