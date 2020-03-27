<template>
  <div>
    <nav class="navbar" role="navigation" aria-label="main navigation">
      <div class="navbar-brand">
        <router-link to="/home" class="navbar-item">
          <svg
            height="35px"
            version="1.1"
            viewBox="0 0 370.61 150"
            xmlns="http://www.w3.org/2000/svg"
            xmlns:cc="http://creativecommons.org/ns#"
            xmlns:dc="http://purl.org/dc/elements/1.1/"
            xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
          >
            <rect width="370.61" height="150" fill="#005eb8" stroke-width="3.75" />
            <g transform="scale(3.75)" fill="#fff">
              <path d="m9.66 3.86h10.75l6.59 22.33h0.1l4.52-22.33h8.12l-6.83 32.2h-10.7l-6.74-22.28h-0.09l-4.47 22.28h-8.12z" />
              <path d="m42.91 3.86h8.63l-2.54 12.32h10.2l2.54-12.32h8.63l-6.69 32.2h-8.68l2.86-13.79h-10.15l-2.86 13.79h-8.62z" />
              <path d="m93.06 11.15a16.18 16.18 0 0 0-7.06-1.48c-3.41 0-6.18 0.51-6.18 3.09 0 4.57 12.5 2.86 12.5 12.64 0 8.9-8.26 11.21-15.73 11.21a36 36 0 0 1-10-1.66l2-6.55c1.71 1.11 5.12 1.85 7.93 1.85s6.87-0.51 6.87-3.83c0-5.17-12.5-3.23-12.5-12.32 0-8.3 7.29-10.8 14.35-10.8 4 0 7.7 0.42 9.87 1.43z" />
            </g>
          </svg>
        </router-link>
        <router-link to="/" class="navbar-item">
          <div class="brand-text">
            <h1 class="brand-top">King's College Hospital</h1>
            <h1 class="brand-bottom">Paediatric Liver Service</h1>
          </div>
        </router-link>
      </div>

      <div class="navbar-menu">
        <div class="navbar-start" style="margin-left: 35px">
          <router-link to="/" class="navbar-item">Home</router-link>
          <router-link to="/news" class="navbar-item">News</router-link>
        </div>

        <div class="navbar-end">
          <div class="searchbox-container" v-if="showSearchBar">
            <div class="control has-icons-left" style="height: 100%">
              <span class="icon is-small is-left" style="height: 100%;">
                <i class="search-icon material-icons">search</i>
              </span>
              <input class="searchbox input" type="text" v-model="searchQuery" placeholder="Search" />

              <transition name="fade" mode="out-in">
              <div v-if="searchQuery.length > 2" class="card search-suggestions">
                <div v-for="page in filtered" v-bind:key="page" class="card suggestion-item">
                  {{page}}
                </div>
                <div v-if="filtered.length == 0" class="card suggestion-item">
                <p><i>No search results found</i></p>
                </div>
              </div>
              </transition>
            </div>
          </div>
        </div>
      </div>
    </nav>
    <transition name="vertical-slide" v-if="displayUrgentNews" appear>
      <router-link class="link" :to="`/news/${urgentNews.slug}`">
        <div class="notification is-warning urgent-news">
          <button class="delete" @click.prevent="closeUrgentNews"></button>
          <strong>{{ urgentNews.title }}:</strong> {{ urgentNews.description }}
        </div>
      </router-link>
    </transition>
  </div>
</template>

<script>
import NewsService from '@/services/news-service';

export default {
  name: "Navbar",
  props: {
    showSearchBar: {
      type: Boolean,
      default: false
    },
    showUrgentNews: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    displayUrgentNews() { return this.showUrgentNews && this.urgentNews && !this.localHiddenState && !this.getHiddenState(); },
    filtered() {
      return this.pages.filter(p => p.includes(this.searchQuery))
    }
  },
  methods: {
    closeUrgentNews() {
      localStorage.setItem('hide-urgent-news', JSON.stringify(this.urgentNews.slug));
      this.localHiddenState = true;
    },
    getHiddenState() {
      const savedState = localStorage.getItem('hide-urgent-news');
      return savedState && JSON.parse(savedState) === this.urgentNews.slug;
    }
  },
  data () {
    return {
      urgentNews: undefined,
      localHiddenState: false,
      searchQuery: '',
      pages: ['Hey', 'test', 'lorem ipsum', 'ipsum', 'lorem', 'this is a test', 'testing']
    }
  },
  async created() {
    const items = await NewsService.getAllNews();
    if (items.length > 0) {
      const firstItem = items[0];
      if (firstItem.urgent) this.urgentNews = firstItem;
    }
  },
};
</script>

<style lang="scss" scoped>
.vertical-slide-leave, .vertical-slide-enter-to {
  top: 0;
}
.vertical-slide-enter-active, .vertical-slide-leave-active {
  position: relative;
  transition: top 0.5s;
}
.vertical-slide-enter, .vertical-slide-leave-to {
  top: -40px;
}

.notification.urgent-news {
  transition: background-color 0.2s;
  background-color: #ffe374;
  border-radius: 0;
  padding: 0.4rem 2.75rem 0.4rem 1.5rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  link {
    text-decoration: none;
  }

  .delete {
    transition: opacity 0.2s;
    opacity: 0.5;
    right: 0.75rem;
  }

  &:hover {
    background-color: #ffda47;

    .delete {
      opacity: 1;
    }
  }
}

nav.navbar {
  flex-shrink: 0;
  height: 68px;
  border-bottom: 3px solid #f5f5f8;

  .navbar-brand {
    padding-left: 14px;
  }

  .brand-text {
    .brand-top {
      font-size: 13.5px;
      opacity: 0.85;
      margin-bottom: -2px;
    }
    .brand-bottom {
      font-weight: bold;
      font-size: 16px;
      opacity: 0.92;
      margin-bottom: 0px;
    }
  }
}

.input.searchbox {
  height: 100%;
  width: 400px;
  border: 0;
  box-shadow: none;
  background-color: #fbfbfb;

  &:hover {
    background-color: #f9f9f9;
  }

  &:focus {
    background-color: #f5f5f7;
  }

  &::placeholder {
    opacity: 1;
    color: #888;
  }
}

.search-icon {
  color: #aaa;
}

.search-suggestions {
  background: white;

  .suggestion-item {
    padding: 15px 20px;
  }
}
</style>
