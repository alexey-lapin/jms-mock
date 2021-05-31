<template>
  <div class="card">
    <div class="card-body">
      <EditableMock
        :mock="localMock"
        @mock-form-submitted="onMockFormSubmitted"
        @mock-form-cancelled="onMockFormCancelled"
      />
    </div>
  </div>
</template>

<script>
import { v4 as uuid } from "uuid";
import EditableMock from "@/components/mocks/EditableMock.vue";

export default {
  components: {
    EditableMock,
  },
  emits: ["mock-created", "mock-cancelled"],
  data() {
    return {
      localMock: {
        id: uuid(),
        name: "",
        nodes: [],
      },
    };
  },
  methods: {
    onMockFormSubmitted(mock) {
      this.$store
        .dispatch("createMock", mock)
        .then(() => this.$emit("mock-created"));
    },
    onMockFormCancelled() {
      this.$emit("mock-cancelled");
    },
  },
};
</script>
