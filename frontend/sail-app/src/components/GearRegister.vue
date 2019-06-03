<template>
  <b-container class="OwnerRegister">
    <div v-if="breachAlert == false">
    <b-row>
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input  class="block" type="text" v-model="gear.gearName" placeholder="Type of gear e.g. water bikes" />
        <b-form-input  class="block" type="number" v-model="gear.gearQuantity" placeholder="How many of those You have?" />
        <b-form-input  class="block" type="number" v-model="gear.gearPrice" placeholder="How much cost 1 hour?" />
        <b-button class='block' block variant="danger" v-on:click="deleteGear(gear.id)">Delete This Gear</b-button>
        <hr>
      </b-container>
    </b-row>
    <b-row>
      <b-button id="Add" class='btnClass'  block variant="primary" v-on:click="addGear()" v-scroll-to="{el: '#Add', duration: 2000}">Add new Gear</b-button>
      <b-button block class='btnClass' variant="success" v-on:click="registerGear()">Add Gear to Your Water Centre</b-button>
      <b-button block class='btnClass' variant="warning" to="/">Go back</b-button>
    </b-row>
    </div>
    <div v-if="breachAlert == true || breachAlert == null">
      <h3>You have to be log in to view this site, go to the <b-link href="/">homepage</b-link>!</h3>
    </div>
  </b-container>
</template>

<script>
export default {
  name: "GearRegister",
  props: ['user'],
  data() {
    return {
      form: {
        centre_id: '',
        companyName: '',
        companyTel: '',
        photoFile: null,
        lattitude: '',
        longtitude: '',
        gears: [],
        token: null,
        type: 'Owner'
      },
      howManyNow: 0,
      place: null,
      status: false,
      token: null,
      breachAlert: null
    }
  },
  methods: {
    registerGear(){
      if(this.form.gears.length == 0){
        this.$parent.noGear = true;
        this.$scrollTo('#alert', 200, {offset: -500});
      }else{
        let flag = false;
        for (let i = 0; i < this.form.gears.length; ++i) {
          if(Object.values(this.form.gears[i]).includes('') == true){
            flag = true;
          }
        }
        if(flag == false){
          this.$parent.noGear = false;
          var obj = this;
          for (let i = 0; i < this.form.gears.length; i++) {
            console.log(JSON.stringify(this.form.gears[i]));
            let data = new FormData();
            data.append("centre_id", this.form.centre_id);
            data.append('gear_name', this.form.gears[i].gearName);
            data.append('gear_quantity', this.form.gears[i].gearQuantity);
            data.append('gear_price', this.form.gears[i].gearPrice);
            this.axios
              .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/gear/addGear", data, {
                headers: {
                  'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/gear/addGear',
                  'Content-Type': 'multipart/form-data',
                  'accept': 'application/json',
                  'Authorization': "Bearer " + this.form.token
                }
              })
              .then(
                (response) => {
                  //console.log(JSON.stringify(response));
                  //Tutaj lecimy dalej
                  this.$router.replace({ name: "OwnerRegistrationSuccess", params: {user: obj.form} });
                });
          }
        }else{
          this.$parent.noGear = true;
          this.$scrollTo('#alert', 200, {offset: -500});
        }
      }
    },
    addGear(){
      this.form.gears.push({ gearName: '',  gearPrice: '', gearQuantity: ''});
      this.howManyNow++;
    },
    deleteGear(elemId){
      const index = this.form.gears.map(e => e.id).indexOf(elemId);
      this.form.gears.splice(index, 1);
      this.howManyNow--;
      if(this.howManyNow < 0){
        this.howManyNow = 0;  
      }
    }
  },
  created() {
    if(this.user.type.toLowerCase() == 'owner'){
      this.form.centre_id = this.user.centre_id;
      this.form.companyName = this.user.companyName;
      this.form.companyTel = this.user.companyTel;
      this.form.photoFile = this.user.photoFile;
      this.form.lattitude = this.user.lattitude;
      this.form.longtitude = this.user.longtitude;
      this.form.gears = this.user.gears;
      this.form.token = this.user.token;
      this.breachAlert = false;
    }else{
      this.breachAlert = true;
    }
  }
}
</script>