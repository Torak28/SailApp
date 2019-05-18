
<template>
  <b-container id='Ads'>
    <br>
    <br>
    <h1 class='title'>{{title}}</h1>
    <p>{{text}}</p>
  </b-container>
</template>

<script>
export default {
  name: "Ads",
  data() {
    return {
      title: 'Sail with us!',
      text: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ultrices, purus vel vulputate vehicula, quam ligula ornare nunc, ac consequat ligula enim sit amet ex. Donec suscipit a magna sed porttitor. Sed vel ex consectetur, pellentesque dui a, sollicitudin ipsum. Praesent consectetur, nibh nec ornare vestibulum, urna elit molestie ex, vitae hendrerit dolor nisl quis libero. Aliquam tincidunt imperdiet congue. Aenean ut lectus ac metus euismod euismod in eget velit. Morbi suscipit finibus est ut maximus. Pellentesque sed tellus vitae sapien luctus sodales ornare ut enim. Cras imperdiet eros dui, et aliquet quam tincidunt ut. Sed arcu nunc, tincidunt eleifend orci ut, sagittis fringilla eros. Nullam sed elit imperdiet, suscipit erat sit amet, consequat ipsum.'
    }
  }
};
</script>

<style scope>
    .title {
            background: linear-gradient(180deg, rgba(255,255,255,0) 65%, #FFD0AE 65%);
            display: inline;
        }
</style>
