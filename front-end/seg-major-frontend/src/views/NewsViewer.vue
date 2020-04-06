<template>
  <div id="news-viewer">
    <Navbar :showUrgentNews="false"></Navbar>
    <section class="section">
      <div class="custom-content-container">
        <loading-spinner class="loading-spinner" v-if="loading"></loading-spinner>
        <http-status :httpStatusCode="404" v-else-if="news === null"></http-status>
        <article v-else>
          <h3 class="subtitle has-text-weight-bold is-uppercase" style="margin-bottom: 2rem;">News</h3>
          <h1 class="title">{{ news.title }}</h1>
          <h2 class="subtitle">{{ news.description }}</h2>
          <time class="subtitle" :datetime="news.date.toISOString().substring(0, 10)" pubdate>{{ news.date.toLocaleDateString("en-GB") }}</time>
          <rich-text-editor v-model="news.content" :editable="false" class="content"></rich-text-editor>
        </article>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
.content {
  margin-top: 2rem;
}
</style>

<script>
import Navbar from "@/components/Navbar";
import LoadingSpinner from "@/components/LoadingSpinner";
import HttpStatus from "@/components/HttpStatus";
import RichTextEditor from "@/components/RichTextEditor";
import NewsService from "@/services/news-service";

export default {
  components: {
    Navbar,
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
    const slug = this.$route.params.articleSlug;
    this.news = await NewsService.getArticle(slug);
    this.loading = false;
  }
};
</script>
