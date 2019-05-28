<template>
  <b-container class="UserPanel">
    <br>
    <b-tabs content-class="mt-3">
      <b-tab title="Rent" active>
        <br>
        <h1 class='title'>Rent page</h1>
      </b-tab>
      <b-tab title="User Panel">
        <br>
        <h1 class='title'>User Panel</h1>
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
        <b-button block variant="success" v-on:click="Change()">Change</b-button>
        <b-button block variant="warning" to="/">Go back</b-button>
      </b-tab>
      <b-tab title="Upcoming events">
        <br>
        <h1 class='title'>Upcoming events</h1>
      </b-tab>
    </b-tabs>
  </b-container>
</template>

<script>
export default {
  name: "UserPanel",
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
        photoFile: 'https://picsum.photos/400/400/?image=20',
        lattitude: '51.1078852',
        longtitude: '17.03853760000004',
        gears: [{"id":"0","gearType":"Water bikes","gearAmount":"10","gearCost":"25"},{"id":"1","gearType":"Sailboat","gearAmount":"5","gearCost":"50"}]
      },
      changeName: true,
      changeSurname: true,
      changeTel: true,
      changeEmail: true,
      changePassword: true,
      changeCompanyName: true,
      changeCompanyTel: true,
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
  }
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>