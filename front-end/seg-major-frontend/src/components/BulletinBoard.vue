<template>
  <div class="card bulletin-board">
    <div class="header">
      <p class="title is-4">Bulletin Board</p>
      <p class="subtitle">Read our latest news and guidance</p>
    </div>

    <router-link v-for="item in displayItems" :key="item.slug" :to="`/news/${item.slug}`">
      <news-card :newsItem="item"></news-card>
    </router-link>

    <router-link to="/news">
      <button class="button is-fullwidth is-light is-medium">View all</button>
    </router-link>
  </div>
</template>

<script>
import NewsCard from '@/components/NewsCard.vue';
import NewsService from '@/services/news-service';
import ArraySlice from '@/ArraySlice.js';

export default {
  name: "BulletinBoard",
  components: {
    NewsCard
  },
  data () {
    return {
      items: []
    }
  },
  computed: {
    displayItems() {
      return new ArraySlice(this.items, 0, Math.min(5, this.items.length));
    }
  },
  async created() {
    this.items = await NewsService.getAllNews();
  }
};
</script>

<style lang="scss" scoped>
.bulletin-board {
  width: 500px;
  border-top: 5px solid #353535;

  .header {
    padding: 2rem 1.5rem;
  }

  .no-news-message {
    height: 50px;
  }
}
</style>
