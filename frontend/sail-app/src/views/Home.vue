<template>
  <b-container class="home">
    <b-row>
      <b-col lg='8' order='2' order-lg='1'>
        <Ads />
      </b-col>
      <b-col align-self="center" lg='4' order='1' order-lg='2'>
        <b-alert :show=noData variant="danger">{{noDataMsg}}</b-alert>
        <b-alert :show=wrongData variant="danger">{{wrongDataMsg}}</b-alert>
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
  },
  data() {
    return {
      authenticated: false,
      noData: false,
      wrongData: false,
      noDataMsg: "A login and password must be present",
      wrongDataMsg: "The login and / or password is incorrect",
      // TODO: zmienić
      mockAccount1: {
        login: "asd",
        password: "asd",
        //role: 'User'
        role: 'Owner'
      }
    }
  }
};
</script>
