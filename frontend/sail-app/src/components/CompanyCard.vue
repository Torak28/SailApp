<template>
  <div id='CompanyCard'>
    <!--b-card :img-src=companyForm.photo  img-alt="Card image" img-width='50%' img-left :title=companyForm.name v-b-modal=companyForm.name-->
    <b-card :img-src=companyForm.photo  img-alt="Card image" img-width='50%' img-left :title=companyForm.name @click="emitModal()" ref="card">
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
    <b-modal :id=this.companyForm.name title='Rent Form' @ok="handleOk">
      <b-alert :show=parentRent variant="danger">You already Rented Something. You can't rent anymore.</b-alert>
      <b-alert :show=badContent variant="danger">Fill in all fields</b-alert>
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
      <b-form-input :readonly=parentRent class="block" type="date" :min="minDate" v-model="modalDate" />
      Start Time:
      <b-form-input :readonly=parentRent class="block" type="time" step='1800' v-model="modalStartTime" />
      End Time:
      <b-form-input :readonly=parentRent class="block" type="time" step='1800' :min="modalStartTime" v-model="modalEndTime" />
      <b-dropdown split variant="primary" split-variant="outline-primary" id="dropdown-1" :text=dropdownTextGear class="m-md-2">
        <b-dropdown-item v-for="(gear, index) in this.gearTypes" :key="index" v-on:click="chooseGear(index)">{{ gear }}</b-dropdown-item>
      </b-dropdown>
      <br>
      Amount:
      <b-form-input :readonly=parentRent class="block" type="number" :min="min" :max="max" v-model="amount" />
      <br>
      Cost: {{this.rentForm.cost}}
      <template slot="modal-footer" slot-scope="{ ok, cancel }">
      <b-button size="sm" variant="success" @click="ok()">
        Rent
      </b-button>
      <b-button size="sm" variant="danger" @click="cancel()">
        Cancel
      </b-button>
    </template>

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
    parentRent: Boolean
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
        rent_amount: '',
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
      currentLng: null,
      amount: 1,
      max: 2,
      min: 1,
      minDate: null,
      badContent: false,
      index: null
    }
  },
  methods: {
    chooseGear(index){
      this.dropdownTextGear = this.gearTypes[index];
      let tmpV = null;
      for (let i = 0; i < this.companyForm.gears.length; i++) {
        if(this.companyForm.gears[i].gearType == this.dropdownTextGear){
          tmpV = this.companyForm.gears[i];
          break;
        }
      }
      this.max = tmpV.gearAmount;
      this.index = index;
      this.rentForm.cost = this.companyForm.gears[this.index].gearCost * Number(this.amount);
    },
    handleOk(){
      this.rentForm.rent_start = new Date(this.modalDate + 'T' + this.modalStartTime);
      this.rentForm.rent_end = new Date(this.modalDate + 'T' + this.modalEndTime);
      this.rentForm.is_returned = false;
      this.rentForm.rent_amount = this.amount;
      this.rentForm.user_id = this.userForm.name;
      this.rentForm.gear_id = this.companyForm.gears[this.index].id;
      this.rentForm.gear_centre_id = this.parentCompanyForm.centre_id;
      this.rentForm.place = this.place;
      this.rentForm.cost = this.companyForm.gears[this.index].gearCost * this.rentForm.rent_amount;

      console.log(JSON.stringify(this.rentForm));

      if(this.modalDate == '' || this.modalStartTime == ''  || this.modalEndTime == '' || this.rentForm.gear_id == 'Choose Gear to Rent'){
        this.badContent = true;
      }else{
        this.badContent = false;
        if(!this.parentRent){
          this.sendRentForm(event);
          this.modalDate = '';
          this.modalStartTime = '';
          this.modalEndTime = '';
          this.dropdownTextGear = "Choose Gear to Rent";
          this.rentForm.rent_start = '';
          this.rentForm.rent_end = '';
          this.rentForm.is_returned ='' 
          this.rentForm.rent_amount = '';
          this.rentForm.user_id = '';
          this.rentForm.gear_id = '';
          this.rentForm.gear_centre_id = '';
        }
      }
    },
    sendRentForm(){
      this.$emit('SendRentFormParent', this.rentForm);
      console.log('Wypożyczenie!');
    },
    emitModal(){
      this.$root.$emit('bv::show::modal', this.companyForm.name, '#card');
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
    this.minDate = new Date().toISOString().split("T")[0];
    this.dropdownTextGear = "Choose Gear to Rent";
    this.modalDate = '';
    this.modalStartTime = '';
    this.modalEndTime = '';
    let tmp = [];
    for (let i = 0; i < this.companyForm.gears.length; i++) {
      tmp.push(Object.values(this.companyForm.gears[i])[1]);
    }
    this.gearTypes = tmp;
    this.axios
      .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.companyForm.latitude + "," + this.companyForm.longtitude + "&key=" + apiKey.API_KEY2)
      .then(
        (response) => {
          this.place = response.data.results[0].address_components[3].long_name + ', '
                       + response.data.results[0].address_components[1].long_name + ', '
                       + response.data.results[0].address_components[0].long_name;
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
    }
  },
   watch: {
     'amount': function(newV){
       if(this.index == null){
        this.rentForm.cost = 0;
      }else{
        this.rentForm.cost = this.companyForm.gears[this.index].gearCost * Number(newV);
      }
     },
    'parentCompanyForm.name': function(newV){
      this.companyForm.name = newV;
    },
    'parentCompanyForm.phone': function(newV){
      this.companyForm.phone = newV;
    },
    'parentCompanyForm.photo': function(newV){
      this.companyForm.photo = newV;
    },
    'parentCompanyForm.latitude': function(newV){
      this.companyForm.latitude = newV;
    },
    'parentCompanyForm.longtitude': function(newV){
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
      }
      this.axios
        .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.companyForm.latitude + "," + this.companyForm.longtitude + "&key=" + apiKey.API_KEY2)
        .then(
          (response) => {
            this.place = response.data.results[0].address_components[3].long_name;
          })
    },
    'parentCompanyForm.gears': function(newV){
      let tmp = [];
      for (let i = 0; i < newV.length; i++) {
        tmp.push(Object.values(newV[i])[1]);
      }
      this.gearTypes = tmp;
      this.companyForm.gears = newV;
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