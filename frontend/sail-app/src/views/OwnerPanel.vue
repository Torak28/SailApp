<template>
  <b-container class="OwnerPanel">
    <div v-if="breachAlert == false">
      <div v-if="loading == false">
        <br>
        <b-tabs content-class="mt-3" align="center">
          <b-tab title="Preview" active>
            <br>
            <h1 class='title'>Preview</h1>
            <br>
            <br>
            <b-row>
              <b-col>
                <CompanyCard :parentUserForm=userForm :parentCompanyForm=companyForm />
                <br>
                <b-form-file class="block" v-model="companyForm.photo" placeholder="Company photo" drop-placeholder="Drop file here..." />
                <br>
                <br>
                <b-button block class='btnClass' variant="success" v-on:click="ChangePic()">Change Picture</b-button>
                <b-button block variant="warning" to="/">Homepage</b-button>
              </b-col>
            </b-row>
            <br>
          </b-tab>
          <b-tab title="Company Data">
            <br>
            <h1 class='title'>Company Data</h1>
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
            <b-button block class='btnClass' variant="success" v-on:click="ChangeCompData()">Save Changed Data</b-button>
            <b-button block variant="warning" to="/">Homepage</b-button>
          </b-tab> 
          <b-tab title="User Data">
            <h1 class='title'>User Data</h1>
            <br>
            <h6>User: {{userForm.email}}</h6>
            <br>
            <b-row>
              <b-col sm="9">
                <b-form-input :readonly='changeName' class="block" type="text" v-model='userForm.name' placeholder="First Name" />
                <b-form-input :readonly='changeSurname' class="block" type="text" v-model='userForm.surname' placeholder="Second Name" />
                <b-form-input :readonly='changeTel' class="block" type="tel" v-model='userForm.phone' placeholder="Phone number" />
                <b-form-input :readonly='changePassword' class="block" type="password" v-model='userForm.password' placeholder="Password" />
                <b-form-input :readonly='changePassword' class="block" type="password" v-model='userForm.checkPassword' placeholder="Repeat Password" />
              </b-col>
              <b-col sm="3">
                <b-button block class="block" variant="info" v-on:click="changeNameProp()">Change</b-button>
                <b-button block class="block" variant="info" v-on:click="changeSurnameProp()">Change</b-button>
                <b-button block class="block" variant="info" v-on:click="changeTelProp()">Change</b-button>
                <b-button block class="block" variant="info" v-on:click="changePasswordProp()">Change</b-button>
                <b-button block class="block" variant="info" v-on:click="changePasswordProp()">Change</b-button>
              </b-col>
            </b-row>
            <b-row>
            </b-row>
            <br>
            <br>
            <b-button block variant="success" v-on:click="Change()">Change</b-button>
            <b-button block variant="warning" to="/">Homepage</b-button>
          </b-tab>
          <b-tab title="Gear">
            <br>
            <h1 class='title'>Gear</h1>
            <br>
            <h6>Gear that existed before last login is locked. It can be changed only by contacting our Admin</h6>
            <br>
            <b-container v-for="gear in companyForm.gears" :key="gear.id">
              <b-form-input readonly class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
              <b-form-input readonly class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
              <b-form-input readonly class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
              <b-button disabled class='block' block variant="danger" v-on:click="deleteGear(gear.id)">Delete This Gear</b-button>
              <hr>
            </b-container>
            <b-container v-for="newGear in companyForm.newGears" :key="newGear.id">
              <b-form-input class="block" type="text" v-model="newGear.gearType" placeholder="Type of gear e.g. water bikes" />
              <b-form-input class="block" type="number" v-model="newGear.gearAmount" placeholder="How many of those You have?" />
              <b-form-input class="block" type="number" v-model="newGear.gearCost" placeholder="How much cost 1 hour?" />
              <b-button class='block' block variant="danger" v-on:click="deleteGear(newGear.id)">Delete This Gear</b-button>
              <hr>
            </b-container>
            <b-button id="Add" class='btnClass' block variant="primary" v-on:click="addGear()">Add new Gear</b-button>
            <b-button class='block' block variant="success" v-on:click="saveGear()">Save This Gear</b-button>

          </b-tab>
          <b-tab title="Pending Rents">
            <br>
            <h1 class='title'>Pending Rents</h1>
            <br>
            <br>
            <!-- Dodać Rent-->
          </b-tab>
          <b-tab title="Rents">
            <br>
            <h1 class='title'>Rents</h1>
            <br>
            <br>
          </b-tab>
        </b-tabs>
      </div>
      <div v-if="loading == true" class="text-center">
        <b-spinner style="width: 200px; height: 200px;" class="m-25" variant="primary" type="grow"></b-spinner>
      </div>
    </div>
    <div v-if="breachAlert == true || breachAlert == null">
      <h3>You have to be log in to view this site, go to the <b-link href="/">homepage</b-link>!</h3>
    </div>
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
        gears: '',
        newGears: [],
        centre_id: '',
        dist: ''
      },
      dropdownTextGear: "Choose Gear to Rent",
      dropdownTextTime: "How Long",
      modalDate: '',
      modalStartTime: '',
      modalEndTime: '',
      howManyNow: 0,
      counter: 0,
      changeSurname: true,
      changeTel: true,
      changeName: true,
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
      time: null,
      loading: true
    }
  },
  methods: {
    ChangeCompData(){
      let obj = this;
      let data = new FormData();
      data.append("centre_id", this.companyForm.centre_id);
      data.append("centre_name", this.companyForm.name);
      data.append("latitude", this.companyForm.latitude);
      data.append("longitude", this.companyForm.longtitude);
      data.append("phone_number", this.companyForm.phone);
      this.axios
      .put("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/owner/editCentre", data, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/owner/editCentre',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.calcPlace();
        });
    },
    ChangePic(){
      let obj = this;
      let data = new FormData();
      data.append("centre_id", this.companyForm.centre_id);
      data.append("file", this.companyForm.photo);
      this.axios
      .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/owner/addPicture", data, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/owner/addPicture',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.getCentrePic();
          obj.companyForm.photo = '';
        });
    },
    addGear(){
      this.companyForm.newGears.push({ id: this.counter.toString(), gearType: '',  gearAmount: '', gearCost: ''});
      this.howManyNow++;
      this.counter++;
    },
    deleteGear(elemId){
      const index = this.companyForm.newGears.map(e => e.id).indexOf(elemId);
      this.companyForm.newGears.splice(index, 1);
      this.howManyNow--;
      if(this.howManyNow < 0){
        this.howManyNow = 0;  
      }
    },
    saveGear(){
      this.addAllNewGear();
      },
    addAllNewGear(){
      var obj = this;
      for (let i = 0; i < this.companyForm.newGears.length; i++) {
        let data = new FormData();
        data.append("centre_id", this.companyForm.centre_id);
        data.append('gear_name', this.companyForm.newGears[i].gearType);
        data.append('gear_quantity', this.companyForm.newGears[i].gearAmount);
        data.append('gear_price', this.companyForm.newGears[i].gearCost);
        this.axios
          .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/gear/addGear", data, {
            headers: {
              'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/gear/addGear',
              'Content-Type': 'multipart/form-data',
              'accept': 'application/json',
              'Authorization': "Bearer " + this.user.token
            }
          })
          .then(
            (response) => {
              obj.addToGears();
          })
        }
    },
    addToGears(){
      this.companyForm.gears =  [];
      let obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/gear/getAllGear/" + this.companyForm.centre_id, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/gear/getAllGear/',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          for (let j = 0; j < response.data.length; j++) {
            obj.companyForm.gears.push({
              "id" : response.data[j].gear_id.toString(),
              "gearType" : response.data[j].gear_name.toString(),
              "gearAmount" : response.data[j].gear_quantity.toString(),
              "gearCost" : response.data[j].gear_price.toString()
            });
          }
          let tmp = [];
          for (let i = 0; i < obj.companyForm.gears.length; i++) {
            tmp.push(Object.values(obj.companyForm.gears[i])[1]);
          }
          obj.gearTypes = tmp;
          obj.companyForm.newGears = [];
          //obj.$router.push({ name: "OwnerPanel", params: {user: obj.form} });
          //this.$router.go()
          //this.$router.reload()

      })
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
    Change(){
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
    },
    calcDist(){
      let obj = this;
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          let currentLat = position.coords.latitude;
          let currentLng = position.coords.longitude;
          let R = 6371e3; // metres
          let φ1 = Number(obj.companyForm.latitude) * Math.PI / 180;
          let φ2 = currentLat * Math.PI / 180;
          let Δφ = (currentLat-Number(obj.companyForm.latitude)) * Math.PI / 180;
          let Δλ = (currentLng-Number(obj.companyForm.longtitude)) * Math.PI / 180;

          let a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                  Math.cos(φ1) * Math.cos(φ2) *
                  Math.sin(Δλ/2) * Math.sin(Δλ/2);
          let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            
          let d = R * c;
          let result = Math.floor(d/1000);
          obj.companyForm.dist = result;
        });
      } else {
        //console.log('No geolocation error');
      }
      this.calcPlace();
    },
    getGear(){
      let obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/gear/getAllGear/" + this.companyForm.centre_id, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/gear/getAllGear/',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          for (let j = 0; j < response.data.length; j++) {
            obj.companyForm.gears.push({
              "id" : response.data[j].gear_id.toString(),
              "gearType" : response.data[j].gear_name.toString(),
              "gearAmount" : response.data[j].gear_quantity.toString(),
              "gearCost" : response.data[j].gear_price.toString()
            });
          }
      })
      obj.calcDist();
    },
    getCentrePic(){
      let obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/user/getPicturesOfCentre/" + this.companyForm.centre_id, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/user/getPicturesOfCentre/',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.companyForm.photo = response.data[response.data.length - 1].picture_link;
      });
      obj.getGear();
    },
    getCentre(){
      var obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/owner/getMyCentres", {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/owner/getMyCentres',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.companyForm.centre_id = response.data[0].centre_id;
          obj.companyForm.latitude = response.data[0].latitude;
          obj.companyForm.longtitude = response.data[0].longitude;
          obj.companyForm.name = response.data[0].name;
          obj.companyForm.phone = response.data[0].phone_number;
          obj.companyForm.gears = [];
          obj.companyForm.photo = null;
          obj.companyForm.dist = null; 
          obj.getCentrePic();
        })
      .catch(function (error){
        console.log(error);
      });
    },
    getUserData(){
      var obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/accounts/getUserData", {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/getUserData',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.userForm.name = response.data.first_name;
          obj.userForm.surname = response.data.last_name;
          obj.userForm.phone = response.data.phone_number;
          obj.getCentre()
        })
      .catch(function (error){
        console.log(error);
      });
    },
    getData(){
      this.getUserData();
    },
    getGearTypes(){
      let tmp = [];
      for (let i = 0; i < this.companyForm.gears.length; i++) {
        tmp.push(Object.values(this.companyForm.gears[i])[1]);
      }
      this.gearTypes = tmp;
      this.howManyNow = this.companyForm.gears.length;
      this.counter = this.companyForm.gears.length;
    },
    calcPlace(){
      let obj = this;
      this.axios
      .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.companyForm.latitude + "," + this.companyForm.longtitude + "&key=" + apiKey.API_KEY2)
      .then(
        (response) => {
          obj.place = response.data.results[0].address_components[3].long_name + ', '
                       + response.data.results[0].address_components[1].long_name + ', '
                       + response.data.results[0].address_components[0].long_name;
          obj.loading = false;
      })
    }
  },
  created () {
    if(this.user.role == 'owner'){

      this.userForm.role = this.user.role;
      this.userForm.email = this.user.login;
      this.userForm.password = this.user.password;
      this.userForm.checkPassword = this.user.password;

      //Dane uzytkownika
      //Dane Centrum
      this.getData()
      this.getGearTypes();
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