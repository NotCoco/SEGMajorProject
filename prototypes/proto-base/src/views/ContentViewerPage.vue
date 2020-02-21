<template>
  <div class="root">
    <div class="scrollable-area">
      <section class="section">
        <router-view></router-view>
      </section>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.scrollable-area {
  overflow-y: scroll;
  height: 100%;
}
</style>

<script>
export default {
  components: {}
};
</script>
