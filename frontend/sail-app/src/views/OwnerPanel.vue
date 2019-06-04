<template>
  <b-container class="OwnerPanel">
    <b-container v-if="breachAlert == false">
      <br>
      <b-tabs content-class="mt-3" align="center">
        <b-tab title="Company Data" active>
          <br>
          <h1 class='title'>Preview</h1>
          <br>
          <br>
          <b-row>
            <b-col>
              <CompanyCard :parentUserForm=userForm :parentCompanyForm=companyForm />
              <br>
              <b-form-file class="block" v-model="companyForm.photo" placeholder="Company photo" drop-placeholder="Drop file here..." />
            </b-col>
          </b-row>
          <br>
          <br>
          <h1 class='title'>Company</h1>
          <br>
          <br>
          <b-row>
            <b-col sm="9">
              <b-form-input :readonly='changeCompanyName' class="block" type="text" v-model='companyForm.name' placeholder="Company Name" />
              <b-form-input :readonly='changeCompanyTel' class="block" type="tel" v-model='companyForm.phone' placeholder="Company Phone Number" />
            </b-col>
            <b-col sm="3">
              <b-button block class="block" variant="info" v-on:click="changeCompanyNameProp()">Change</b-button>
              <b-button block class="block" variant="info" v-on:click="changeCompanyTelProp()">Change</b-button>
            </b-col>
            <b-col>
            <gmap-map class='block' :center= "center" :zoom= "zoom" style="width:100%;  height: 600px;" >
            <gmap-marker
              :position="{
                lat: Number(this.companyForm.latitude),
                lng: Number(this.companyForm.longtitude),
              }"
              />
            </gmap-map>
            <GmapAutocomplete class="AutoBlockOff" :placeholder="place" @place_changed="setPlace" />
            </b-col>
          </b-row>
        </b-tab> 
        <b-tab title="User Data">
          <h1 class='title'>User Data</h1>
          <br>
          <br>
          <b-row>
            <b-col sm="9">
              <b-form-input :readonly='changeName' class="block" type="text" v-model='userForm.name' placeholder="First Name" />
              <b-form-input :readonly='changeSurname' class="block" type="text" v-model='userForm.surname' placeholder="Second Name" />
              <b-form-input :readonly='changeTel' class="block" type="tel" v-model='userForm.phone' placeholder="Phone number" />
              <b-form-input :readonly='changeEmail' class="block" type="email" v-model='userForm.email' placeholder="Email" />
              <b-form-input :readonly='changePassword' class="block" type="password" v-model='userForm.password' placeholder="Password" />
              <b-form-input :readonly='changePassword' class="block" type="password" v-model='userForm.checkPassword' placeholder="Repeat Password" />
            </b-col>
            <b-col sm="3">
              <b-button block class="block" variant="info" v-on:click="changeNameProp()">Change</b-button>
              <b-button block class="block" variant="info" v-on:click="changeSurnameProp()">Change</b-button>
              <b-button block class="block" variant="info" v-on:click="changeTelProp()">Change</b-button>
              <b-button block class="block" variant="info" v-on:click="changeEmailProp()">Change</b-button>
              <b-button block class="block" variant="info" v-on:click="changePasswordProp()">Change</b-button>
              <b-button block class="block" variant="info" v-on:click="changePasswordProp()">Change</b-button>
            </b-col>
          </b-row>
          <b-row>
          </b-row>
          <br>
          <br>
          <b-button block variant="success" v-on:click="Change()">Change</b-button>
          <b-button block variant="warning" to="/">Go back</b-button>
        </b-tab>
        <b-tab title="Gear">
          <br>
          <h1 class='title'>Gear</h1>
          <br>
          <br>
          <b-container v-for="gear in companyForm.gears" :key="gear.id">
            <b-form-input class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
            <b-form-input class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
            <b-form-input class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
            <b-button class='block' block variant="danger" v-on:click="deleteGear(gear.id)">Delete This Gear</b-button>
            <hr>
          </b-container>
          <b-button id="Add" class='btnClass' block variant="primary" v-on:click="addGear()" v-scroll-to="{el: '#Add', duration: 2000, offset: -210}">Add new Gear</b-button>
          <b-button class='block' block variant="success" v-on:click="saveGear()">Save This Gear</b-button>

        </b-tab>
        <b-tab title="Rents">
          <br>
          <h1 class='title'>Rents</h1>
          <br>
          <br>
          <!-- Dodać Rent-->
        </b-tab>
      </b-tabs>
    </b-container>
    <b-container v-if="breachAlert == true || breachAlert == null">
      <h3>You have to be log in to view this site, go to the <b-link href="/">homepage</b-link>!</h3>
    </b-container>
  </b-container>
