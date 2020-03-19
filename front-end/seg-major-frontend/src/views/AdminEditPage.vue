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
              <router-link v-bind:to="`/admin/sites/${page.site.name}`">{{page.site.name || ''}}</router-link>
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

        <rich-text-editor ref="rte"></rich-text-editor>

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
      const currentPages = await SitesService.getAllPages(this.page.site.name);
      if (currentPages && currentPages.length > 0) {
        this.page.index = currentPages[currentPages.length - 1].index + 1;
      } else {
        this.page.index = 0;
      }

      this.page.content = JSON.stringify(this.$refs.rte.getJSON());

      const res = await SitesService.createPage(this.page);
      this.page = res;
      this.$router.push(
        `/admin/sites/${this.page.site.name}/pages/${this.page.slug}`
      );
    },
    async updatePage() {
      this.page.content = JSON.stringify(this.$refs.rte.getJSON());

      await SitesService.updatePage(this.page);
      
      const currentSlug = this.$route.params.pageSlug;
      if (currentSlug != this.page.slug) {
        this.$router.push(
          `/admin/sites/${this.page.site.name}/pages/${this.page.slug}`
        );
      }
    },
    async deletePage() {
      const res = await SitesService.deletePage(this.page);
      console.log(res);
      this.$router.push(`/admin/sites/${this.page.site.name}/pages`);
    }
  },
  async mounted() {
    const siteName = this.$route.params.siteName;
    const pageSlug = this.$route.params.pageSlug;

    if (!this.newPage) {
      this.page = await SitesService.getPage(siteName, pageSlug);
      setTimeout(() => {
        this.$refs.rte.setContent(JSON.parse(this.page.content));
      });
    } else {
      this.page.site = { name: siteName };
    }

    this.showBreadcrumbs = true;
  }
};
</script>