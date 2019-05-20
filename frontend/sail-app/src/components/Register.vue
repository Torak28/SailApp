
<template>
  <b-container class="Register">
    <b-row>
      <b-form-input class="block" type="text" v-model='form.name' placeholder="First Name" />
      <b-form-input  class="block" type="text" v-model='form.surname' placeholder="Second Name" />
      <b-form-input  class="block" type="tel" v-model='form.phone' placeholder="Phone number" />
      <b-form-input  class="block" type="email" v-model='form.email' placeholder="Email" />
      <b-form-input  class="block" type="password" v-model='form.password' placeholder="Password" />
      <b-form-input  class="block" type="password" v-model='form.checkPassword' placeholder="Repeat Password" />
      <b-form-checkbox class="block" v-model="form.type" unchecked-value="Owner" value="User" name="check-button" switch> {{ form.type }} </b-form-checkbox>

      <b-form-input  v-if="form.type == 'Owner'" class="block" type="text" v-model='form.companyName' placeholder="Company Name" />
      <b-form-input  v-if="form.type == 'Owner'" class="block" type="text" v-model='form.lattitude' placeholder="Lattitude" />
      <b-form-input  v-if="form.type == 'Owner'" class="block" type="text" v-model='form.longtitude' placeholder="Longtitude" />

      <b-button v-if="form.type == 'Owner'" block variant="primary" v-on:click="addGear()">Add new Gear</b-button>
      <b-button v-if="form.type == 'Owner'" class='block' block variant="danger" v-on:click="deleteGear()">Delete Last Gear</b-button>
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
        <hr>
      </b-container>

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
      form: {
        type: 'User',
        name: '',
        surname: '',
        phone: '',
        email: '',
        password: '',
        checkPassword: '',
        companyName: '',
        lattitude: '',
        longtitude: '',
        howManyGear: null,
        gears: []
      },
      counter: 0
    }
  },
  methods: {
    regiterNewAccount() {
      if(this.form.type == 'User'){
        if(this.form.name != '' && this.form.surname != '' && this.form.phone != '' && this.form.email != '' &&  this.form.password != '' && this.form.checkPassword != ''){
          if(this.form.password != this.form.checkPassword){
            this.$parent.wrongPass = true;
            this.$parent.noData = false;
          }else{
            //console.log("User " + JSON.stringify(this.form) + " registred");
            this.$router.replace({ name: "home" });
          }
        }else{
          this.$parent.wrongPass = false;
          this.$parent.noData = true;
        }
      }else{
        if(this.form.name != '' && this.form.surname != '' && this.form.phone != '' && this.form.email != '' &&  this.form.password != '' && this.form.checkPassword != '' && this.form.companyName != '' &&  this.form.lattitude != '' &&  this.form.longtitude != ''){
          if(this.form.password != this.form.checkPassword){
            this.$parent.wrongPass = true;
            this.$parent.noData = false;
            this.$parent.noGear = false;
          }else{
            if(this.form.gears.length == 0){
              this.$parent.wrongPass = false;
              this.$parent.noGear = true;
              this.$parent.noData = false;
            }else{
              let flag = false;
              for (var i = 0; i < this.form.gears.length; ++i) {
                if(Object.values(this.form.gears[i]).includes('') == true){
                  flag = true;
                }
              }
              if(flag == false) {
                //console.log("Owner " + JSON.stringify(this.form) + " registred");
                this.$router.replace({ name: "home" });
              }else{
                this.$parent.wrongPass = false;
                this.$parent.noGear = true;
                this.$parent.noData = false;
              }
            }
          }
        }else{
          this.$parent.wrongPass = false;
          this.$parent.noGear = false;
          this.$parent.noData = true;
        }
      }
    },
    addGear(){
      this.form.gears.push({ id: this.counter.toString(), gearType: '',  gearAmount: '', gearCost: ''});
      this.counter++;
    },
    deleteGear(){
      this.form.gears.pop();
      this.counter--;
      if(this.counter < 0){
        this.counter = 0;  
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