</template>

<script>
import apiKey from '../json/secret.json';
import CompanyCard from '@/components/CompanyCard.vue';

export default {
  name: "OwnerPanel",
  props: ['user'],
  components: {
    CompanyCard
  },
  data() {
    return {
      userForm: {
        role: '',
        name: '',
        surname: '',
        phone: '',
        email: '',
        password: '',
        checkPassword: ''
      },
      companyForm: {
        name: '',
        latitude: '',
        longtitude: '',
        phone: '',
        photo: '',
        gears: ''
      },
      rentForm: {
        rent_start: '',
        rent_end: '',
        rent_amount: 1,
        is_returned: null,
        user_id: '',
        gear_id: '',
        gear_centre_id: ''
      },
      dropdownTextGear: "Choose Gear to Rent",
      dropdownTextTime: "How Long",
      modalDate: '',
      modalStartTime: '',
      modalEndTime: '',
      howManyNow: 0,
      counter: 0,
      changeName: true,
      changeSurname: true,
      changeTel: true,
      changeEmail: true,
      changePassword: true,
      changeCompanyName: true,
      changeCompanyTel: true,
      place: null,
      center: { lat: 52.237049, lng: 21.017532 },
      zoom: 6,
      gearTypes: '',
      currentLat: null,
      currentLng: null,
      dist: null,
      breachAlert: null,
      date: null,
      time: null
    }
  },
  methods: {
    addGear(){
      this.companyForm.gears.push({ id: this.counter.toString(), gearType: '',  gearAmount: '', gearCost: ''});
      this.howManyNow++;
      this.counter++;
    },
    deleteGear(elemId){
      const index = this.companyForm.gears.map(e => e.id).indexOf(elemId);
      this.companyForm.gears.splice(index, 1);
      this.howManyNow--;
      if(this.howManyNow < 0){
        this.howManyNow = 0;  
      }
    },
    saveGear(){
      let newValue = this.companyForm.gears.pop();
      this.companyForm.gears.push(newValue);
      let tmp = [];
      for (let i = 0; i < this.companyForm.gears.length; i++) {
        tmp.push(Object.values(this.companyForm.gears[i])[1]);
      }
      this.gearTypes = tmp;
    },
    changeNameProp(){
      this.changeName = !this.changeName;
    },
    changeSurnameProp(){
      this.changeSurname = !this.changeSurname;
    },
    changeTelProp(){
      this.changeTel = !this.changeTel;
    },
    changeEmailProp(){
      this.changeEmail = !this.changeEmail;
    },
    changeCompanyNameProp(){
      this.changeCompanyName = !this.changeCompanyName;
    },
    changeCompanyTelProp(){
      this.changeCompanyTel = !this.changeCompanyTel;
    },
    changePasswordProp(){
      this.changePassword = !this.changePassword;
    },
    setPlace(place) {
      let tmp = place;
      this.companyForm.latitude = tmp.geometry.location.lat();
      this.companyForm.longtitude = tmp.geometry.location.lng();
      this.place = tmp.address_components[1].long_name;
      var R = 6371e3; // metres
      var φ1 = Number(this.companyForm.latitude) * Math.PI / 180;
      var φ2 = this.currentLat * Math.PI / 180;
      var Δφ = (this.currentLat-Number(this.companyForm.latitude)) * Math.PI / 180;
      var Δλ = (this.currentLng-Number(this.companyForm.longtitude)) * Math.PI / 180;

      var a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
              Math.cos(φ1) * Math.cos(φ2) *
              Math.sin(Δλ/2) * Math.sin(Δλ/2);
      var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

      var d = R * c;
      this.dist = Math.floor(d/1000);
    },
    chooseGear(index){
      this.dropdownTextGear = this.gearTypes[index];
    },
    handleOk(){
      this.rentForm.rent_start = new Date(this.modalDate + 'T' + this.modalStartTime + '+01:00');
      this.rentForm.rent_end = new Date(this.modalDate + 'T' + this.modalEndTime + '+01:00');
      this.rentForm.is_returned = false;
      this.rentForm.user_id = this.userForm.name;
      this.rentForm.gear_id = this.dropdownTextGear;
      this.rentForm.gear_centre_id = this.companyForm.name;
    },
    Change(){
      //console.log("User " + JSON.stringify(this.form) + " changed");
      var obj = this;
      //User Data
      let dataU = new FormData();
      dataU.append("first_name", this.userForm.name);
      dataU.append("last_name", this.userForm.surname);
      dataU.append("email", this.userForm.email);
      dataU.append("phone_number", this.userForm.phone);
      this.axios
      .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/accounts/changeData", dataU, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/changeData',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      });
      //Password
      let dataP = new FormData();
      dataP.append("password", this.userForm.password);
      this.axios
      .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/accounts/changePassword", dataP, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/changePassword',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      });
    }
  },
  created () {
    if(this.user.role == 'owner'){

      this.userForm.role = this.user.role;
      this.userForm.email = this.user.login;
      this.userForm.password = this.user.password;
      this.userForm.checkPassword = this.user.password;

      var obj = this;
      //Dane uzytkownika
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/accounts/getUserData", {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/login',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          this.userForm.name = response.data.first_name;
          this.userForm.surname = response.data.last_name;
          this.userForm.phone = response.data.phone_number;
        })
      .catch(function (error){
        console.log(error);
      });

      this.companyForm.name = 'KajaX';
      this.companyForm.phone = '123 123 123';
      this.companyForm.photo = 'https://picsum.photos/450/300/?image=20';
      this.companyForm.latitude = '51.1078852';
      this.companyForm.longtitude = '17.03853760000004';
      this.companyForm.gears = [{"id":"0","gearType":"Water bikes","gearAmount":"10","gearCost":"25"},{"id":"1","gearType":"Sailboat","gearAmount":"5","gearCost":"50"}];

      if (navigator.geolocation) {
        var obj = this;
        navigator.geolocation.getCurrentPosition(function(position) {
          obj.currentLat = position.coords.latitude;
          obj.currentLng = position.coords.longitude;
          var R = 6371e3; // metres
          var φ1 = Number(obj.companyForm.latitude) * Math.PI / 180;
          var φ2 = obj.currentLat * Math.PI / 180;
          var Δφ = (obj.currentLat-Number(obj.companyForm.latitude)) * Math.PI / 180;
          var Δλ = (obj.currentLng-Number(obj.companyForm.longtitude)) * Math.PI / 180;

          var a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                  Math.cos(φ1) * Math.cos(φ2) *
                  Math.sin(Δλ/2) * Math.sin(Δλ/2);
          var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

          var d = R * c;
          obj.dist = Math.floor(d/1000);
        });
      } else {
        //console.log('No geolocation error');
      }

      let tmp = [];
      for (let i = 0; i < this.companyForm.gears.length; i++) {
        tmp.push(Object.values(this.companyForm.gears[i])[1]);
      }
      this.gearTypes = tmp;
      this.howManyNow = this.companyForm.gears.length;
      this.counter = this.companyForm.gears.length;

      this.axios
        .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.companyForm.latitude + "," + this.companyForm.longtitude + "&key=" + apiKey.API_KEY2)
        .then(
          (response) => {
            this.place = response.data.results[0].address_components[3].long_name;
          })
      this.breachAlert = false;
    }else{
      this.breachAlert = true;
    }
  }
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
  .AutoBlockOff{
    background-clip: padding-box;
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
    opacity: 1;
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
    width: 100%;
  }
  .card:hover{
    cursor: pointer;
  }
</style>