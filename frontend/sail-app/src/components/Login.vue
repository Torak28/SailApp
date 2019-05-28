
<template>
  <b-container id='Login'>
    <b-row>
      <b-form-input class="block" type="text" v-model='form.login' placeholder="Login" />
      <b-form-input class="block" type="password" v-model='form.password' placeholder="Password" />
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
      form: {
        login: '',
        password: '',
        role: ''
      }
    }
  },
  methods: {
    loginToAccount() {
      let databaseLogin = this.$parent.mockAccount2.login;
      let databasePass = this.$parent.mockAccount2.password;
      let databaseRole = this.$parent.mockAccount2.role;
      this.form.role = databaseRole;
      if(this.form.login != "" && this.form.password != "") {
        if(this.form.login == databaseLogin && this.form.password == databasePass) {
          this.$parent.authenticated = true;
          if(this.form.role == 'User'){
            this.$router.push({ name: "UserPanel", params: {user: this.form} });
          }else{
            this.$router.push({ name: "OwnerPanel", params: {user: this.form} });
          }
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