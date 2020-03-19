<template>
  <div id="admin-all-pages">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">All Pages</h1>

        <div class="pages-list">
          <router-link
            v-bind:to="page.slug"
            append
            v-for="page of pages"
            v-bind:key="page.primaryKey"
          >
            <div class="card">
              <div class="card-content">
                <p class="page-name">{{ page.title }}</p>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </section>

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
      pages: []
    };
  },
  async mounted() {
    const siteName = this.$route.params.siteName;
    this.pages = await SitesService.getAllPages(siteName);
  }
};
</script>

<style lang="scss" scoped>
#admin-all-pages {
  height: 100%;
}

.pages-list {
  margin-top: 50px;
  .card {
    margin-bottom: 20px;

    .page-name {
      font-size: 18px;
      font-weight: bold;
    }
  }
}
</style>