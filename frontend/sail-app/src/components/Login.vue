
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
      this.form.role = 'User';
      if(this.form.login != "" && this.form.password != "") {
        //Tutaj lecimy z koksem
        var obj = this;
        this.axios
        .post("https://projekt-gospodarka-backend.herokuapp.com/accounts/login", {
          headers: {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
            'Access-Control-Allow-Headers': 'X-Requested-With,content-type',
            'Access-Control-Allow-Credentials': true
	        },
          email: obj.form.login,
          password: obj.form.password
        })
        .then(
          (response) => {
            obj.$parent.authenticated = true;
            if(obj.form.role == 'User'){
              obj.$router.push({ name: "UserPanel", params: {user: obj.form} });
            }else{
              obj.$router.push({ name: "OwnerPanel", params: {user: obj.form} });
            }
          })
        .catch(function (error){
          obj.$parent.wrongData = true;
          obj.$parent.noData = false;
        });
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