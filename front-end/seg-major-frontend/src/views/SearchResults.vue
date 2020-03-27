<template>
  <div id="search-results">
    <section class="hero is-primary">
      <div class="hero-body">
        <div class="container" style="padding: 0 35px;">
          <h1 class="title">Search Results</h1>
          <h2 class="subtitle" v-if="pages && pages.length > 0">
            Searching for <b>{{ searchQuery }}</b> in {{ siteName }}
          </h2>
        </div>
      </div>
    </section>

    <div class="custom-content-container">
      <div class="search-results-list">
        <div class="card">
          <div class="card-content">
            <p class="page-title">Page Title</p>
          </div>
        </div>

        <div class="card">
          <div class="card-content">
            <p class="page-title">Page Title</p>
          </div>
        </div>

        <div class="card">
          <div class="card-content">
            <p class="page-title">Page Title</p>
          </div>
        </div>

      
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    pages: {
      type: Array
    }
  },
  data() {
    return {
      searchQuery: 'lorem ipsum'
    }
  },
  computed: {
    siteName() {
      return this.pages[0].site.name;
    }
  }
};
</script>

<style lang="scss" scoped>

.search-results-list {
  margin-top: 35px;

  .card {
    margin-bottom: 1rem;

    .page-title {
      font-size: 18px;
      font-weight: bold;
    }
    
  }
}
</style>