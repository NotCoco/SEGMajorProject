<template>
  <div id="admin-edit-page">
    <section class="section">
      <transition name="fade" mode="out-in">
        <loading-spinner v-if="loading" style="padding-top: 68px;"></loading-spinner>
        <div class="custom-content-container" v-if="!loading">
          <nav class="breadcrumb is-right" aria-label="breadcrumbs">
            <ul>
              <li>
                <router-link to="/admin">Admin</router-link>
              </li>
              <li>
                <router-link to="/admin/sites">Sites</router-link>
              </li>
              <li>
                <router-link v-bind:to="`/admin/sites/${page.site.slug}`">{{page.site.name || ''}}</router-link>
              </li>
              <li>
                <router-link v-bind:to="`/admin/sites/${page.site.slug}/pages`">Pages</router-link>
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
                v-model="page.title"
                placeholder="Enter page title here..."
              />
            </div>
          </div>

          <label class="label">URL Slug</label>
          <div class="field has-addons" style="margin-bottom: 35px;">
            <p class="control">
              <a class="button is-static" v-if="page.site">/{{page.site.slug}}/</a>
            </p>
            <p class="control is-expanded">
              <input
                class="input"
                type="text"
                v-model="page.slug"
                placeholder="Enter URL Slug here..."
              />
            </p>
          </div>

          <div style="flex-grow: 1;">
            <rich-text-editor v-model="page.content"></rich-text-editor>
          </div>

          <div class="buttons" style="justify-content: flex-end">
            <router-link to="../" class="button is-light">Cancel</router-link>
            <button class="button is-danger" @click="deletePage()">Delete</button>
            <button class="button is-success" @click="save()">Save</button>
          </div>
        </div>
      </transition>
    </section>
  </div>
</template>

<style lang="scss" scoped>
#admin-edit-page {
  height: 100%;
}

.section {
  height: 100%;
}

.custom-content-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}
</style>

<script>
import RichTextEditor from "@/components/RichTextEditor";

import SitesService from "@/services/sites-service";

import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  components: {
    RichTextEditor,
    LoadingSpinner
  },
  props: {
    newPage: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      page: {},
      loading: true
    };
  },
  methods: {
    save() {
      if (this.newPage === true) {
        this.createNewPage();
      } else {
        this.updatePage();
      }
    },
    async createNewPage() {
      this.loading = true;

      // Generate Page index
      const currentPages = await SitesService.getAllPages(this.page.site.slug);
      if (currentPages && currentPages.length > 0) {
        this.page.index = currentPages[currentPages.length - 1].index + 1;
      } else {
        this.page.index = 0;
      }

      const res = await SitesService.createPage(this.page);
      this.page = res;
      this.$router.push(
        `/admin/sites/${this.page.site.slug}/pages/${this.page.slug}`
      );

      this.loading = false;
    },
    async updatePage() {
      this.loading = true;

      await SitesService.updatePage(this.page);

      const currentSlug = this.$route.params.pageSlug;
      if (currentSlug != this.page.slug) {
        this.$router.push(
          `/admin/sites/${this.page.site.slug}/pages/${this.page.slug}`
        );
      }

      this.loading = false;
    },
    async deletePage() {
      this.loading = true;
      await SitesService.deletePage(this.page);
      this.$router.push(`/admin/sites/${this.page.site.slug}/pages`);
    }
  },
  async mounted() {
    const siteSlug = this.$route.params.siteSlug;
    const pageSlug = this.$route.params.pageSlug;

    if (!this.newPage) {
      this.page = await SitesService.getPage(siteSlug, pageSlug);
    } else {
      this.page.site = { slug: siteSlug };
      this.page.site = await SitesService.getSite(siteSlug);
    }

    this.loading = false;
  }
};
</script>