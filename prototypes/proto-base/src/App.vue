<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style lang="scss">

@import 'bulma';
@import 'global-styles';


#app {
  // font-family: Avenir, Helvetica, Arial, sans-serif;
  font-family: 'Open Sans', Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;

  height: 100%;
}

html, body {
  height: 100%;
  overflow: auto;
}

</style>
