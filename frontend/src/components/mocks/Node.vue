<template>
  <div>
    <div class="card mx-1" style="width: 10rem">
      <div class="card-body">
        <div class="d-flex">
          <i class="p-1 bi-arrows-move handle" />
          <div class="flex-grow-1">
            <span class="align-middle">{{ localNode.type }}</span>
          </div>
          <EditIcon @icon-click="toggleNodeEditing" />
          <DeleteIcon @icon-click="deleteNode" />
        </div>
      </div>
    </div>
    <VueFinalModal v-model="isNodeEditing">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Node config</h5>
            <button
              type="button"
              class="btn-close"
              @click="toggleNodeEditing"
            ></button>
          </div>
          <div class="modal-body">
            <label class="form-label">Type</label>
            <select class="form-select" v-model="localNode.type">
              <option value="composer">composer</option>
              <option value="delay">delay</option>
              <option value="interval">interval</option>
              <option value="groovy">groovy</option>
              <option value="receiver">receiver</option>
              <option value="sender">sender</option>
              <option value="template">template</option>
            </select>
            <div class="mt-3">
              <DelayNode
                v-if="localNode.type === 'delay'"
                :param="getParameter('duration')"
              />
              <IntervalNode
                v-if="localNode.type === 'interval'"
                :param="getParameter('duration')"
              />
              <GroovyNode
                v-if="localNode.type === 'groovy'"
                :script-parameter="getParameter('script')"
              />
              <ReceiverNode
                v-if="localNode.type === 'receiver'"
                :receiver-param="getParameter('receiver-name')"
              />
              <SenderNode
                v-if="localNode.type === 'sender'"
                :sender-param="getParameter('sender-name')"
              />
              <TemplateNode
                v-if="localNode.type === 'template'"
                v-model="localNode.parameters"
              />
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="cancel">
              Cancel
            </button>
            <button type="button" class="btn btn-primary" @click="submit">
              Save
            </button>
          </div>
        </div>
      </div>
    </VueFinalModal>
  </div>
</template>

<script>
import DelayNode from "@/components/mocks/DelayNode.vue";
import IntervalNode from "@/components/mocks/IntervalNode.vue";
import GroovyNode from "@/components/mocks/GroovyNode.vue";
import ReceiverNode from "@/components/mocks/ReceiverNode.vue";
import SenderNode from "@/components/mocks/SenderNode.vue";
import TemplateNode from "@/components/mocks/TemplateNode.vue";
import EditIcon from "@/components/icon/EditIcon.vue";
import DeleteIcon from "@/components/icon/DeleteIcon.vue";

export default {
  components: {
    GroovyNode,
    ReceiverNode,
    SenderNode,
    DelayNode,
    IntervalNode,
    TemplateNode,
    EditIcon,
    DeleteIcon,
  },
  props: ["node"],
  emits: ["node-updated", "node-deleted"],
  data() {
    return {
      localNode: {
        id: this.node.id,
        type: this.node.type,
        parameters: this.node.parameters,
      },
      isNodeEditing: false,
    };
  },
  methods: {
    toggleNodeEditing() {
      this.isNodeEditing = !this.isNodeEditing;
    },
    getParameter(name) {
      let param = this.localNode.parameters.find((item) => item.key === name);
      if (!param) {
        param = { key: name, value: "" };
        this.localNode.parameters.push(param);
      }
      return param;
    },
    deleteNode() {
      this.$emit("node-deleted", this.localNode);
    },
    submit() {
      this.toggleNodeEditing();
      this.$emit("node-updated", this.localNode);
    },
    cancel() {
      this.toggleNodeEditing();
    },
  },
};
</script>
