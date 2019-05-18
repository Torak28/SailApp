<template>
  <div id="app">
    <b-container fluid class="cont">
      <b-navbar :sticky="true" type="light" class="nav-background">
        <b-navbar-brand href="#" class="mx-auto" >
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
      <cookie-law :buttonText="cookiebuttonText" theme="royal">
        <div slot="message">
          {{cookieText}} <b-link href="https://ezelechowska-psycholog.pl/PolitykaPrywatnosci.pdf">Polityki Prywatności</b-link>!
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
      cookiebuttonText: "Zgadzam się!",
      cookieText: "Ta strona korzysta z ciasteczek aby świadczyć usługi na najwyższym poziomie. Dalsze korzystanie ze strony oznacza, że zgadzasz się na ich użycie, po więcej informacji zapraszam do zapozaniania się z treścią",
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
    /*padding-left: 0 !important;
    padding-right: 0 !important;*/
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
