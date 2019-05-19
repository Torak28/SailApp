<template>
  <b-container class="UserPanel">
    <h1 class='title'>User Panel page!</h1>
    {{user}}
  </b-container>
</template>

<script>
export default {
  name: "UserPanel",
  props: ['user']
};
</script>

<style scope>
  .title {
    background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
    display: inline;
  }
</style>