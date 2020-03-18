<template>
  <div id="admin-edit-page">
    <section class="section">
      <div class="custom-content-container">
        <nav class="breadcrumb is-right" aria-label="breadcrumbs">
          <ul>
            <li>
              <router-link to="/admin">Admin</router-link>
            </li>
            <li>
              <router-link to="/admin/sites">Sites</router-link>
            </li>
            <li>
              <router-link v-bind:to="`/admin/sites/${page.site.name}`">{{page.site.name}}</router-link>
            </li>
            <li>
              <router-link v-bind:to="`/admin/sites/${page.site.name}/pages`">Pages</router-link>
            </li>
            <li class="is-active">
              <a href="#" aria-current="page">Page Editor</a>
            </li>
          </ul>
        </nav>

        <div class="field">
          <div class="control">
            <input
              type="text"
              class="input title"
              v-bind:value="page.title"
              placeholder="Enter page title here..."
            />
          </div>
        </div>

        <label class="label">URL Slug</label>
        <div class="field has-addons" style="margin-bottom: 35px;">
          <p class="control">
            <a class="button is-static">/biliary-atresia/</a>
          </p>
          <p class="control is-expanded">
            <input class="input" type="text" v-bind:value="page.slug" placeholder="Enter URL Slug here..." />
          </p>
        </div>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
#admin-edit-page {
  height: 100%;
}
</style>

<script>
import SitesService from "@/services/sites-service";

export default {
  data() {
    return {
      page: {},
      newPage: Boolean
    };
  },
  async mounted() {
    const siteName = this.$route.params.siteName;
    const pageSlug = this.$route.params.pageSlug;
    this.page = await SitesService.getPage(siteName, pageSlug);
  }
};
</script>