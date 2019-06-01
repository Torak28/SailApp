<template>
  <b-container class="UserPanel">
    <b-container v-if="breachAlert == false">
      <br>
      <b-tabs content-class="mt-3" align="center">
        <b-tab title="Rent" active>
          <br>
          <h1 class='title'>Rent page</h1>
          <br>
          <br>
          <h4>
            Sort by Distance:
            <b-button-group>
              <b-button variant="primary" v-on:click="sortNear()"> Nearest </b-button>
              <b-button variant="primary" v-on:click="sortFurth()"> Furthest </b-button>
            </b-button-group>
          </h4>
          <br>
          <div v-for="(companyForm, index) in companyForms" :key="index">
            <CompanyCard :parentUserForm=userForm :parentCompanyForm=companyForm :parentRent=rent @SendRentFormParent="FillRentForm" />
            <br>
          </div>
        </b-tab>
        <b-tab title="User Panel">
          <br>
          <h1 class='title'>User Panel</h1>
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
          <b-button block variant="success" v-on:click="Change()">Change</b-button>
          <b-button block variant="warning" to="/">Go back</b-button>
        </b-tab>
        <b-tab title="Upcoming events">
          <br>
          <h1 class='title'>Upcoming events</h1>
          <br>
          <b-container v-if="rent == true">
            <b-card title="Rent">
              <b-card-text>
                Your Water Cenre: <b>{{rentForm.gear_centre_id}}</b>
                <br>
                Rent Start: <b>{{rentForm.rent_start.toISOString().substring(0, 10)}}</b>
                <br>
                Type of Gear: <b>{{rentForm.gear_id}}</b>
              </b-card-text>
              <b-button variant="primary" v-on:click="Return()">Return</b-button>
            </b-card>
          </b-container>
          <b-container v-if="rent == false">
            <br>
            <br>
            <h2 align="center">Rent something!</h2>
          </b-container>
        </b-tab>
      </b-tabs>
    </b-container>
    <b-container v-if="breachAlert == true || breachAlert == null">
      <h3>You have to be log in to view this site, go to the <b-link href="/">homepage</b-link>!</h3>
    </b-container>
  </b-container>
</template>

<script>
import CompanyCard from '@/components/CompanyCard.vue';

