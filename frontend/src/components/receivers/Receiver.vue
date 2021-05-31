<template>
  <div class="card my-2">
    <div class="card-body">
      <div class="d-flex align-items-center">
        <Switch
          :modelValue="receiver.isEnabled"
          @update:modelValue="onReceiverToggled"
        />
        <div class="flex-grow-1">
          <span>{{ receiver.name }}</span>
        </div>
        <EditIcon @icon-click="toggleReceiverEditing" />
        <DeleteIcon @icon-click="deleteReceiver" />
      </div>
      <EditableReceiver
        @receiver-form-submitted="onReceiverFormUpdated"
        @receiver-form-cancelled="toggleReceiverEditing"
        v-if="isReceiverEditing"
        :receiver="receiver"
      />
    </div>
  </div>
</template>

<script>
import EditableReceiver from "@/components/receivers/EditableReceiver.vue";
import DeleteIcon from "@/components/icon/DeleteIcon.vue";
import EditIcon from "@/components/icon/EditIcon.vue";
import Switch from "@/components/ui/Switch.vue";

export default {
  components: {
    DeleteIcon,
    EditIcon,
    EditableReceiver,
    Switch,
  },
  props: ["receiver"],
  data() {
    return {
      isReceiverEditing: false,
    };
  },
  methods: {
    toggleReceiverEditing() {
      this.isReceiverEditing = !this.isReceiverEditing;
    },
    onReceiverFormUpdated(receiver) {
      this.$store
        .dispatch("updateReceiver", {
          name: this.receiver.name,
          item: receiver,
        })
        .then(() => {
          this.toggleReceiverEditing();
        });
    },
    deleteReceiver() {
      this.$store.dispatch("deleteReceiver", this.receiver);
    },
    onReceiverToggled() {
      this.$store.dispatch("toggleReceiver", this.receiver);
    },
  },
};
</script>
