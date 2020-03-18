<template>
  <div id="admin-new-site">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">Create a new site</h1>

        <div class="field">
          <label class="label">Site Name</label>
          <div class="control">
            <input class="input" v-model="siteName" type="text" placeholder="Enter site name..." />
          </div>
        </div>

        <div class="level is-mobile">
          <div class="level-left">
            <router-link to="/admin/sites" class="button is-light">Cancel</router-link>
          </div>
          <div class="level-right">
            <button class="button is-success is-medium" @click="createSite()">Create</button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import SitesService from "@/services/sites-service";
export default {
  data() {
    return {
      siteName: ""
    };
  },
  methods: {
    async createSite() {
      const res = await SitesService.createSite(this.siteName);
      console.log(res);
    }
  }
};
</script>

<style>
</style>