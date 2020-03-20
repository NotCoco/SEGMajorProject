<template>
  <div id="news-viewer">
    <section class="section">
      <div class="custom-content-container">
        <loading-spinner class="loading-spinner" v-if="loading"></loading-spinner>
        <http-status :httpStatusCode="404" v-else-if="news === null"></http-status>
        <div v-else>
          <h1 class="title">{{ news.title }}</h1>
          <rich-text-editor ref="rte" :editable="false"></rich-text-editor>
        </div>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
</style>

<script>
import LoadingSpinner from "@/components/LoadingSpinner";
import HttpStatus from "@/components/HttpStatus";
import RichTextEditor from "@/components/RichTextEditor";
import NewsService from "@/services/news-service";

export default {
  components: {
    LoadingSpinner,
    HttpStatus,
    RichTextEditor
  },
  data() {
    return {
      loading: false,
      news: {}
    }
  },
  async created() {
    this.loading = true;
    const slug = this.$route.params.newsSlug;
    this.news = await NewsService.getNews(slug);
    this.loading = false;
  }
};
</script>
