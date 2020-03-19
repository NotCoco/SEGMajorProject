<template>
  <div id="news-root">
    <Navbar :showUrgentNews="false"></Navbar>
    <section class="hero is-primary">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">News</h1>
          <h2 class="subtitle">Get the latest news and advice</h2>
        </div>
      </div>
    </section>

    <div class="section">
      <div class="container">
        <div v-if="paginatedItems.length === 0" class="has-text-dark has-text-centered">There are no news items at this time.</div>
        <div v-else>
          <router-link v-for="item in paginatedItems" :key="item.slug" :to="item.slug" append>
            <news-card :newsItem="item" class="news-card"></news-card>
          </router-link>

          <nav class="pagination is-centered" role="navigation" aria-label="pagination">
            <a class="pagination-previous" @click="navigatePrevious" :disabled="!hasPreviousPage">Newer</a>
            <a class="pagination-next" @click="navigateNext" :disabled="!hasNextPage">Older</a>
            <ul class="pagination-list">
              <li>
                <router-link to="news"
                              class="pagination-link"
                              v-bind="currentPageNumber === 1 ? {'aria-label': 'Page 1', 'aria-current': 'page', 'class': 'is-current'} : {'aria-label': 'Goto page 1'}">1</router-link>
              </li>
              <li v-if="pageCount > 5 && currentPageNumber > 3"><span class="pagination-ellipsis">&hellip;</span></li>
              <li v-for="pageNumber in middlePageNumbers" :key="pageNumber">
                  <router-link :to="{ path: 'news', query: { page: pageNumber } }"
                                class="pagination-link"
                                v-bind="pageNumber === currentPageNumber ? {'aria-label': `Page ${pageNumber}`, 'aria-current': 'page', 'class': 'is-current'} : {'aria-label': `Goto page ${pageNumber}`}">{{ pageNumber }}</router-link>
              </li>
              <li v-if="pageCount > 5 && currentPageNumber < pageCount - 2"><span class="pagination-ellipsis">&hellip;</span></li>
              <li v-if="pageCount > 1">
                <router-link :to="{ path: 'news', query: { page: pageCount } }"
                              class="pagination-link"
                              v-bind="currentPageNumber === pageCount ? {'aria-label': `Page ${pageCount}`, 'aria-current': 'page', 'class': 'is-current'} : {'aria-label': `Goto page ${pageCount}`}">{{ pageCount }}</router-link>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import NewsCard from "@/components/NewsCard.vue";

export default {
  props: {
    currentPageNumber: {
      type: Number,
      default: 1
    }
  },
  components: {
    Navbar, NewsCard
  },
  methods: {
    navigatePrevious() {
      if (!this.hasPreviousPage) return;
      const newPageNumber = this.currentPageNumber - 1;
      if (newPageNumber === 1) this.$router.push('news');
      else this.$router.push({ path: 'news', query: { page: newPageNumber } });
    },
    navigateNext() {
      if (!this.hasNextPage) return;
      const newPageNumber = this.currentPageNumber + 1;
      this.$router.push({ path: 'news', query: { page: newPageNumber } });
    },
  },
  computed: {
    paginatedItems(){
      const start = (this.currentPageNumber - 1) * this.pageSize,
            end = start + this.pageSize;
      return this.items.slice(start, end);
    },
    pageCount() {
      return Math.max(Math.ceil(this.items.length / this.pageSize), 1);
    },
    hasPreviousPage() {
      return this.currentPageNumber > 1;
    },
    hasNextPage() {
      return this.currentPageNumber < this.pageCount;
    },
    middlePageNumbers() {
      if (this.pageCount < 3) return [];

      const middleNumbersCount = Math.min(this.pageCount - 2, 3);
      let ret = [];
      if (this.currentPageNumber <= 3) {
        for (let i = 0; i < middleNumbersCount; ++i) ret.push(i + 2);
      } else if (this.currentPageNumber >= this.pageCount - 2) {
        for (let i = 0; i < middleNumbersCount; ++i) ret.unshift(this.pageCount - 1 - i);
      } else {
        ret = [this.currentPageNumber - 1, this.currentPageNumber, this.currentPageNumber + 1];
      }
      return ret;
    },
  },
  data () {
    return {
      items: [
        { title: "Title1", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200310-lorem-ipsum", date: new Date("2020-03-10"), pinned: false, urgent: false },
        { title: "Title2", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200214-lorem-ipsum", date: new Date("2020-02-14"), pinned: true, urgent: false },
        { title: "Title3", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200311-lorem-ipsum", date: new Date("2020-03-11"), pinned: false, urgent: false },
        { title: "Title4", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200114-lorem-ipsum", date: new Date("2020-01-14"), pinned: true, urgent: true },
        { title: "Title5", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200313-lorem-ipsum", date: new Date("2020-03-13"), pinned: false, urgent: false },
        { title: "Title6", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200414-lorem-ipsum", date: new Date("2020-04-14"), pinned: false, urgent: false },
        { title: "Title7", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200312-lorem-ipsum", date: new Date("2020-03-12"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum1", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum2", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum3", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum4", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum5", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum6", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum7", date: new Date("2020-05-14"), pinned: false, urgent: false },
        { title: "Title8", description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", slug: "20200514-lorem-ipsum8", date: new Date("2020-05-14"), pinned: false, urgent: false },
      ],
      pageSize: 10
    }
  },
};
</script>

<style lang="scss" scoped>
  .news-card {
    margin-bottom: 1rem;
  }

  .pagination {
    margin-top: 3rem;
  }
</style>