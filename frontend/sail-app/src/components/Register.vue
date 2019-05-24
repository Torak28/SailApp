
<template>
  <b-container class="Register">
    <br>
    <br>
    <b-row>
      <b-form-input type="text" v-model='name' placeholder="First Name" />
      <br>
      <br>
      <b-form-input type="text" v-model='surname' placeholder="Second Name" />
      <br>
      <br>
      <b-form-input type="tel" v-model='phone' placeholder="Phone number" />
      <br>
      <br>
      <b-form-input type="email" v-model='email' placeholder="Email" />
      <br>
      <br>
      <b-form-input type="password" v-model='password' placeholder="Password" />
      <br>
      <br>
      <b-form-input type="password" v-model='checkPassword' placeholder="Replay Password" />
      <br>
      <br>
      <b-button block variant="success" to="/">Register</b-button>
      <b-button block variant="warning" to="/">Go back</b-button>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      name: '',
      surname: '',
      phone: '',
      email: '',
      password: '',
      checkPassword: ''
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