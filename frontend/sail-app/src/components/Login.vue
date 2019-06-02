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
        role: '',
        token: ''
      }
    }
  },
  methods: {
    loginToAccount() {
      this.form.role = 'User';
      if(this.form.login != "" && this.form.password != "") {
        //Tutaj lecimy z koksem
        var obj = this;
        let data = new FormData();
        data.append("email", this.form.login);
        data.append("password", this.form.password);
        this.axios
        .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/accounts/login", data, {
          headers: {
            'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/login',
            'Content-Type': 'multipart/form-data',
            'accept': 'application/json'
          }
        })
        .then(
          (response) => {
            obj.form.token = response.data.access_token;
            obj.form.role = response.data.role;
            obj.$parent.authenticated = true;
            if(obj.form.role == 'user'){
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