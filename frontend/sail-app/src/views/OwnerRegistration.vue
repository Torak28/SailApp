<template>
  <b-container class="OwnerRegistration">
    <h1 class='title'>Register Company:</h1>
    <br>
    <br>
    <br>
    <b-alert :show=noData variant="danger">{{noDataMsg}}</b-alert>
    <b-alert :show=noGear variant="danger">{{noGearMsg}}</b-alert>
    <b-alert :show=cookieData variant="danger">{{cookieDataMsg}}</b-alert>
    <OwnerRegister :user=user />
  </b-container>
</template>

<script>
import OwnerRegister from "@/components/OwnerRegister.vue";

export default {
  name: "OwnerRegistration",
  props: ['user'],
  components: {
    OwnerRegister
  },
  data() {
    return {
      noData: false,
      noGear: false,
      cookieData: false,
      cookieDataMsg: "Accept terms and use",
      noDataMsg: "All data must be present",
      noGearMsg: "Gear tab is not filled properly"
    }
  }
};
</script>

<style scoped>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>

