
<template>
  <b-container id='Login'>
    <b-row>
      <b-form-input type="text" v-model='login' placeholder="Login" />
      <br>
      <br>
      <b-form-input type="password" v-model='password' placeholder="Password" />
      <br>
      <br>
      <b-button block variant="success">Login</b-button>
    </b-row>
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
