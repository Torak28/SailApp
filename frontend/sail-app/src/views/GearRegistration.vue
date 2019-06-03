<template>
  <b-container class="GearRegistration">
    <h1 class='title'>Add Gear to Company:</h1>
    <br>
    <br>
    <br>
    <b-alert :show=noGear variant="danger">{{noGearMsg}}</b-alert>
    <GearRegister :user=user />
  </b-container>
</template>

<script>
import OwnerRegister from "@/components/OwnerRegister.vue";

export default {
  name: "GearRegistration",
  props: ['user'],
  components: {
    OwnerRegister
  },
  data() {
    return {
      noGear: false,
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

