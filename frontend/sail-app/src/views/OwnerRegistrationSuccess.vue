<template>
  <b-container class="OwnerRegistrationSuccess">
    <h1 class='title'>Registration Success!</h1>
    <br>
    <br>
    <br>
    {{initMsg}}
    <br>
    <br>
    {{previewMsg}}
    <br>
    <br>
      <h1 class='title'>Preview</h1>
      <br>
      <br>
      <b-row>
        <b-col>
          <CompanyCardBasic :parentForm=user />
          <!-- Tutaj dodać jakieś propsy -->
        </b-col>
      </b-row>
      <br>
  </b-container>
</template>

<script>
import CompanyCardBasic from "@/components/CompanyCardBasic.vue";

export default {
  name: "OwnerRegistrationSuccess",
  props: ['user'],
  components: {
    CompanyCardBasic
  },
  data() {
    return {
      initMsg: 'You successfully register Your Owner Account! Now You have to wait a bit for our Admins to accept Your account. It will take up to 3 days.',
      previewMsg: 'Meanwhile You can see how You shop looks like in our system:',
      form: {

      }
    }
  }
};
</script>

<style scoped>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>

