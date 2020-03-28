<template>
  <div id="admin-site-settings">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">Site Settings</h1>

        <transition name="fade" mode="out-in">
          <loading-spinner v-if="loading" style="margin-top: 50px;"></loading-spinner>
          <div v-if="!loading">
            <div class="field">
              <label class="label">Site Name</label>
              <div class="control">
                <input
                  class="input"
                  v-model="site.name"
                  type="text"
                  placeholder="Enter site name..."
                />
              </div>
            </div>

            <label class="label">URL Slug</label>
            <div class="field">
              <p class="control is-expanded">
                <input
                  class="input"
                  type="text"
                  v-model="site.slug"
                  placeholder="Enter URL Slug here..."
                />
              </p>
            </div>

            <div class="level is-mobile">
              <div class="level-left">
                <div class="level-item">
                  <router-link
                    v-bind:to="`/admin/sites/${site.slug}`"
                    class="button is-light"
                  >Cancel</router-link>
                </div>
                <div class="level-item">
                  <button class="button is-danger" @click="deleteSite()">Delete</button>
                </div>
              </div>
              <div class="level-right">
                <button class="button is-success is-medium" @click="save()">Save</button>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </section>
  </div>
</template>

<script>
import SitesService from "@/services/sites-service";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  components: {
    LoadingSpinner
  },
  data() {
    return {
      site: {},
      loading: true
    };
  },
  methods: {
    async save() {
      this.loading = true;

      await SitesService.updateSite(this.site);

      const currentSlug = this.$route.params.siteSlug;
      if (currentSlug != this.site.slug) {
        this.$router.push(`/admin/sites/${this.site.slug}/settings`);
      }

      this.$emit("siteUpdate", this.site);

      this.loading = false;
    },
    async deleteSite() {
      this.loading = true;
      await SitesService.deleteSite(this.site);
      this.$router.push("/admin/sites");
    }
  },
  async mounted() {
    const siteSlug = this.$route.params.siteSlug;
    this.site = await SitesService.getSite(siteSlug);
    this.loading = false;
  }
};
</script>

<style lang="scss" scoped>
</style>