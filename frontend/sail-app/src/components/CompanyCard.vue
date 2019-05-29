<template>
  <b-container id='CompanyCard'>
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
  </b-container>
</template>

<script>
export default {
  name: "CompanyCard",
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
        geras: ''
      },
      place: null,
      dist: null,
      dropdownTextGear: "Choose Gear to Rent",
      modalDate: '',
      modalStartTime: '',
      modalEndTime: ''
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
      this.rentForm.user_id = this.userForm.name;
      this.rentForm.gear_id = this.dropdownTextGear;
      this.rentForm.gear_centre_id = this.companyForm.companyName;
    }
  },
  created () {
    this.userForm.role = '';
    this.userForm.name = '';
    this.userForm.surname = '';
    this.userForm.phone = '';
    this.userForm.email = '';
    this.userForm.password = '';
    this.userForm.checkPassword = '';
    this.companyFrom.name = '';
    this.companyFrom.latitude = '';
    this.companyFrom.longtitude = '';
    this.companyFrom.phone = '';
    this.companyFrom.photo = '';
    this.companyFrom.geras = '';
    this.place = null;
    this.dist = null;
    this.dropdownTextGear = "Choose Gear to Rent";
    this.modalDate = '';
    this.modalStartTime = '';
    this.modalEndTime = '';
  }
};
</script>

<style scope>
  .card:hover{
    cursor: pointer;
  }
</style>