<template>
  <div id="drug-chart-creator">
    <Navbar></Navbar>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
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
        <table class="Drugchart-layout">
          <tr>
            <td>
              <button class="layout button is-danger"  v-show="showfirst" @click="showFirst()">
                <i class="fa fa-minus-square">&nbsp;Hide FirstLayout</i>
              </button>
              <button class="layout button is-success" v-show="!showfirst" @click="showFirst()">
                <i class="fa fa-plus-square">&nbsp;Show FirstLayout</i>
              </button>
            </td>
            <td>
              <button class="layout button is-danger" v-show="showsecond" @click="showSecond()">
                <i class="fa fa-minus-square">&nbsp;Hide SecondLayout</i>
              </button>
              <button class="layout button is-success" v-show="!showsecond" @click="showSecond()">
                <i class="fa fa-plus-square">&nbsp;Show SecondLayout</i>
              </button>
            </td>
             
          </tr>
        </table>
            <transition name="fade-transform" mode="out-in">
              <FirstLayout v-show="showfirst"></FirstLayout>
            </transition>
             <SecondLayout v-show="showsecond"></SecondLayout>
      </div>
    </section>
  </div>
</template>
<script>
import FirstLayout from "@/views/FirstLayout.vue";
import SecondLayout from "@/views/SecondLayout.vue"
import Navbar from "@/components/Navbar.vue";
export default{
  name: "DrugChart",
  data(){
    return{
      showfirst: true,
      showsecond: false
    }
   
  },
  components: {
    FirstLayout,
    SecondLayout,
    Navbar
  },
  methods: {
    showFirst: function(){
        this.showfirst = !this.showfirst
        this.showsecond = !this.showsecond
    },
    showSecond: function(){
        this.showsecond = !this.showsecond
        this.showfirst = !this.showfirst
     
    }
  }
}
</script>
<style>
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
