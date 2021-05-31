<template>
  <table class="table table-striped lh-1">
    <tbody>
      <tr v-for="(value, key) in headers" :key="key">
        <td>{{ key }}</td>
        <td>{{ value }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  props: ["headers"],
};
</script>
