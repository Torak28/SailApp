
<template>
  <b-container id='Login'>
    <b-row>
      <b-form-input type="text" v-model='login' placeholder="Login" />
      <br>
      <br>
      <b-form-input type="password" v-model='password' placeholder="Password" />
      <br>
      <br>
      <b-button block variant="success" v-on:click="loginToAccount()">Login</b-button>
      <b-button block variant="warning" to="/registration">Register</b-button>
    </b-row>
    <br>
    <br>
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
