import Vue from "vue";
import Router from "vue-router";
import Home from "./views/Home.vue";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "home",
      component: Home
    },
    {
      path: "/userPanel",
      name: "UserPanel",
      component: () =>
        import("./views/UserPanel.vue")
    },
    {
      path: "/register",
      name: "Register",
      component: () =>
        import("./views/Register.vue")
    }
  ]
});
