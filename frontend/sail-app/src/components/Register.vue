<template>
  <b-container id='alert' class="Register">
    <b-row>
      <b-form-input class="block" type="text" v-model='form.name' placeholder="First Name" />
      <b-form-input class="block" type="text" v-model='form.surname' placeholder="Second Name" />
      <b-form-input class="block" type="tel" v-model='form.phone' placeholder="Phone number" />
      <b-form-input class="block" type="email" v-model='form.email' placeholder="Email" />
      <b-form-input class="block" type="password" v-model='form.password' placeholder="Password" />
      <b-form-input class="block" type="password" v-model='form.checkPassword' placeholder="Repeat Password" />
      <b-form-checkbox class="block" v-model="form.type" unchecked-value="Owner" value="User" name="check-button" switch> {{ form.type }} </b-form-checkbox>
    </b-row>
    <b-row>
      <b-form-checkbox id="checkbox-1" v-model="status" name="checkbox-1" class='block'> I accept the <b-link href="https://ezelechowska-psycholog.pl/PolitykaPrywatnosci.pdf">terms and use</b-link> </b-form-checkbox>
      <b-button block class='btnClass' variant="success" v-on:click="regiterUserAccount()">Register</b-button>
      <b-button block class='btnClass' variant="warning" to="/">Go back</b-button>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      form: {
        type: 'User',
        name: '',
        surname: '',
        phone: '',
        email: '',
        password: '',
        checkPassword: '',
      },
      status: false
    }
  },
  methods: {
    regiterUserAccount() {
      if(this.status == true){
          if(this.form.name != '' && this.form.surname != '' && this.form.phone != '' && this.form.email != '' &&  this.form.password != '' && this.form.checkPassword != ''){
            if(this.form.password != this.form.checkPassword){
              this.$parent.wrongPass = true;
              this.$parent.noData = false;
              this.$parent.noGear = false;
              this.$parent.cookieData = false;
              this.$scrollTo('#alert', 200, {offset: -500});
            }else{
                var obj = this;
                let data = new FormData();
                data.append("first_name", this.form.name);
                data.append("last_name", this.form.surname);
                data.append("email", this.form.email);
                data.append("password", this.form.password);
                data.append("phone_number", this.form.phone);
                data.append("role", this.form.type);
                this.axios
                .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/accounts/register", data, {
                  headers: {
                    'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/register',
                    'Content-Type': 'multipart/form-data',
                    'accept': 'application/json'
                  }
                })
                .then(
                  (response) => {
                    if(this.form.type.toLowerCase() == 'user'){
                      this.$router.replace({ name: "home" });
                    }else if(this.form.type.toLowerCase() == 'owner'){
                      this.$router.replace({ name: "OwnerRegistration", params: {user: obj.form} });
                    }
                })
                .catch(function (error){
                  obj.$parent.wrongData = true;
                  obj.$parent.noData = false;
                });
            }
          }else{
            this.$parent.wrongPass = false;
            this.$parent.noData = true;
            this.$parent.noGear = false;
            this.$parent.cookieData = false;
            this.$scrollTo('#alert', 200, {offset: -500});
          }
      }else{
        this.$parent.cookieData = true;
        this.$parent.wrongPass = false;
        this.$parent.noGear = false;
        this.$parent.noData = false;
        this.$scrollTo('#alert', 200, {offset: -500});
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