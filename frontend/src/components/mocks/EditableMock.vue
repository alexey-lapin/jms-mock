<template>
  <form>
    <div class="mb-3">
      <label for="mock-name" class="form-label">Name</label>
      <input
        type="text"
        class="form-control"
        id="mock-name"
        v-model="localMock.name"
      />
    </div>
    <draggable
      :list="localMock.nodes"
      item-key="id"
      class="d-flex flex-row"
      handle=".handle"
    >
      <template #item="{ element }">
        <Node
          :node="element"
          @node-updated="onNodeUpdated"
          @node-deleted="onNodeDeleted"
        />
      </template>
      <template #footer>
        <div class="card mx-1" style="width: 10rem">
          <div
            class="card-body d-flex justify-content-center align-items-center"
          >
            <AddIcon @click="toggleCreating" />
          </div>
        </div>
      </template>
    </draggable>
    <div class="d-flex justify-content-end mt-2">
      <button @click="cancel" type="button" class="btn btn-secondary">
        Cancel
      </button>
      <button @click="submit" type="button" class="btn btn-primary ms-2">
        Save
      </button>
    </div>
  </form>
</template>

<script>
import { v4 as uuid } from "uuid";
import draggable from "vuedraggable";
import Node from "@/components/mocks/Node.vue";
import AddIcon from "@/components/icon/AddIcon.vue";

export default {
  components: {
    Node,
    AddIcon,
    draggable,
  },
  props: ["mock"],
  emits: ["mock-form-submitted", "mock-form-cancelled"],
  data() {
    return {
      localMock: {
        id: this.mock.id,
        name: this.mock.name,
        nodes: this.mock.nodes,
      },
    };
  },
  methods: {
    toggleCreating() {
      this.localMock.nodes.push({
        id: uuid(),
        type: "",
        parameters: [],
      });
    },
    onNodeUpdated(node) {
      const index = this.localMock.nodes.findIndex(
        (item) => item.id === node.id
      );
      this.localMock.nodes[index] = node;
    },
    onNodeDeleted(node) {
      this.localMock.nodes = this.localMock.nodes.filter(
        (item) => item.id !== node.id
      );
    },
    submit() {
      this.$emit("mock-form-submitted", this.localMock);
    },
    cancel() {
      this.$emit("mock-form-cancelled");
    },
  },
};
</script>
