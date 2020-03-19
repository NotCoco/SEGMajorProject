<template>
  <div id="page-viewer">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">{{ page.title }}</h1>
        
        <rich-text-editor ref="rte" v-bind:editable="false"></rich-text-editor>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
</style>

<script>
import RichTextEditor from "@/components/RichTextEditor";
import SitesService from "@/services/sites-service";

export default {
  components: {
    RichTextEditor
  },
  data() {
    return {
      page: {}
    }
  },
  async mounted() {
    const siteName = this.$route.params.siteName;
    const pageSlug = this.$route.params.pageSlug;

    this.page = await SitesService.getPage(siteName, pageSlug);
    setTimeout(() => {
      this.$refs.rte.setContent(JSON.parse(this.page.content));
    });
  }
};
</script>
