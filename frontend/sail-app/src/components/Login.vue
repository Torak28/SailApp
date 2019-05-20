
<template>
  <b-container id='Login'>
    <b-row>
      <b-form-input class="block" type="text" v-model='login' placeholder="Login" />
      <b-form-input class="block" type="password" v-model='password' placeholder="Password" />
      <b-button block variant="success" v-on:click="loginToAccount()">Login</b-button>
      <b-button block variant="warning" to="/registration">Register</b-button>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      login: '',
      password: '',
      user: ''
    }
  },
  methods: {
    loginToAccount() {
      if(this.login != "" && this.password != "") {
        if(this.login == this.$parent.mockAccount.login && this.password == this.$parent.mockAccount.password) {
          this.$parent.authenticated = true;
          this.user = JSON.parse('{"login": "' + this.login + '", "password": "' + this.password + '"}');
          this.$router.push({ name: "UserPanel", params: {user: this.user} });
        } else {
          this.$parent.wrongData = true;
          this.$parent.noData = false;
        }
      } else {
        this.$parent.noData = true;
        this.$parent.wrongData = false;
      }
    }
  }
};
</script>

<style scope>
  .block {
    margin-bottom: 10px;
  }
</style>