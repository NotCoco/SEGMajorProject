<template>
  <div id="admin-all-sites">
    <div class="expanded-scrollable-area">
      <section class="section">
        <div class="custom-content-container">
          <h1 class="title">All Sites</h1>

          <div class="card" v-for="site of sites" v-bind:key="site.primaryKey" style="margin-bottom: 20px">
            <div class="card-content">
              <h5 class="title is-5">{{site.name}}</h5>
            </div>
          </div>
        </div>
      </section>
    </div>

    <router-link to="new" append class="button is-primary floating-add-button">
      <i class="material-icons" style="font-size: 48px;">add</i>
    </router-link>
  </div>
</template>

<script>
import SitesService from "@/services/sites-service";

export default {
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
#admin-all-sites {
  height: 100%;
}

.expanded-scrollable-area {
  overflow-y: scroll;
}
</style>