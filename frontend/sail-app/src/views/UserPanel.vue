<template>
  <b-container class="UserPanel">
    <b-container v-if="breachAlert == false">
      <br>
      <b-tabs content-class="mt-3">
        <b-tab title="Rent" active>
          <br>
          <h1 class='title'>Rent page</h1>
          <div v-for="(companyForm, index) in companyForms" :key="index">
            <CompanyCard :parentUserForm=userForm :parentCompanyForm=companyForm />
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
      companyForms: [],
      changeName: true,
      changeSurname: true,
      changeTel: true,
      changeEmail: true,
      changePassword: true,
      changeCompanyName: true,
      changeCompanyTel: true,
      breachAlert: null
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
    Change(){
      // TODO: zmienić
      //console.log("User " + JSON.stringify(this.form) + " changed");
    }
  },
  created () {
    if(this.user.role == 'User'){
      this.userForm.role = 'User';
      this.userForm.name = 'Jarosław';
      this.userForm.surname = 'Ciołek-Żelechowski';
      this.userForm.phone = '666 615 315';
      this.userForm.email = 'zelechowski28@gmail.com';
      this.userForm.password = 'dupa123';
      this.userForm.checkPassword = 'dupa123';
      this.breachAlert = false;
      this.companyForms.push({
        name: 'KajaX',
        latitude: '51.1078852',
        longtitude: '17.03853760000004',
        phone: '123 123 123',
        photo: 'https://picsum.photos/450/300/?image=20',
        gears: [{"id":"0","gearType":"Water bikes","gearAmount":"10","gearCost":"25"},{"id":"1","gearType":"Sailboat","gearAmount":"5","gearCost":"50"}]
      });
      this.companyForms.push({
        name: 'XKajak',
        latitude: '51.1043057',
        longtitude: '17.0472932',
        phone: '666 777 888',
        photo: 'https://picsum.photos/450/300/?image=10',
        gears: [{"id":"0","gearType":"Kayak","gearAmount":"20","gearCost":"250"}]
      });
      
      
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