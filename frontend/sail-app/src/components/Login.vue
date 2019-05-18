
<template>
  <b-container id='Login'>
    <br>
    <br>
    <b-row>
      <b-form-input type="text" v-model='login' placeholder="Login" />
      <br>
      <br>
      <b-form-input type="password" v-model='password' placeholder="Password" />
      <br>
      <br>
      <b-button block variant="success" to="/userPanel">Login</b-button>
      <b-button block variant="warning" to="/register">Register</b-button>
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
      password: ''
    }
  }
};
</script>
