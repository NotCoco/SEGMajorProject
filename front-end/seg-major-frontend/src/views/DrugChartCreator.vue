<template>
  <div id="drug-chart-creator">
    <Navbar></Navbar>
    <section class="hero is-primary">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Drug Chart Creator</h1>
          <h2 class="subtitle">Create your own printable drug chart</h2>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="container">
        <transition name="fade-transform" mode="out-in">
          <DrugChartLayout></DrugChartLayout>
        </transition>
      </div>
    </section>
  </div>
</template>

<script>
import DrugChartLayout from "@/components/DrugChartLayout.vue";
import Navbar from "@/components/Navbar.vue";
export default{
  name: "DrugChart",
  components: {
    DrugChartLayout,
    Navbar
  }
}
</script>

<style lang="scss" scoped>
  html, body {
    height: 100%;
    overflow: unset;
  }

</style>
<style lang="scss" scoped>
 .table-layout{
    border-collapse:separate; 
    border-spacing:15px 10px;
    .subtable-layout{
      border-collapse:separate;
      border-spacing:15px 25px;
    }
 }
 .Drugchart-layout{
   border-collapse:separate; 
   border-spacing:30px 0px;
   .button.layout{
     width: 200px;
   }
 }
</style>
