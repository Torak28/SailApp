
<template>
  <b-container class="Register">
    <b-row>
      <b-form-input class="block" type="text" v-model='name' placeholder="First Name" />
      <b-form-input  class="block" type="text" v-model='surname' placeholder="Second Name" />
      <b-form-input  class="block" type="tel" v-model='phone' placeholder="Phone number" />
      <b-form-input  class="block" type="email" v-model='email' placeholder="Email" />
      <b-form-input  class="block" type="password" v-model='password' placeholder="Password" />
      <b-form-input  class="block" type="password" v-model='checkPassword' placeholder="Repeat Password" />
      <b-button block variant="success" v-on:click="regiterNewAccount()">Register</b-button>
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
  },
  methods: {
    regiterNewAccount() {
      if(this.name != '' && this.surname != '' && this.phone != '' && this.email != '' &&  this.password != '' && this.checkPassword != ''){
        if(this.password != this.checkPassword){
          this.$parent.wrongPass = true;
          this.$parent.noData = false;
        }else{
          //console.log("User " + this.name + " registred");
          this.$router.replace({ name: "home" });
        }
      }else{
        this.$parent.wrongPass = false;
        this.$parent.noData = true;
      }
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