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
      path: "/adminPanel",
      name: "AdminPanel",
      props: true,
      component: () =>
        import("./views/AdminPanel.vue")
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
      props: true,
      component: () =>
        import("./views/OwnerRegistration.vue")
    },
    {
      path: "/gearRegistration",
      name: "GearRegistration",
      props: true,
      component: () =>
        import("./views/GearRegistration.vue")
    },
    {
      path: "/ownerRegistrationSuccess",
      name: "OwnerRegistrationSuccess",
      props: true,
      component: () =>
        import("./views/OwnerRegistrationSuccess.vue")
    }
  ]
});
