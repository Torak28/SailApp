<template>
  <div id="app">
    <b-container fluid>
      <b-navbar :sticky="true" type="light" class="nav-background">
          <b-navbar-brand href="#" class="mx-auto">
            <img src="./assets/logoNavbar.png" class="align-centre" alt="Logo">
            Sail
          </b-navbar-brand>
        </b-navbar>
      <div id="text">
        <router-link to="/">Home</router-link> |
        <router-link to="/about">About</router-link>
        <router-view />
      </div>
    </b-container>
  </div>
</template>

<style>
  #app {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif !important;
    background: #fafafa !important; 
    color: #222;
    margin-left: -15px;
    margin-right: -15px;
  }
  #text {
    color: #222;
    padding: 2rem;
    margin: 8px;
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
