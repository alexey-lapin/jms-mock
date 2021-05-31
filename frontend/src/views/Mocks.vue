<template>
  <h3>Mocks</h3>
  <Mock v-for="mock in mocks" :key="mock.name" :mock="mock" />
  <div v-if="!isCreating" class="card my-2" style="width: 20rem">
    <div class="card-body d-flex justify-content-center align-items-center">
      <AddIcon @icon-click="toggleCreating" />
    </div>
  </div>
  <NewMock
    v-if="isCreating"
    @mock-created="onMockCreated"
    @mock-cancelled="onMockCancelled"
  />
</template>

<script>
import { mapGetters } from "vuex";
import Mock from "@/components/mocks/Mock.vue";
import NewMock from "@/components/mocks/NewMock.vue";
import AddIcon from "@/components/icon/AddIcon.vue";

export default {
  components: {
    Mock,
    NewMock,
    AddIcon,
  },
  data() {
    return {
      isCreating: false,
    };
  },
  computed: {
    ...mapGetters(["mocks"]),
  },
  methods: {
    toggleCreating() {
      this.isCreating = !this.isCreating;
    },
    onMockCreated() {
      this.toggleCreating();
    },
    onMockCancelled() {
      this.toggleCreating();
    },
  },
};
</script>
