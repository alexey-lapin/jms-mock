<template>
  <div>
    <div class="row mb-2" v-for="header in headers" :key="header.id">
      <div class="col-sm-5">
        <input
          class="form-control"
          type="text"
          placeholder="name"
          list="jsmHeaders"
          v-model="header.key"
        />
      </div>
      <datalist id="jsmHeaders">
        <option value="jms_correlationId"></option>
        <option value="jms_destination"></option>
        <option value="jms_deliveryMode"></option>
        <option value="jms_expiration"></option>
        <option value="jms_messageId"></option>
        <option value="jms_priority"></option>
        <option value="jms_replyTo"></option>
        <option value="jms_redelivered"></option>
        <option value="jms_type"></option>
        <option value="jms_timestamp"></option>
      </datalist>
      <div class="col-sm-7">
        <div class="d-flex align-items-center">
          <input
            class="form-control"
            type="text"
            placeholder="value"
            v-model="header.value"
          />
          <DeleteIcon class="ms-4 me-2" @icon-click="deleteItem(header.id)" />
        </div>
      </div>
    </div>
    <div class="d-flex justify-content-end">
      <button @click="addItem" type="button" class="btn btn-primary btn-sm">
        Add
      </button>
    </div>
  </div>
</template>

<script>
import { v4 as uuid } from "uuid";
import DeleteIcon from "@/components/icon/DeleteIcon.vue";

export default {
  components: {
    DeleteIcon,
  },
  props: ["modelValue"],
  emits: ["update:modelValue"],
  data() {
    return {
      headers: [...this.modelValue],
    };
  },
  watch: {
    modelValue(value) {
      this.headers = [...value];
    },
  },
  methods: {
    addItem() {
      this.headers.push({
        id: uuid(),
        key: "",
        value: "",
      });
      this.$emit("update:modelValue", [...this.headers]);
    },
    deleteItem(id) {
      this.headers = this.headers.filter((item) => item.id !== id);
      this.$emit("update:modelValue", this.headers);
    },
  },
};
</script>
