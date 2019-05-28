
<template>
  <b-container id='Ads'>
    <br>
    <br>
    <h1 class='title'>{{title}}</h1>
    <br>
    <br>
    <img src="../assets/promo.png" class="align-centre" alt="Logo">
    <br>
    <br>
    <p>{{text}}</p>
  </b-container>
</template>

<script>
export default {
  name: "Ads",
  data() {
    return {
      title: 'Sail with us!',
      text: 'Want to renta a boat or a kayak? We got You covered! Use our app to search for a perfect match between You and your dream sailboat! There are no such thing as a failure with us. Above one hundret places to choose from, above five hundrets boats to rent and prices from twenty PLN for hour up! Want to spend time on a lake, but don\'t now how and where? Don\'t worry, We got You covered. Sail app for the rescue!'
    }
  }
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
  img {
        max-width: 100%;
        height: auto;
  }
</style>