export default {
  name: "UserPanel",
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
      rentForm: {
        rent_start: '',
        rent_end: '',
        rent_amount: 1,
        is_returned: null,
        user_id: '',
        gear_id: '',
        gear_centre_id: ''
      },
      companyForms: [],
      changeName: true,
      changeSurname: true,
      changeTel: true,
      changeEmail: true,
      changePassword: true,
      changeCompanyName: true,
      changeCompanyTel: true,
      breachAlert: null,
      rent: false
    }
  },
  methods: {
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
    changePasswordProp(){
      this.changePassword = !this.changePassword;
    },
    calcDistAll(all){
      for (let i = 0; i < all.length; i++) {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function(position) {
            let currentLat = position.coords.latitude;
            let currentLng = position.coords.longitude;
            let R = 6371e3; // metres
            let φ1 = Number(all[i].latitude) * Math.PI / 180;
            let φ2 = currentLat * Math.PI / 180;
            let Δφ = (currentLat-Number(all[i].latitude)) * Math.PI / 180;
            let Δλ = (currentLng-Number(all[i].longtitude)) * Math.PI / 180;

            let a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                    Math.cos(φ1) * Math.cos(φ2) *
                    Math.sin(Δλ/2) * Math.sin(Δλ/2);
            let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            
            let d = R * c;
            let result = Math.floor(d/1000);
            all[i].dist = result;
          });
        } else {
          //console.log('No geolocation error');
        }
      }
    },
    sortNear(){
      this.companyForms.sort((a, b) => (a.dist > b.dist) ? 1 : -1);
    },
    sortFurth(){
      this.companyForms.sort((a, b) => (a.dist < b.dist) ? 1 : -1);
    },
    FillRentForm (value) {
      this.rentForm.rent_start = value.rent_start;
      this.rentForm.rent_end = value.rent_end;
      this.rentForm.rent_amount = value.rent_amount;
      this.rentForm.is_returned = value.is_returned;
      this.rentForm.user_id = value.user_id;
      this.rentForm.gear_id = value.gear_id;
      this.rentForm.gear_centre_id = value.gear_centre_id;
      this.rent = true;
    },
    Return(){
      this.rentForm.rent_start = '';
      this.rentForm.rent_end = '';
      this.rentForm.rent_amount = '';
      this.rentForm.is_returned = '';
      this.rentForm.user_id = '';
      this.rentForm.gear_id = '';
      this.rentForm.gear_centre_id = '';
      this.rent = false;
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
          this.userForm.name = response.data.first_name;
          this.userForm.surname = response.data.last_name;
          this.userForm.phone = response.data.phone_number;
        })
      .catch(function (error){
        console.log(error);
      });
    },
    getAllCentreData(){
      this.getAllCentreBasicData(this.companyForms);
      //this.getCentrePic(this.companyForms);
    },
    getAllCentreBasicData(data){
      var obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/centre/getCentres", {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/centre/getCentres',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          for (let i = 0; i < response.data.length; i++) {
            console.log('poczatek pierwszego Centrum --- z getCentreData');
            data.push({
              name: response.data[i].centre_name,
              latitude: response.data[i].latitude,
              longtitude: response.data[i].longitude,
              phone: response.data[i].phone_number,
              centre_id: response.data[i].centre_id,
              gears: [{"id":"0","gearType":"Kayak","gearAmount":"20","gearCost":"250"}],
              //photo: obj.getCentrePic(response.data[i].centre_id)
            });
            console.log('koniec pierwszego Centrum --- z getCentreData');
          }
          return this.getAllCentrePicId(data);
        })
      .catch(function (error){
        console.log(error);
      });
    },
    getAllCentrePicId(data){
      console.log(JSON.stringify(data));
      console.log(data.length);
      //console.log(data[0].centre_id);
      for (let i = 0; i < data.length; i++) {
        console.log('Zdjątko ID');
        this.axios
        .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/user/getPicturesIdsOfCentre/" + data[i].centre_id.toString(), {
          headers: {
            'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/user/getPicturesIdsOfCentre',
            'accept': 'application/json',
            'Authorization': "Bearer " + this.user.token
          }
        })
        .then(
          (response) => {
            data[0].photo = response.data[0].picture_id.toString();
            return this.getAllCentrePic(data);
          })
        .catch(function (error){
          console.log(error);
        });
      }
      //console.log('Data z getAllCentrePicId' + JSON.stringify(data));
    },
    getAllCentrePic(data){
      console.log('Data z getAllCentrePic' + JSON.stringify(data));
      for (let i = 0; i < data.length; i++) {
        console.log('Zdjątko');
        this.axios
        .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/user/getPicture/" + data[i].photo.toString(), {
          headers: {
            'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/user/getPicture',
            'accept': 'application/json',
            'Authorization': "Bearer " + this.user.token
          }
        })
        .then(
          (response) => {
            data[0].photo = response.data[0].picture_id.toString();
          })
        .catch(function (error){
          console.log(error);
        });
      }
    }
  },
  created () {
    if(this.user.role == 'user'){
      this.userForm.role = this.user.role;
      this.userForm.email = this.user.login;
      this.userForm.password = this.user.password;
      this.userForm.checkPassword = this.user.password;

      //Dane uzytkownika
      this.getUserData();
      this.getAllCentreData(this.companyForms);
      this.breachAlert = false;

      //Dane wypozyczalni
      function xd(){
        console.log('func');
      }

      


      xd();
      console.log('pomiedzy');

      //Zdjecie wypozyczalni
      /*for (let i = 0; i < this.companyForms.length; i++) {
        let url = 'https://projekt-gospodarka-backend.herokuapp.com/user/getPicturesIdsOfCentre/' + this.companyForms[i].centre_id
        console.log('inside');
        console.log(url);
        this.axios
        .get(url, {
          headers: {
            'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/accounts/login',
            'accept': 'application/json',
            'Authorization': "Bearer " + this.user.token
          }
        })
        .then(
          (response) => {
            console.log(response);
          })
        .catch(function (error){
          console.log(error);
        });
      }*/

      /*this.companyForms.push({
        name: 'XKajak',
        latitude: '50.3483816',
        longtitude: '18.915717599999994',
        phone: '666 777 888',
        photo: 'https://picsum.photos/450/300/?image=10',
        gears: [{"id":"0","gearType":"Kayak","gearAmount":"20","gearCost":"250"}]
      });
      this.companyForms.push({
        name: 'KajaX',
        latitude: '51.1078852',
        longtitude: '17.03853760000004',
        phone: '123 123 123',
        photo: 'https://picsum.photos/450/300/?image=20',
        gears: [{"id":"0","gearType":"Water bikes","gearAmount":"10","gearCost":"25"},{"id":"1","gearType":"Sailboat","gearAmount":"5","gearCost":"50"}]
      });
      this.companyForms.push({
        name: 'Opole',
        latitude: '50.671062',
        longtitude: '17.926126',
        phone: '123 123 123',
        photo: 'https://picsum.photos/450/300/?image=30',
        gears: [{"id":"0","gearType":"Boats","gearAmount":"20","gearCost":"250"},{"id":"1","gearType":"Sailboat","gearAmount":"5","gearCost":"50"}]
      });*/
      this.calcDistAll(this.companyForms);
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
</style>