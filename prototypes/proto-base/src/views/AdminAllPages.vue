<template>
  <div class="root">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">All Pages</h1>

        <div class="pages-list">
          <div class="card">
            <div class="card-content">
              <p class="page-name">Introduction</p>
            </div>
          </div>

          <div class="card">
            <div class="card-content">
              <p class="page-name">Nutrition</p>
            </div>
          </div>

          <div class="card">
            <div class="card-content">
              <p class="page-name">Medications</p>
            </div>
          </div>

          <div class="card">
            <div class="card-content">
              <p class="page-name">Immunisation and Infection</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style lang="scss" scoped>
.pages-list {
  margin-top: 50px;
}

.card {
  margin-bottom: 20px;

  .page-name {
    font-size: 18px;
    font-weight: bold;
  }
}
</style>

<script>
export default {
  components: {}
};
</script>
