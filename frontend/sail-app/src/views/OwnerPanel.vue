<template>
  <b-container class="OwnerPanel">
    <h1 class='title'>Owner Panel page!</h1>
    <h3> Login: {{user.login}} </h3>
    <h3> Password: {{user.login}} </h3>
    <h3> Role: {{user.role}} </h3>
    <b-row>
      <b-form-input class="block" type="text" v-model='form.name' placeholder="First Name" />
      <b-form-input  class="block" type="text" v-model='form.surname' placeholder="Second Name" />
      <b-form-input  class="block" type="tel" v-model='form.phone' placeholder="Phone number" />
      <b-form-input  class="block" type="email" v-model='form.email' placeholder="Email" />
      <b-form-input  class="block" type="password" v-model='form.password' placeholder="Password" />
      <b-form-input  class="block" type="password" v-model='form.checkPassword' placeholder="Repeat Password" />
      <b-form-input  class="block" type="text" v-model='form.companyName' placeholder="Company Name" />
      <b-form-input  class="block" type="text" v-model='form.lattitude' placeholder="Lattitude" />
      <b-form-input  class="block" type="text" v-model='form.longtitude' placeholder="Longtitude" />
      <b-button block variant="primary" v-on:click="addGear()">Add new Gear</b-button>
      <b-button class='block' block variant="danger" v-on:click="deleteGear()">Delete Last Gear</b-button>
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
        <b-form-input class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
        <b-form-input class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
        <hr>
      </b-container>

      <b-button block variant="success">Change</b-button>
      <b-button block variant="warning" to="/">Go back</b-button>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "OwnerPanel",
  props: ['user'],
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