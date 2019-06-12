<template>
  <div id="app">
    <b-container fluid class="cont">
      <b-navbar :sticky="true" type="light" class="nav-background">
        <b-navbar-brand href="/" class="mx-auto" >
          <img src="./assets/logoNavbar.png" class="align-centre" alt="Logo">
        </b-navbar-brand>
      </b-navbar>
      <b-row class="text">
        <b-col sm="2" />
        <b-col sm="8">
          <router-view />
        </b-col>  
        <b-col sm="2" />
      </b-row>
    </b-container>
    <footer>
      <cookie-law theme="royal">
        <div slot="message">
          {{cookieText}} <b-link href='https://ezelechowska-psycholog.pl/PolitykaPrywatnosci.pdf'>terms and use policy</b-link>!
        </div>
      </cookie-law>
    </footer>
  </div>
</template>

<script>
import CookieLaw from 'vue-cookie-law'


export default {
  name: 'app',
  components: {
    CookieLaw
  },
  data() {
    return {
      cookieText: "This website uses cookies to ensure you get the best experience on our website,",
    }
  }
}
</script>

<style>
  #app {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif !important;
    background: #fafafa !important; 
    color: #222;
  }
  .cont {
    background: 0;
  }
  .text {
    color: #222;
    padding: 2rem;
    text-align: justify;
    text-justify: inter-word;
    min-height: 90vh;
  }
  img {
    max-width: 100%;
    height: auto;
  }
  .nav-background {
    background: #ece4b7;
    height: auto;
  }
</style>
