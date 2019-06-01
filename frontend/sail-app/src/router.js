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
      props: true,
      component: () =>
        import("./views/UserPanel.vue")
    },
    {
      path: "/ownerPanel",
      name: "OwnerPanel",
      props: true,
      component: () =>
        import("./views/OwnerPanel.vue")
    },
    {
      path: "/registration",
      name: "Registration",
      component: () =>
        import("./views/Registration.vue")
    },
    {
      path: "/ownerRegistration",
      name: "OwnerRegistration",
      component: () =>
        import("./views/OwnerRegistration.vue")
    }
  ]
});
