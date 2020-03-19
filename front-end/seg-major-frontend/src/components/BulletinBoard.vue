<template>
  <div class="card bulletin-board">
    <div class="header">
      <p class="title is-4">Bulletin Board</p>
      <p class="subtitle">Read our latest news and guidance</p>
    </div>

    <loading-spinner class="loading-spinner" v-if="loading"></loading-spinner>
    <div class="no-news-message has-text-centered" v-if="!loading && items.length === 0">No news items</div>

    <router-link v-for="item in displayItems" :key="item.slug" :to="`/news/${item.slug}`">
      <news-card :newsItem="item"></news-card>
    </router-link>

    <router-link to="/news">
      <button class="button is-fullwidth is-light is-medium">View all</button>
    </router-link>
  </div>
</template>

<script>
import LoadingSpinner from '@/components/LoadingSpinner.vue';
import NewsCard from '@/components/NewsCard.vue';
import NewsService from '@/services/news-service';
import ArraySlice from '@/ArraySlice.js';

export default {
  name: "BulletinBoard",
  components: {
    LoadingSpinner,
    NewsCard
  },
  data () {
    return {
      loading: false,
      items: []
      // items: [
      //   { title: "Title1", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200310-lorem-ipsum", date: new Date("2020-03-10"), pinned: false, urgent: false },
      //   { title: "Title2", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200214-lorem-ipsum", date: new Date("2020-02-14"), pinned: true, urgent: false },
      //   { title: "Title3", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200311-lorem-ipsum", date: new Date("2020-03-11"), pinned: false, urgent: false },
      //   { title: "Title4", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200114-lorem-ipsum", date: new Date("2020-01-14"), pinned: true, urgent: true },
      //   { title: "Title5", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200313-lorem-ipsum", date: new Date("2020-03-13"), pinned: false, urgent: false },
      //   { title: "Title6", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200414-lorem-ipsum", date: new Date("2020-04-14"), pinned: false, urgent: false },
      //   { title: "Title7", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200312-lorem-ipsum", date: new Date("2020-03-12"), pinned: false, urgent: false },
      //   { title: "Title8", content: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum", date: new Date("2020-05-14"), pinned: false, urgent: false },
      // ]
    }
  },
  computed: {
    displayItems() {
      return new ArraySlice(this.items, 0, 5);
    }
  },
  async created() {
    this.loading = true;
    this.items = await NewsService.getAllNews();
    this.loading = false;
  }
};
</script>

<style lang="scss" scoped>
.bulletin-board {
  width: 500px;
  border-top: 5px solid #353535;

  .loading-spinner {
    min-height: 535px;
  }

  .header {
    padding: 2rem 1.5rem;
  }

  .no-news-message {
    height: 50px;
  }
}
</style>
