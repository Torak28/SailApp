<template>
  <b-container class="home">
    <b-row>
      <b-col lg='8' order='2' order-lg='1'>
        <Ads />
      </b-col>
      <b-col align-self="center" lg='4' order='1' order-lg='2'>
        <Login />
      </b-col>
    </b-row>
  </b-container>
</template>

<script>
import Login from "@/components/Login.vue";
import Ads from "@/components/Ads.vue";

export default {
  name: "home",
  components: {
    Login,
    Ads
  }
};
</script>
