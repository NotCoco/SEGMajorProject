<template>

  <div id="admin-all-pages">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">Drug Chart Configuration</h1>
    <DrugList></DrugList>
      </div>
    </section>
  </div>
</template>

<script  type="text/javascript">
import DrugList from "@/components/DrugList.vue"
export default{
  components: {
    DrugList,
  }
}
</script>

<style lang="scss" scoped>

</style>
