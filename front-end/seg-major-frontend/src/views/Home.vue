<template>
  <div id="home">
    <section class="hero is-primary">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Paediatric Liver Service</h1>
          <h2 class="subtitle">King's College Hospital</h2>
        </div>
      </div>
    </section>

    <div class="section">
      <div class="container">
        <div class="columns">
          <div class="column">
            <p class="title is-3">Sites</p>
            <router-link
              v-for="site of sites"
              v-bind:key="site.primaryKey"
              class="is-block"
              style="margin-bottom: 20px"
              v-bind:to="site.name"
              append
            >
              <div class="card">
                <div class="card-content">
                  <h5 class="title is-5">{{site.name}}</h5>
                </div>
              </div>
            </router-link>
          </div>

          <div class="column is-narrow">
            <bulletin-board></bulletin-board>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BulletinBoard from "@/components/BulletinBoard.vue";
import SitesService from "@/services/sites-service";

export default {
  name: "Home",
  components: {
    BulletinBoard: BulletinBoard
  },
  data() {
    return {
      sites: []
    };
  },
  async mounted() {
    this.sites = await SitesService.getAllSites();
  }
};
</script>


<style lang="scss" scoped>
.site-card {
  transition: background-color 0.2s;
  &:hover {
    background-color: #f8f8f8;
  }
}
</style>