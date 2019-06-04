<template>
  <b-container class="Denied">
    <b-container v-if="breachAlert == false">
      <br>
      <h1 class='title'>Denied page</h1>
      <br>
      <br>
      <h3>You account is denied. Our admin decide that You cannot be a member of Sail-Familly. Contact us if You have any questions: <b-link href="mailto:sail@sail.com">sail@sail.com</b-link></h3>
      <br>
      <b-button block class='btnClass' variant="warning" to="/">Go back</b-button>
    </b-container>
    <b-container v-if="breachAlert == true || breachAlert == null">
      <h3>You have to be log in to view this site, go to the <b-link href="/">homepage</b-link>!</h3>
    </b-container>
  </b-container>
</template>

<script>
export default {
  name: "Denied",
  props: ['user'],
  data() {
    return {
      userForm: {
        login: '',
        password: '',
        role: '',
        token: ''
      },
      breachAlert: null
    }
  },
  created () {
    if(this.user.role == 'owner'){
      this.userForm.role = this.user.role;
      this.userForm.login = this.user.login;
      this.userForm.password = this.user.password;
      this.userForm.token = this.user.token;

      this.breachAlert = false;
    }else{
      this.breachAlert = true;
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