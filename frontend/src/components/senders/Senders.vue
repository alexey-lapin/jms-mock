<template>
  <h3>Senders</h3>
  <Sender v-for="sender in senders" :key="sender.name" :sender="sender" />
  <div v-if="!isSenderCreating" class="card my-2">
    <div class="card-body d-flex justify-content-center">
      <AddIcon @click="toggleSenderCreating" />
    </div>
  </div>
  <NewSender
    @sender-created="senderCreated"
    @sender-cancelled="senderCancelled"
    v-if="isSenderCreating"
  />
</template>

<script>
import { mapGetters } from "vuex";
import NewSender from "@/components/senders/NewSender.vue";
import Sender from "@/components/senders/Sender.vue";
import AddIcon from "@/components/icon/AddIcon.vue";

export default {
  components: {
    NewSender,
    Sender,
    AddIcon,
  },
  data() {
    return {
      isSenderCreating: false,
    };
  },
  computed: {
    ...mapGetters(["senders"]),
  },
  methods: {
    toggleSenderCreating() {
      this.isSenderCreating = !this.isSenderCreating;
    },
    senderCreated() {
      this.toggleSenderCreating();
    },
    senderCancelled() {
      this.toggleSenderCreating();
    },
  },
};
</script>
