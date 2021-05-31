<template>
  <h3>Receivers</h3>
  <Receiver
    v-for="receiver in receivers"
    :key="receiver.name"
    :receiver="receiver"
  />
  <div v-if="!isReceiverCreating" class="card my-2">
    <div class="card-body d-flex justify-content-center">
      <AddIcon @click="toggleReceiverCreating" />
    </div>
  </div>
  <NewReceiver
    @receiver-created="receiverCreated"
    @receiver-cancelled="receiverCancelled"
    v-if="isReceiverCreating"
  />
</template>

<script>
import { mapGetters } from "vuex";
import NewReceiver from "@/components/receivers/NewReceiver.vue";
import Receiver from "@/components/receivers/Receiver.vue";
import AddIcon from "@/components/icon/AddIcon.vue";

export default {
  components: {
    NewReceiver,
    Receiver,
    AddIcon,
  },
  data() {
    return {
      isReceiverCreating: false,
    };
  },
  computed: {
    ...mapGetters(["receivers"]),
  },
  methods: {
    toggleReceiverCreating() {
      this.isReceiverCreating = !this.isReceiverCreating;
    },
    receiverCreated() {
      this.toggleReceiverCreating();
    },
    receiverCancelled() {
      this.toggleReceiverCreating();
    },
  },
};
</script>
