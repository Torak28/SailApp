
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
      <b-form-input v-if="form.type == 'Owner'" class="block" type="text" v-model='form.companyName' placeholder="Company Name" />

      <GmapAutocomplete v-if="form.type == 'Owner'" class="AutoBlock" placeholder="Place" @place_changed="setPlace" />

      <gmap-map class='block' v-if="form.type == 'Owner'" :center= "center" :zoom= "zoom" style="width:100%;  height: 600px;" >
      <gmap-marker
        v-if="this.place"
        :position="{
          lat: this.place.geometry.location.lat(),
          lng: this.place.geometry.location.lng(),
        }"
        />
      </gmap-map>
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
        <b-button v-if="form.type == 'Owner'" class='block' block variant="danger" v-on:click="deleteGear(gear.id)">Delete Last Gear</b-button>
        <hr>
      </b-container>
      <b-button id="Add" v-if="form.type == 'Owner'" block variant="primary" v-on:click="addGear()" v-scroll-to="{el: '#Add', duration: 2000}">Add new Gear</b-button>

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
        place: '',
        lattitude: '',
        longtitude: '',
        howManyGear: null,
        gears: []
      },
      howManyNow: 0,
      counter: 0,
      place: null,
      center: { lat: 52.237049, lng: 21.017532 },
      zoom: 6
    }
  },
  methods: {
      regiterNewAccount() {
      if(this.form.type == 'User'){
        if(this.form.name != '' && this.form.surname != '' && this.form.phone != '' && this.form.email != '' &&  this.form.password != '' && this.form.checkPassword != ''){
          if(this.form.password != this.form.checkPassword){
            this.$parent.wrongPass = true;
            this.$parent.noData = false;
            this.$scrollTo('#alert', 200, {offset: -500});
          }else{
            //console.log("User " + JSON.stringify(this.form) + " registred");
            this.$router.replace({ name: "home" });
          }
        }else{
          this.$parent.wrongPass = false;
          this.$parent.noData = true;
          this.$scrollTo('#alert', 200, {offset: -500});
        }
      }else{
        if(this.form.name != '' && this.form.surname != '' && this.form.phone != '' && this.form.email != '' &&  this.form.password != '' && this.form.checkPassword != '' && this.form.companyName != '' &&  this.form.lattitude != '' &&  this.form.longtitude != ''){
          if(this.form.password != this.form.checkPassword){
            this.$parent.wrongPass = true;
            this.$parent.noData = false;
            this.$parent.noGear = false;
            this.$scrollTo('#alert', 200, {offset: -500});
          }else{
            if(this.form.gears.length == 0){
              this.$parent.wrongPass = false;
              this.$parent.noGear = true;
              this.$parent.noData = false;
              this.$scrollTo('#alert', 200, {offset: -500});
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
                this.$scrollTo('#alert', 200, {offset: -500});
              }
            }
          }
        }else{
          this.$parent.wrongPass = false;
          this.$parent.noGear = false;
          this.$parent.noData = true;
          this.$scrollTo('#alert', 200, {offset: -500});
        }
      }
    },
    addGear(){
      this.form.gears.push({ id: this.counter.toString(), gearType: '',  gearAmount: '', gearCost: ''});
      this.howManyNow++;
      this.counter++;
    },
    deleteGear(elemId){
      const index = this.form.gears.map(e => e.id).indexOf(elemId);
      this.form.gears.splice(index, 1);
      this.howManyNow--;
      if(this.howManyNow < 0){
        this.howManyNow = 0;  
      }
    },
    setPlace(place) {
      this.place = place;
      this.form.place = place.address_components[0].long_name;
      this.form.lattitude = this.place.geometry.location.lat();
      this.form.longtitude = this.place.geometry.location.lng();
    }
  }
};
</script>

<style scope>
  .AutoBlock{
    background-clip: padding-box;
    background-color: rgb(255, 255, 255);
    border-bottom-color: rgb(206, 212, 218);
    border-bottom-left-radius: 4px;
    border-bottom-right-radius: 4px;
    border-bottom-style: solid;
    border-bottom-width: 1px;
    border-image-outset: 0;
    border-image-repeat: stretch;
    border-image-slice: 100%;
    border-image-source: none;
    border-image-width: 1;
    border-left-color: rgb(206, 212, 218);
    border-left-style: solid;
    border-left-width: 1px;
    border-right-color: rgb(206, 212, 218);
    border-right-style: solid;
    border-right-width: 1px;
    border-top-color: rgb(206, 212, 218);
    border-top-left-radius: 4px;
    border-top-right-radius: 4px;
    border-top-style: solid;
    border-top-width: 1px;
    box-sizing: border-box;
    color: rgb(73, 80, 87);
    display: block;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif;
    font-size: 16px;
    font-weight: 400;
    height: 38px;
    line-height: 24px;
    margin-bottom: 10px;
    margin-left: 0px;
    margin-right: 0px;
    margin-top: 0px;
    overflow: visible;
    overflow-x: visible;
    overflow-y: visible;
    padding-bottom: 6px;
    padding-left: 12px;
    padding-right: 12px;
    padding-top: 6px;
    text-align: start;
    text-justify: inter-word;
    transition-delay: 0s, 0s, 0s;
    transition-duration: 0.15s, 0.15s, 0.15s;
    /*transition-property: border-color, box-shadow, box-shadow;*/
    transition-timing-function: ease-in-out, ease-in-out, ease-in-out;
    width: 632.667px;
  }
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>