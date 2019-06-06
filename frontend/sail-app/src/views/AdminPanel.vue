<template>
  <b-container class="AdminPanel">
    <div v-if="breachAlert == false">
      <div v-if="loading == false">
        <br>
        <h1 class='title'>Admin page</h1>
        <br>
        <br>

        <div v-for="(pendingOwner, index) in pendingOwners" :key="index">
          <b-card :title="pendingOwner.first_name + ' ' +  pendingOwner.last_name">
            <b-card-text>
              <br>
              <font-awesome-icon icon="envelope" /> {{pendingOwner.email}}
              <br>
              <font-awesome-icon icon="phone" /> {{pendingOwner.phone_number}}
            </b-card-text>

            <b-button class="btn_space" variant="success" v-on:click="acc(pendingOwner.owner_id)">Accept</b-button>
            <b-button class="btn_space" variant="danger" v-on:click="del(pendingOwner.owner_id)">Delete</b-button>
          </b-card>
          <br>
        </div>
        <div v-if="pendingOwners.length == 0">
          <h3>Thera are no pending Owners!</h3>
        </div>
        <b-button block class='btnClass' variant="warning" to="/">Go back</b-button>
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
export default {
  name: "AdminPanel",
  props: ['user'],
  data() {
    return {
      userForm: {
        email: '',
        password: '',
        role: '',
        token: ''
      },
      breachAlert: null,
      pendingOwners: [],
      loading: true
    }
  },
  methods: {
    getAllPendingOwners(){
      var obj = this;
      this.axios
      .get("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/admin/getPendingOwners", {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/admin/getPendingOwners',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.pendingOwners = [];
          for (let i = 0; i < response.data.length; i++) {
            obj.pendingOwners.push({
              email: response.data[i].email,
              first_name: response.data[i].first_name,
              last_name: response.data[i].last_name,
              owner_id: response.data[i].owner_id,
              phone_number: response.data[i].phone_number,
            });
          }
          obj.loading = false;
        });
    },
    acc(id) {
      let obj = this;
      let data = new FormData();
      data.append("owner_id", id);
      data.append("decision", 1);
      this.axios
      .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/admin/decideAboutOwner", data, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/admin/decideAboutOwner',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.getAllPendingOwners();
        });
    },
    del(id){
      let obj = this;
      let data = new FormData();
      data.append("owner_id", id);
      data.append("decision", -1);
      this.axios
      .post("http://127.0.0.1:8000/projekt-gospodarka-backend.herokuapp.com/admin/decideAboutOwner", data, {
        headers: {
          'X-Requested-With': 'http://projekt-gospodarka-backend.herokuapp.com/admin/decideAboutOwner',
          'Content-Type': 'multipart/form-data',
          'accept': 'application/json',
          'Authorization': "Bearer " + this.user.token
        }
      })
      .then(
        (response) => {
          obj.getAllPendingOwners();
        });
    }
  },
  created () {
    if(this.user.role == 'admin'){
      this.userForm.role = this.user.role;
      this.userForm.email = this.user.login;
      this.userForm.password = this.user.password;
      this.userForm.token = this.user.token;

      this.getAllPendingOwners();

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
  .btn_space {
    margin-right: 5px;
  }
</style>