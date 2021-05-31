<template>
  <div class="card my-2" :class="{ read: !isEditing }">
    <div class="card-body">
      <div class="d-flex align-items-center">
        <Switch
          :modelValue="mock.isEnabled"
          @update:modelValue="onMockToggled"
        />
        <div class="flex-grow-1">
          <span>{{ mock.name }}</span>
        </div>
        <TriggerIcon
          v-if="isTriggerable()"
          @icon-click="toggleTriggering"
          :is-enabled="mock.isEnabled"
        />
        <EditIcon @icon-click="toggleEditing" />
        <DeleteIcon @icon-click="deleteMock" />
      </div>
      <EditableMock
        v-if="isEditing"
        :mock="localMock"
        @mock-form-submitted="onMockFormSubmitted"
        @mock-form-cancelled="onMockFormCancelled"
      />
    </div>
  </div>
  <VueFinalModal v-if="isTriggerable()" v-model="isTriggering">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Trigger mock</h5>
          <button
            type="button"
            class="btn-close"
            @click="toggleTriggering"
          ></button>
        </div>
        <div class="modal-body">
          <div class="container-fluid">
            <div class="row">
              <div class="col-9">
                <h5>Compose</h5>
                <div>
                  <Composer
                    :mock="mock"
                    @message-sent="historyCounter++"
                    :message="historyMessage"
                  />
                </div>
              </div>
              <div class="col-3">
                <div class="d-flex align-items-center justify-content-between">
                  <h5>History</h5>
                  <button
                    class="btn btn-xs btn-outline-danger"
                    @click="onClear"
                  >
                    Clear
                  </button>
                </div>
                <div>
                  <MessageHistory
                    :mock="mock"
                    :counter="historyCounter"
                    @message-clicked="onMessageClicked"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </VueFinalModal>
</template>

<script>
import MessageHistory from "@/components/destinations/MessageHistory.vue";
import EditableMock from "@/components/mocks/EditableMock.vue";
import Composer from "@/components/mocks/Composer.vue";
import DeleteIcon from "@/components/icon/DeleteIcon.vue";
import EditIcon from "@/components/icon/EditIcon.vue";
import TriggerIcon from "@/components/icon/TriggerIcon.vue";
import Switch from "@/components/ui/Switch.vue";

export default {
  components: {
    MessageHistory,
    EditableMock,
    Composer,
    DeleteIcon,
    EditIcon,
    TriggerIcon,
    Switch,
  },
  props: ["mock"],
  data() {
    return {
      historyCounter: 0,
      historyMessage: {},
      isEditing: false,
      isTriggering: false,
      localMock: {
        id: this.mock.id,
        name: this.mock.name,
        nodes: this.mock.nodes,
      },
    };
  },
  methods: {
    toggleEditing() {
      this.isEditing = !this.isEditing;
    },
    deleteMock() {
      this.$store.dispatch("deleteMock", this.localMock);
    },
    onMockFormSubmitted(mock) {
      this.$store
        .dispatch("updateMock", {
          name: this.localMock.name,
          item: mock,
        })
        .then(() => this.toggleEditing());
    },
    onMockFormCancelled() {
      this.toggleEditing();
    },
    isTriggerable() {
      return (
        this.mock.nodes[0].type === "composer" ||
        this.mock.nodes[0].type === "groovy"
      );
    },
    toggleTriggering() {
      this.isTriggering = !this.isTriggering;
    },
    onMockToggled() {
      this.$store.dispatch("toggleMock", this.localMock);
    },
    onMessageClicked(message) {
      this.historyMessage = message;
    },
    onClear() {
      this.$store
        .dispatch("deleteHistory", this.mock)
        .then(() => (this.historyCounter = this.historyCounter + 1));
    },
  },
};
</script>

<style scoped>
.read {
  width: 20rem;
}
.btn-xs {
  font-size: 0.8rem;
  padding: 0.1rem 0.2rem;
}
</style>
