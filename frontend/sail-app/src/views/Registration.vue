<template>
  <b-container class="Registration">
    <h1 class='title'>Register:</h1>
    <Register />
  </b-container>
</template>

<script>
import Register from "@/components/Register.vue";

export default {
  name: "Registration",
  components: {
    Register,
  }
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>
