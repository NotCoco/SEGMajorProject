<template>
  <div id="site-content-viewer-layout">
    <Navbar :pages="pages" :showSearchBar="true" />

    <div class="flex-wrapper">
      <div class="sidebar is-hidden-mobile">
        <div class="section">
          <div class="container">
            <p class="sidebar-label">{{ site.name }}</p>
            <nav class="sidebar-navigation">
              <transition name="fade" mode="out-in">
                <span v-if="loading">
                  <loading-spinner style="margin-top: 50px; opacity: 0.4" />
                </span>
                <div class="navigation-items" v-else>
                  <router-link
                    :to="`/${page.site.slug}/${page.slug}`"
                    v-for="page of pages"
                    :key="page.primaryKey"
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
          <router-view :pages="pages" :key="$route.path" />
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
import AppInfoService from "@/services/app-info-service";
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
      pages: null,
      loading: true,
      appInfo: {
        departmentName: ''
      },
    };
  },
  metaInfo() {
    return {
      titleTemplate: titleChunk => {
        return titleChunk
          ? `${titleChunk} - ${this.site.name} | ${this.appInfo.departmentName}`
          : `${this.site.name} | ${this.appInfo.departmentName}`;
      }
    }
  },
  async created() {
    const siteSlug = this.$route.params.siteSlug;
    await Promise.all([
      AppInfoService.getAppInfo().then(value => this.appInfo = value),
      SitesService.getSite(siteSlug).then(value => this.site = value),
      SitesService.getAllPages(siteSlug).then(value => this.pages = value),
    ]);
    this.loading = false;
  }
};
</script>