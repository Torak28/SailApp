<template>
  <b-container class="OwnerPanel">
    <br>
    <br>
    <h1 class='title'>Preview</h1>
    <br>
    <br>
    <b-row>
      <b-col>
        <!--TODO-->
        <b-card no-body class="overflow-hidden">
          <b-row no-gutters>
            <b-col md="6">
              <b-card-img src="https://picsum.photos/400/400/?image=20" class="rounded-0"></b-card-img>
            </b-col>
            <b-col md="6">
              <b-card-body title="Horizontal Card">
                <b-card-text>
                  This is a wider card with supporting text as a natural lead-in to additional content.
                  This content is a little bit longer.
                </b-card-text>
              </b-card-body>
            </b-col>
          </b-row>
        </b-card>
        <!--TODO-->
        <b-form-file class="block" v-model="form.photoFile" placeholder="Company photo" drop-placeholder="Drop file here..." />
      </b-col>
    </b-row>
    <br>
    <h1 class='title'>Company</h1>
    <br>
    <br>
    <b-row>
      <b-col sm="9">
        <b-form-input :readonly='changeCompanyName' class="block" type="text" v-model='form.companyName' placeholder="Company Name" />
        <b-form-input :readonly='changeCompanyTel' class="block" type="tel" v-model='form.companyTel' placeholder="Company Phone Number" />
      </b-col>
      <b-col sm="3">
        <b-button block class="block" variant="info" v-on:click="changeCompanyNameProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changeCompanyTelProp()">Change</b-button>
      </b-col>
      <b-col>
      <gmap-map class='block' v-if="form.type == 'Owner'" :center= "center" :zoom= "zoom" style="width:100%;  height: 600px;" >
      <gmap-marker
        :position="{
          lat: Number(this.form.lattitude),
          lng: Number(this.form.longtitude),
        }"
        />
      </gmap-map>
      <GmapAutocomplete class="AutoBlockOff" :placeholder="place" @place_changed="setPlace" />
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
        <b-form-input  v-if="form.type == 'Owner'" class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
        <b-button v-if="form.type == 'Owner'" class='block' block variant="danger" v-on:click="deleteGear(gear.id)">Delete This Gear</b-button>
        <hr>
      </b-container>
      <b-button block variant="primary" v-on:click="addGear()">Add new Gear</b-button>
      </b-col>
    </b-row>
    <br>
    <h1 class='title'>User Options</h1>
    <br>
    <br>
    <b-row>
      <b-col sm="9">
        <b-form-input :readonly='changeName' class="block" type="text" v-model='form.name' placeholder="First Name" />
        <b-form-input :readonly='changeSurname' class="block" type="text" v-model='form.surname' placeholder="Second Name" />
        <b-form-input :readonly='changeTel' class="block" type="tel" v-model='form.phone' placeholder="Phone number" />
        <b-form-input :readonly='changeEmail' class="block" type="email" v-model='form.email' placeholder="Email" />
        <b-form-input :readonly='changePassword' class="block" type="password" v-model='form.password' placeholder="Password" />
        <b-form-input :readonly='changePassword' class="block" type="password" v-model='form.checkPassword' placeholder="Repeat Password" />
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
  </b-container>
</template>

<script>
import apiKey from '../json/secret.json';
export default {
  name: "OwnerPanel",
  props: ['user'],
  data() {
    return {
      form: {
        type: 'Owner',
        name: 'Jarosław',
        surname: 'Ciołek-Żelechowski',
        phone: '666 615 315',
        email: 'zelechowski28@gmail.com',
        password: 'dupa123',
        checkPassword: 'dupa123',
        companyName: 'KajaX',
        companyTel: '123 123 123',
        photoFile: 'xd',
        lattitude: '51.1078852',
        longtitude: '17.03853760000004',
        howManyGear: null,
        gears: [{"id":"0","gearType":"Water bikes","gearAmount":"10","gearCost":"25"},{"id":"1","gearType":"Sailboat","gearAmount":"5","gearCost":"50"}]
      },
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
      zoom: 6
    }
  },
  methods: {
    addGear(){
      this.form.gears.push({ id: this.counter.toString(), gearType: '',  gearAmount: '', gearCost: ''});
      this.counter++;
    },
    deleteGear(){
      this.form.gears.pop();
      this.counter--;
      if(this.counter < 0){
        this.counter = 0;  
      }
    },
    changeNameProp(){
      this.changeName = false;
    },
    changeSurnameProp(){
      this.changeSurname = false;
    },
    changeTelProp(){
      this.changeTel = false;
    },
    changeEmailProp(){
      this.changeEmail = false;
    },
    changeCompanyNameProp(){
      this.changeCompanyName = false;
    },
    changeCompanyTelProp(){
      this.changeCompanyTel = false;
    },
    changePasswordProp(){
      this.changePassword = false;
    },
    setPlace(place) {
      this.place = place;
      //this.form.place = place.address_components[0].long_name;
      this.form.lattitude = this.place.geometry.location.lat();
      this.form.longtitude = this.place.geometry.location.lng();
    },
    Change(){
      // TODO: zmienić
      //console.log("User " + JSON.stringify(this.form) + " changed");
    }
  },
  mounted () {
    console.log(apiKey.API_KEY);
    this.axios
      .get("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + this.form.lattitude + "," + this.form.longtitude + "&key=" + apiKey.API_KEY)
      .then((response) => {
        this.place = response.data.results[0].address_components[3].long_name
      })
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
</style>