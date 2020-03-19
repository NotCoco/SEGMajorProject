<template>
  <div id="home">
    <section class="hero is-primary">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Paediatric Liver Service</h1>
          <h2 class="subtitle">King's College Hospital</h2>
        </div>
      </div>
    </section>

    <div class="section">
      <div class="container">
        <div class="columns">
          <div class="column">
            <p class="title is-3">Sites</p>
            <router-link
              v-for="site of sites"
              v-bind:key="site.primaryKey"
              class="is-block"
              style="margin-bottom: 20px"
              v-bind:to="site.name"
              append
            >
              <div class="card">
                <div class="card-content">
                  <h5 class="title is-5">{{site.name}}</h5>
                </div>
              </div>
            </router-link>
          </div>

          <div class="column is-narrow">
            <div class="card bulletin-card">
              <div class="section">
                <p class="title is-5">Bulletin Board</p>
                <p class="subtitle">Read the latest news!</p>
              </div>

              <router-link to>
                <div class="card bulletin-item">
                  <div class="card-content">
                    <h1 class="bulletin-title">Weekly Newsletter #5</h1>
                    <p
                      class="bulletin-content-preview"
                    >Lorem ipsum dolor sit amet consectetur adipisicing elit. Vel veritatis in debitis eos eum quis suscipit doloremque corporis eveniet animi maxime voluptatem veniam neque incidunt ducimus sed, aliquid quos ipsa.</p>
                  </div>
                </div>
              </router-link>

              <router-link to>
                <div class="card bulletin-item">
                  <div class="card-content">
                    <h1 class="bulletin-title">Updated Advice</h1>
                    <p
                      class="bulletin-content-preview"
                    >Lorem ipsum dolor sit amet consectetur adipisicing elit. Aliquid eos distinctio enim! Pariatur impedit atque eligendi, esse, dolorum numquam voluptates dolorem nostrum aliquam quaerat deleniti ipsam et incidunt quo nisi.</p>
                  </div>
                </div>
              </router-link>

              <router-link to>
                <div class="card bulletin-item">
                  <div class="card-content">
                    <h1 class="bulletin-title">Important News</h1>
                    <p
                      class="bulletin-content-preview"
                    >Lorem ipsum dolor sit amet consectetur adipisicing elit.</p>
                  </div>
                </div>
              </router-link>

              <button class="button is-fullwidth is-light is-medium">View all</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SitesService from "@/services/sites-service";

export default {
  name: "Home",
  data() {
    return {
      sites: []
    };
  },
  async mounted() {
    this.sites = await SitesService.getAllSites();
  }
};
</script>


<style lang="scss" scoped>
.bulletin-card {
  width: 500px;
  border-top: 5px solid #353535;
}

.bulletin-item {
  transition: background-color 0.2s;
  &:hover {
    background-color: #f8f8f8;
  }

  .bulletin-title {
    font-size: 18px;
    font-weight: bold;
  }

  .bulletin-content-preview {
    // Show ellipsis on text overflow
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.site-card {
  transition: background-color 0.2s;
  &:hover {
    background-color: #f8f8f8;
  }
}
</style>