<template>
  <div class="card my-2">
    <div class="card-body">
      <div class="d-flex">
        <div class="flex-grow-1">
          <span class="align-middle">{{ sender.name }}</span>
        </div>
        <EditIcon @icon-click="toggleSenderEditing" />
        <DeleteIcon @icon-click="deleteSender" />
      </div>
      <EditableSender
        @sender-form-submitted="onSenderFormSubmitted"
        @sender-form-cancelled="toggleSenderEditing"
        v-if="isSenderEditing"
        :sender="sender"
      />
    </div>
  </div>
</template>

<script>
import EditableSender from "@/components/senders/EditableSender.vue";
import DeleteIcon from "@/components/icon/DeleteIcon.vue";
import EditIcon from "@/components/icon/EditIcon.vue";

export default {
  components: {
    DeleteIcon,
    EditIcon,
    EditableSender,
  },
  props: ["sender"],
  data() {
    return {
      isSenderEditing: false,
    };
  },
  methods: {
    toggleSenderEditing() {
      this.isSenderEditing = !this.isSenderEditing;
    },
    onSenderFormSubmitted(sender) {
      this.$store
        .dispatch("updateSender", {
          name: this.sender.name,
          item: sender,
        })
        .then(() => {
          this.toggleSenderEditing();
        });
    },
    deleteSender() {
      this.$store.dispatch("deleteSender", this.sender);
    },
  },
};
</script>
