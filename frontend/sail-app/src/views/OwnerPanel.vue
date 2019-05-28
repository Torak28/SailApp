<template>
  <b-container class="OwnerPanel">
    <h1 class='title'>Owner Panel page!</h1>
    <h3> Login: {{user.login}} </h3>
    <h3> Password: {{user.login}} </h3>
    <h3> Role: {{user.role}} </h3>
    <b-row>
      <GmapAutocomplete class="AutoBlockOff" :placeholder="form.place" @place_changed="setPlace" />
      <b-col sm="9">
        <b-form-input :readonly='changeCompanyName' class="block" type="text" v-model='form.companyName' placeholder="Company Name" />
        <b-form-input :readonly='changeName' class="block" type="text" v-model='form.name' placeholder="First Name" />
        <b-form-input :readonly='changeSurname' class="block" type="text" v-model='form.surname' placeholder="Second Name" />
        <b-form-input :readonly='changeTel' class="block" type="tel" v-model='form.phone' placeholder="Phone number" />
        <b-form-input :readonly='changeEmail' class="block" type="email" v-model='form.email' placeholder="Email" />
        <b-form-input :readonly='changePassword' class="block" type="password" v-model='form.password' placeholder="Password" />
        <b-form-input :readonly='changePassword' class="block" type="password" v-model='form.checkPassword' placeholder="Repeat Password" />
      </b-col>
      <b-col sm="3">
        <b-button block class="block" variant="info" v-on:click="changeCompanyNameProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changeNameProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changeSurnameProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changeTelProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changeEmailProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changePasswordProp()">Change</b-button>
        <b-button block class="block" variant="info" v-on:click="changePasswordProp()">Change</b-button>
      </b-col>
    </b-row>
    <b-row>
      
      <b-button block variant="primary" v-on:click="addGear()">Add new Gear</b-button>
      <b-button class='block' block variant="danger" v-on:click="deleteGear()">Delete Last Gear</b-button>
      <b-container v-for="gear in form.gears" :key="gear.id">
        <b-form-input class="block" type="text" v-model="gear.gearType" placeholder="Type of gear e.g. water bikes" />
        <b-form-input class="block" type="number" v-model="gear.gearAmount" placeholder="How many of those You have?" />
        <b-form-input class="block" type="number" v-model="gear.gearCost" placeholder="How much cost 1 hour?" />
        <hr>
      </b-container>

      <b-button block variant="success" v-on:click="Change()">Change</b-button>
      <b-button block variant="warning" to="/">Go back</b-button>
    </b-row>
  </b-container>
</template>

<script>
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
        lattitude: '51',
        longtitude: '18',
        place: 'Wrocław',
        howManyGear: null,
        gears: []
      },
      counter: 0,
      changeName: true,
      changeSurname: true,
      changeTel: true,
      changeEmail: true,
      changePassword: true,
      changeCompanyName: true,
      place: 'Wrocław',
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
    changePasswordProp(){
      this.changePassword = false;
    },
    setPlace(place) {
      this.place = place;
      this.form.place = place.address_components[0].long_name;
      this.form.lattitude = this.place.geometry.location.lat();
      this.form.longtitude = this.place.geometry.location.lng();
    },
    Change(){
      // TODO: zmienić
      //console.log("User " + JSON.stringify(this.form) + " changed");
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
    width: 1110px;
  }
</style>