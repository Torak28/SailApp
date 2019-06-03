<template>
  <b-container class="OwnerRegister">
    <div v-if="breachAlert == false">
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
    </b-row>
    <b-row>
      <b-form-checkbox id="checkbox-1" v-model="status" name="checkbox-1" class='block'> I accept the <b-link href="https://ezelechowska-psycholog.pl/PolitykaPrywatnosci.pdf">terms and use</b-link> </b-form-checkbox>
      <b-button block class='btnClass' variant="success" v-on:click="registerOwnerAccoount()">Register Water Centre</b-button>
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
  name: "OwnerRegister",
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
      place: null,
      status: false,
      center: { lat: 52.237049, lng: 21.017532 },
      zoom: 6,
      breachAlert: null
    }
  },
  methods: {
    registerOwnerAccoount() {
      if(this.status == true){
        if(this.form.companyName != '' &&  this.form.lattitude != '' &&  this.form.longtitude != '' &&  this.form.companyTel != '' &&  this.form.photoFile != ''){
          var obj = this;
          let data = new FormData();
          data.append("centre_name", this.form.companyName);
          data.append("latitude", this.form.lattitude);
          data.append("longitude", this.form.longtitude);
          data.append("phone_number", this.form.companyTel);
          this.axios
          .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/owner/addWaterCentre", data, {
            headers: {
              'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/owner/addWaterCentre',
              'Content-Type': 'multipart/form-data',
              'accept': 'application/json',
              'Authorization': "Bearer " + this.form.token
            }
          })
          .then(
            (response) => {
              obj.getCentreId();
            });
        
        }else{
          this.$parent.noData = true;
          this.$parent.cookieData = false;
          this.$scrollTo('#alert', 200, {offset: -500});
        }
      }else{
        this.$parent.cookieData = true;
        this.$parent.noData = false;
        this.$scrollTo('#alert', 200, {offset: -500});
      }
    },
    setPlace(place) {
      this.place = place;
      this.form.lattitude = this.place.geometry.location.lat();
      this.form.longtitude = this.place.geometry.location.lng();
      this.center = {lat: this.form.lattitude, lng: this.form.longtitude};
      this.zoom = 9
    },
    addPicture(data){
      let obj = this;
      this.axios
      .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/owner/addPicture", data, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/owner/addPicture',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.form.token
        }
      })
      .then(
        (response) => {
          this.$router.replace({ name: "GearRegistration", params: {user: obj.form} });
        });
    },
    getCentreId(){
      let obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/owner/getMyCentres", {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/owner/getMyCentres',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.form.token
        }
      })
      .then(
        (response) => {
          obj.form.centre_id = response.data[0].centre_id;
          let data = new FormData();
          data.append("centre_id", this.form.centre_id);
          data.append("file", this.form.photoFile);
          this.addPicture(data);
        });
    }
  },
  created() {
    if(this.user.type.toLowerCase() == 'owner'){
      var obj = this;
      let data = new FormData();
      data.append("email", this.user.email);
      data.append("password", this.user.password);
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
        })
      this.breachAlert = false;
    }else{
      this.breachAlert = true;
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
