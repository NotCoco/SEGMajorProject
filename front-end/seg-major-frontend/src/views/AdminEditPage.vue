<template>
  <div id="admin-edit-page">
    <section class="section">
      <div class="custom-content-container">
        <nav class="breadcrumb is-right" v-if="showBreadcrumbs" aria-label="breadcrumbs">
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
            <a class="button is-static">/biliary-atresia/</a>
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

        <div>
          <rich-text-editor ref="rte"></rich-text-editor>
        </div>

        <div class="buttons" style="justify-content: flex-end">
          <button class="button is-light">Cancel</button>
          <button class="button is-danger">Delete</button>
          <button class="button is-success" @click="save()">Save</button>
        </div>
      </div>
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

export default {
  components: {
    RichTextEditor
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
      showBreadcrumbs: false
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
      // Generate Page index
      const currentPages = await SitesService.getAllPages(this.page.site.slug);
      if (currentPages && currentPages.length > 0) {
        this.page.index = currentPages[currentPages.length - 1].index + 1;
      } else {
        this.page.index = 0;
      }

      this.page.content = JSON.stringify(this.$refs.rte.getJSON());

      const res = await SitesService.createPage(this.page);
      this.page = res;
      this.$router.push(
        `/admin/sites/${this.page.site.slug}/pages/${this.page.slug}`
      );
    },
    async updatePage() {
      this.page.content = JSON.stringify(this.$refs.rte.getJSON());

      await SitesService.updatePage(this.page);

      const currentSlug = this.$route.params.pageSlug;
      if (currentSlug != this.page.slug) {
        this.$router.push(
          `/admin/sites/${this.page.site.slug}/pages/${this.page.slug}`
        );
      }
    },
    async deletePage() {
      const res = await SitesService.deletePage(this.page);
      console.log(res);
      this.$router.push(`/admin/sites/${this.page.site.slug}/pages`);
    }
  },
  async mounted() {
    const siteSlug = this.$route.params.siteSlug;
    const pageSlug = this.$route.params.pageSlug;

    console.log(siteSlug)

    if (!this.newPage) {
      console.log("YOOOo")
      this.page = await SitesService.getPage(siteSlug, pageSlug);
      setTimeout(() => {
        this.$refs.rte.setContent(JSON.parse(this.page.content));
      });
    } else {
      this.page.site = { slug: siteSlug };
      console.log(this.page)
    }

    this.showBreadcrumbs = true;
  }
};
</script>