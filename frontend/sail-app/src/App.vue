<template>
  <div id="app">
    <b-container fluid>
      <b-navbar :sticky="true" type="light" class="nav-background">
        <b-navbar-brand href="#" class="mx-auto">
          <img src="./assets/logoNavbar2.png" class="align-centre" alt="Logo">
        </b-navbar-brand>
      </b-navbar>
      <b-row class="text" style="min-height: 80vh;">
        <b-col sm="2" />
        <b-col sm="8">
          <router-link to="/">Home</router-link> |
          <router-link to="/about">About</router-link>
          <router-view />
        </b-col>  
        <b-col sm="2" />
      </b-row>
      <b-navbar type="light" class="nav-background">
        <b-navbar-brand href="#" class="mx-auto">
          <small class="text-muted">© 2019, Sail App team</small>
        </b-navbar-brand>
      </b-navbar>
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
      cookieText: "Ta strona korzysta z ciasteczek aby świadczyć usługi na najwyższym poziomie. Dalsze korzystanie ze strony oznacza, że zgadzasz się na ich użycie, po więcej informacji zapraszam do zapozaniania się z treścią"
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
  .text {
    color: #222;
    padding: 2rem;
    text-align: justify;
    text-justify: inter-word;
  }
  img {
    max-width: 100%;
    height: auto;
  }
  .nav-background {
    background: #ece4b7;
    min-height: 50px;
  }
</style>
