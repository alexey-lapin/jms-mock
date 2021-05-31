<template>
  <Collapsible :title="date">
    <MessageHeaders :headers="message.headers" />
    <Editor
      :readonly="false"
      :highlight="highlighter"
      :modelValue="message.payload"
      :style="{ background: 'white' }"
    />
  </Collapsible>
</template>

<script>
import { highlight, languages } from "prismjs/components/prism-core";

import Collapsible from "@/components/Collapsible.vue";
import MessageHeaders from "@/components/destinations/MessageHeaders.vue";
import Editor from "@/components/Editor.vue";

export default {
  components: {
    Collapsible,
    MessageHeaders,
    Editor,
  },
  props: ["message"],
  computed: {
    date() {
      const millis = parseInt(this.message.headers.jms_timestamp);
      return new Date(millis).toLocaleString();
    },
  },
  methods: {
    highlighter(input) {
      return highlight(input, languages.json);
    },
  },
};
</script>
