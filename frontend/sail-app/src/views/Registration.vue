<template>
  <b-container class="Registration">
    <h1 class='title'>Register:</h1>
    <br>
    <br>
    <br>
    <b-alert :show=noData variant="danger">{{noDataMsg}}</b-alert>
    <b-alert :show=wrongPass variant="danger">{{wrongPassMsg}}</b-alert>
    <b-alert :show=noGear variant="danger">{{noGearMsg}}</b-alert>
    <b-alert :show=wrongData variant="danger">{{wrongDataMsg}}</b-alert>
    <b-alert :show=cookieData variant="danger">{{cookieDataMsg}}</b-alert>
    <Register />
  </b-container>
</template>

<script>
import Register from "@/components/Register.vue";

export default {
  name: "Registration",
  components: {
    Register
  },
  data() {
    return {
      noData: false,
      noGear: false,
      wrongPass: false,
      cookieData: false,
      wrongData: false,
      wrongDataMsg: 'You provide worng data',
      cookieDataMsg: "Accept terms and use",
      noDataMsg: "All data must be present",
      noGearMsg: "Gear tab is not filled properly",
      wrongPassMsg: "Passwords do not match",
    }
  }
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>
