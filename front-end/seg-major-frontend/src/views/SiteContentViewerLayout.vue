<template>
  <div id="site-content-viewer-layout">
    <Navbar v-bind:showSearchBar="true"></Navbar>

    <div class="flex-wrapper">
      <div class="sidebar is-hidden-mobile">
        <div class="section">
          <div class="container">
            <p class="sidebar-label">Contents</p>
            <nav class="sidebar-navigation">
              <div class="navigation-items">
                <router-link
                  v-bind:to="`/${page.site.slug}/${page.slug}`"
                  v-for="page of pages"
                  v-bind:key="page.primaryKey"
                  class="navigation-item is-unselectable"
                >{{ page.title }}</router-link>
              </div>
            </nav>
          </div>
        </div>
      </div>

      <div class="page-content">
        <transition name="fade" mode="out-in">
          <router-view v-bind:key="$route.path"></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
#site-content-viewer-layout {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.flex-wrapper {
  height: 100%;
  display: flex;
  overflow: hidden;
}

.page-content {
  flex-grow: 1;
  overflow-y: scroll;
}

.sidebar {
  background: #fdfdfd;
  border: none;
}
</style>

<script>
import SitesService from "@/services/sites-service";

import Navbar from "@/components/Navbar.vue";

export default {
  components: {
    Navbar
  },
  data() {
    return {
      pages: []
    };
  },
  async mounted() {
    const siteSlug = this.$route.params.siteSlug;
    this.pages = await SitesService.getAllPages(siteSlug);
  }
};
</script>