<template>
  <Collapsible title="Headers">
    <Headers v-model="headerExpressions" />
  </Collapsible>
  <Editor
    v-model="payloadTemplate"
    :highlighter="highlight"
    classes="mt-2 mh-300"
  />
</template>

<script>
import Collapsible from "@/components/Collapsible.vue";
import Headers from "@/components/Headers.vue";
import Editor from "@/components/Editor.vue";

const HEADER_EXPRESSION_PREFIX = "header-expression-";
const PAYLOAD_TEMPLATE = "payload-template";

export default {
  components: {
    Collapsible,
    Headers,
    Editor,
  },
  props: ["modelValue"],
  emits: ["update:modelValue"],
  data() {
    return {
      payloadTemplate: this.filterPayloadTemplate(),
      headerExpressions: this.filterHeaderExpressions(),
    };
  },
  watch: {
    payloadTemplate: {
      handler() {
        const arr = this.assemble();
        this.$emit("update:modelValue", arr);
      },
    },
    headerExpressions: {
      deep: true,
      handler() {
        const arr = this.assemble();
        this.$emit("update:modelValue", arr);
      },
    },
  },

  methods: {
    highlight(input) {
      return input;
    },
    filterPayloadTemplate() {
      const p = this.modelValue.find((item) =>
        item.key.startsWith(PAYLOAD_TEMPLATE)
      );
      if (p) {
        return p.value;
      }
      return "";
    },
    filterHeaderExpressions() {
      return this.modelValue
        .filter((item) => item.key.startsWith(HEADER_EXPRESSION_PREFIX))
        .map((item) => {
          return {
            id: item.id,
            key: item.key.replace(HEADER_EXPRESSION_PREFIX, ""),
            value: item.value,
          };
        });
    },
    assemble() {
      const headerExpressions = this.headerExpressions.map((item) => {
        return {
          id: item.id,
          key: HEADER_EXPRESSION_PREFIX + item.key,
          value: item.value,
        };
      });
      const payloadTemplate = {
        key: PAYLOAD_TEMPLATE,
        value: this.payloadTemplate,
      };
      return [payloadTemplate, ...headerExpressions];
    },
  },
};
</script>

<style scoped>
.mh-300 {
  max-height: 300px;
}
</style>
