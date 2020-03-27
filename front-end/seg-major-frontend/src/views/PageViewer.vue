<template>
  <div id="page-viewer">
    <section class="section">
      <div class="custom-content-container" v-if="page">
        <h1 class="title">{{ page.title }}</h1>

        <rich-text-editor v-bind:editable="false" v-model="page.content"></rich-text-editor>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
@import "@/styles.scss";
.title {
  color: $primary-dark;
  font-size: 35px;
}
</style>

<script>
import RichTextEditor from "@/components/RichTextEditor";
// import SitesService from "@/services/sites-service";

export default {
  components: {
    RichTextEditor
  },
  props: {
    pages: {
      type: Array
    }
  },
  async mounted() {
    // const siteSlug = this.$route.params.siteSlug;
    // const pageSlug = this.$route.params.pageSlug;

    // this.page = await SitesService.getPage(siteSlug, pageSlug);
  },
  computed: {
    page() {
      const pageSlug = this.$route.params.pageSlug;
      return this.pages.find(p => p.slug === pageSlug)
    }
  }
};
</script>
