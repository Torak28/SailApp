<template>
  <div id='CompanyCard'>
    <b-card :img-src=form.photoFile  img-alt="Card image" img-width='50%' img-left :title=form.companyName ref="card">
      <b-card-text>
        <font-awesome-icon icon="phone" /> {{form.companyTel}}
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
  </div>
</template>

<script>
import apiKey from '../json/secret.json';

export default {
  name: "CompanyCard",
  props: {
    parentForm: Object
  },
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
      },
      place: null,
      dist: null,
      gearTypes: '',
      pic_id: null
    }
  },
  methods: {
    getPictureId(){
      let obj = this;
      this.axios
        .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/user/getPicturesIdsOfCentre/" + this.form.centre_id, {
          headers: {
            'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/user/getPicturesIdsOfCentre/',
            'Authorization': "Bearer " + this.form.token
          }
        })
        .then(
          (response) => {
            obj.pic_id = response.data[0].picture_id;
            obj.getPicture();
          })
    },
    getPicture(){
      let obj = this;
      this.axios
        .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/user/getPicture/" + this.pic_id, {
          headers: {
            'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/user/getPicturesIdsOfCentre/',
            'Authorization': "Bearer " + this.form.token
          }
        })
        .then(
          (response) => {
            obj.form.photoFile = response.data.picture_link;
            console.log('Internal Error JSON? ' + JSON.stringify(response));
          })
    }
  },
  created () {
    
    this.form.centre_id = this.parentForm.centre_id;
    this.form.companyName = this.parentForm.companyName;
    this.form.companyTel = this.parentForm.companyTel;
    this.form.lattitude = this.parentForm.lattitude;
    this.form.longtitude = this.parentForm.longtitude;
    this.form.gears = this.parentForm.gears;
    this.form.token = this.parentForm.token;
    
    //this.form.photoFile = this.form.photoFile;
    this.getPictureId();

    //Ilosc są zle nie to jest wyciagane w gearTypach
    //Zdjecia nie ma

    let tmp = [];
    for (let i = 0; i < this.form.gears.length; i++) {
      tmp.push(Object.values(this.form.gears[i])[0]);
    }
    this.gearTypes = tmp;
    this.axios
      .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.form.lattitude + "," + this.form.longtitude + "&key=" + apiKey.API_KEY2)
      .then(
        (response) => {
          console.log(response);
          this.place = response.data.results[0].address_components[3].long_name + ', '
                       //+ response.data.results[0].address_components[1].long_name + ' '
                       + response.data.results[0].address_components[0].long_name;
        })
    if (navigator.geolocation) {
      var obj = this;
      navigator.geolocation.getCurrentPosition(function(position) {
        obj.currentLat = position.coords.latitude;
        obj.currentLng = position.coords.longitude;
        var R = 6371e3; // metres
        var φ1 = Number(obj.form.lattitude) * Math.PI / 180;
        var φ2 = obj.currentLat * Math.PI / 180;
        var Δφ = (obj.currentLat-Number(obj.form.lattitude)) * Math.PI / 180;
        var Δλ = (obj.currentLng-Number(obj.form.longtitude)) * Math.PI / 180;

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
    this.form.photoFile = this.form.photoFile;
  }
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>