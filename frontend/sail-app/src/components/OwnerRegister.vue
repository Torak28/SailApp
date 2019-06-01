<template>
  <b-container class="OwnerRegister">
    <b-row>
      <b-form-input class="block" type="text" v-model='form.companyName' placeholder="Company Name" />
      <b-form-input class="block" type="tel" v-model='form.companyTel' placeholder="Company Phone Number" />
      <b-form-file class="block" v-model="form.photoFile" placeholder="Company photo" drop-placeholder="Drop file here..." />
      <GmapAutocomplete class="AutoBlock" placeholder="Place" @place_changed="setPlace" />

      <gmap-map class='block' :center= "center" :zoom= "zoom" style="width:100%;  height: 600px;" >
      <gmap-marker
        v-if="this.place"
        :position="{
          lat: this.place.geometry.location.lat(),
          lng: this.place.geometry.location.lng(),
        }"
        />
      </gmap-map>
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input  class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
        <b-form-input  class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
        <b-form-input  class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
        <b-button class='block' block variant="danger" v-on:click="deleteGear(gear.id)">Delete This Gear</b-button>
        <hr>
      </b-container>
    </b-row>
    <b-row>
      <!--b-button id="Add" class='btnClass'  block variant="primary" v-on:click="addGear()" v-scroll-to="{el: '#Add', duration: 2000}">Add new Gear</b-button-->
      <b-form-checkbox id="checkbox-1" v-model="status" name="checkbox-1" class='block'> I accept the <b-link href="https://ezelechowska-psycholog.pl/PolitykaPrywatnosci.pdf">terms and use</b-link> </b-form-checkbox>
      <b-button block class='btnClass' variant="success" v-on:click="regiterNewAccount()">Register</b-button>
      <b-button block class='btnClass' variant="warning" to="/">Go back</b-button>
    </b-row>
  </b-container>
</template>

<script>
export default {
  name: "OwnerRegister",
  data() {
    return {
      form: {
        companyName: '',
        companyTel: '',
        photoFile: null,
        lattitude: '',
        longtitude: '',
        gears: []
      },
      howManyNow: 0,
      counter: 0,
      place: null,
      status: false,
      center: { lat: 52.237049, lng: 21.017532 },
      zoom: 6
    }
  },
  methods: {
    registerOwnerAccoount() {
      if(this.status == true){
        if(this.form.name != '' && this.form.surname != '' && this.form.phone != '' && this.form.email != '' &&  this.form.password != '' && this.form.checkPassword != '' && this.form.companyName != '' &&  this.form.lattitude != '' &&  this.form.longtitude != '' &&  this.form.companyTel != '' &&  this.form.photoFile != ''){
          if(this.form.password != this.form.checkPassword){
            this.$parent.wrongPass = true;
            this.$parent.noData = false;
            this.$parent.noGear = false;
            this.$parent.cookieData = false;
            this.$scrollTo('#alert', 200, {offset: -500});
          }else{
            if(this.form.gears.length == 0){
              this.$parent.wrongPass = false;
              this.$parent.noGear = true;
              this.$parent.noData = false;
              this.$parent.cookieData = false;
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
                //Nie ma tworzenia Centrum Wodnego :c
                //I sprzetu i wgl :c
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
                    this.$router.replace({ name: "home" });
                  })
                .catch(function (error){
                  obj.$parent.wrongData = true;
                  obj.$parent.noData = false;
                });
              }else{
                this.$parent.wrongPass = false;
                this.$parent.noGear = true;
                this.$parent.noData = false;
                this.$parent.cookieData = false;
                this.$scrollTo('#alert', 200, {offset: -500});
              }
            }
          }
        }else{
          this.$parent.wrongPass = false;
          this.$parent.noGear = false;
          this.$parent.noData = true;
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
    },
    setPlace(place) {
      this.place = place;
      this.form.lattitude = this.place.geometry.location.lat();
      this.form.longtitude = this.place.geometry.location.lng();
    }
  }
}
</script>

<style scoped>
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
    margin-top: 10px;
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
    width: 1110px;
  }
</style>
