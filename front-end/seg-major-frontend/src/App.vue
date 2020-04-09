<template>
  <transition name="fade" mode="out-in">
    <router-view />
  </transition>
</template>

<script>
export default {
  metaInfo: {
    titleTemplate: '%s | KCH Paediatric Liver Service'
  }
}
</script>

<style lang="scss">
@import "styles";
</style>