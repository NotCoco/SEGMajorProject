<template>
  <div id="site-content-viewer-layout">
    <Navbar v-bind:pages="pages" v-bind:showSearchBar="true"></Navbar>

    <div class="flex-wrapper">
      <div class="sidebar is-hidden-mobile">
        <div class="section">
          <div class="container">
            <p class="sidebar-label">{{ site.name }}</p>
            <nav class="sidebar-navigation">
              <transition name="fade" mode="out-in">
                <span v-if="loading">
                  <loading-spinner style="margin-top: 50px; opacity: 0.2"></loading-spinner>
                </span>
                <div class="navigation-items" v-else>
                  <router-link
                    v-bind:to="`/${page.site.slug}/${page.slug}`"
                    v-for="page of pages"
                    v-bind:key="page.primaryKey"
                    class="navigation-item is-unselectable"
                  >{{ page.title }}</router-link>
                </div>
              </transition>
            </nav>
          </div>
        </div>
      </div>

      <div class="page-content">
        <div class="searchBarContainer is-hidden-desktop">
          <search-bar :pages="pages" />
        </div>
        <transition name="fade" mode="out-in">
          <router-view v-bind:pages="pages" v-bind:key="$route.path"></router-view>
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

.searchBarContainer {
  height: 50px;
}
</style>

<script>
import SitesService from "@/services/sites-service";
import Navbar from "@/components/Navbar.vue";
import SearchBar from '@/components/SearchBar.vue';
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  components: {
    Navbar,
    SearchBar,
    LoadingSpinner
  },
  data() {
    return {
      site: { name: "... " },
      pages: [],
      loading: true
    };
  },
  async mounted() {
    const siteSlug = this.$route.params.siteSlug;
    this.site = await SitesService.getSite(siteSlug);
    this.pages = await SitesService.getAllPages(siteSlug);
    this.loading = false;
  }
};
</script>