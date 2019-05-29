<template>
  <div id='CompanyCard'>
    <b-card :img-src=companyForm.photo  img-alt="Card image" img-width='50%' img-left :title=companyForm.name v-b-modal.modal-1>
      <b-card-text>
        <font-awesome-icon icon="phone" /> {{companyForm.phone}}
        <br>
        <font-awesome-icon icon="map-marker-alt" /> {{place}}
        <br>
        <font-awesome-icon icon="road" /> {{dist}} km
        <br>
        <br>
        Gear:
        <ul>
          <li v-for="(gear, index) in this.gearTypes" :key="index">
            {{ gear }}
          </li>
        </ul>
      </b-card-text>
    </b-card>
    <b-modal id="modal-1" title='Rent Form' @ok="handleOk">
      <br>
      <h2>{{companyForm.name}}</h2>
      <br>
      <b-img :src=companyForm.photo alt="Responsive image"></b-img>
      <br>
      <br>
      <font-awesome-icon icon="phone" /> {{companyForm.phone}}
      <br>
      <font-awesome-icon icon="map-marker-alt" /> {{place}}
      <br>
      <font-awesome-icon icon="road" /> {{dist}} km
      <br>
      <br>
      Date:
      <b-form-input class="block" type="date" placeholder="Date" v-model="modalDate" />
      Start Time:
      <b-form-input class="block" type="time" step='1800' v-model="modalStartTime" />
      End Time:
      <b-form-input class="block" type="time" step='1800' :min="modalStartTime" v-model="modalEndTime" />
      <b-dropdown split variant="primary" split-variant="outline-primary" id="dropdown-1" :text=dropdownTextGear class="m-md-2">
        <b-dropdown-item v-for="(gear, index) in this.gearTypes" :key="index" v-on:click="chooseGear(index)">{{ gear }}</b-dropdown-item>
      </b-dropdown>
    </b-modal>
  </div>
</template>

<script>
import apiKey from '../json/secret.json';

export default {
  name: "CompanyCard",
  props: {
    parentUserForm: Object,
    parentCompanyForm: Object,
    parentGearTypes: Array
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
      rentForm: {
        rent_start: '',
        rent_end: '',
        rent_amount: 1,
        is_returned: null,
        user_id: '',
        gear_id: '',
        gear_centre_id: ''
      },
      companyForm: {
        name: '',
        latitude: '',
        longtitude: '',
        phone: '',
        photo: '',
        gears: ''
      },
      place: null,
      dist: null,
      dropdownTextGear: "Choose Gear to Rent",
      modalDate: '',
      modalStartTime: '',
      modalEndTime: '',
      gearTypes: '',
      currentLat: null,
      currentLng: null
    }
  },
  methods: {
    chooseGear(index){
      this.dropdownTextGear = this.gearTypes[index];
    },
    handleOk(){
      this.rentForm.rent_start = new Date(this.modalDate + 'T' + this.modalStartTime + '+01:00');
      this.rentForm.rent_end = new Date(this.modalDate + 'T' + this.modalEndTime + '+01:00');
      this.rentForm.is_returned = false;
      this.rentForm.user_id = this.companyForm.name;
      this.rentForm.gear_id = this.dropdownTextGear;
      this.rentForm.gear_centre_id = this.companyForm.name;
    }
  },
  created () {
    this.userForm.role = this.parentUserForm.type;
    this.userForm.name = this.parentUserForm.name;
    this.userForm.surname = this.parentUserForm.surname;
    this.userForm.phone = this.parentUserForm.phone;
    this.userForm.email = this.parentUserForm.email;
    this.userForm.password = this.parentUserForm.password;
    this.userForm.checkPassword = this.parentUserForm.checkPassword;
    this.companyForm.name = this.parentCompanyForm.name;
    this.companyForm.latitude = this.parentCompanyForm.latitude;
    this.companyForm.longtitude = this.parentCompanyForm.longtitude;
    this.companyForm.phone = this.parentCompanyForm.phone;
    this.companyForm.photo = this.parentCompanyForm.photo;
    this.companyForm.gears = this.parentCompanyForm.gears;
    this.dropdownTextGear = "Choose Gear to Rent";
    this.modalDate = '';
    this.modalStartTime = '';
    this.modalEndTime = '';
    this.gearTypes = this.parentGearTypes;
    this.axios
      .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.companyForm.latitude + "," + this.companyForm.longtitude + "&key=" + apiKey.API_KEY2)
      .then(
        (response) => {
          this.place = response.data.results[0].address_components[3].long_name;
        },
        (error) => { 
          console.log(error) 
        })
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
  },
   watch: {
    'parentCompanyForm.name': function(newV, oldV){
      this.companyForm.name = newV;
    },
    'parentCompanyForm.phone': function(newV, oldV){
      this.companyForm.phone = newV;
    },
    'parentCompanyForm.photo': function(newV, oldV){
      this.companyForm.photo = newV;
    },
    'parentCompanyForm.latitude': function(newV, oldV){
      this.companyForm.latitude = newV;
    },
    'parentCompanyForm.longtitude': function(newV, oldV){
      this.companyForm.longtitude = newV;
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
      this.axios
        .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.companyForm.latitude + "," + this.companyForm.longtitude + "&key=" + apiKey.API_KEY2)
        .then(
          (response) => {
            this.place = response.data.results[0].address_components[3].long_name;
          },
          (error) => { 
            console.log(error) 
          })
    },
    'parentGearTypes': function(newV, oldV){
      this.gearTypes = newV;
    },
    deep: true,
  }
};
</script>

<style scope>
  .card:hover{
    cursor: pointer;
  }
</style>