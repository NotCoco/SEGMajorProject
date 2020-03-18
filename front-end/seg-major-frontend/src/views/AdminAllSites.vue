<template>
  <div id="admin-all-sites">
    <div class="expanded-scrollable-area">
      <section class="section">
        <div class="container">
          <h1 class="title">All Sites</h1>

          <div class="card">
            <div class="card-content">
              <h5 class="title is-5">Biliary Atresia</h5>
            </div>
          </div>
        </div>
      </section>
    </div>

    <button class="button is-primary floating-add-button">
      <i class="material-icons" style="font-size: 48px;">add</i>
    </button>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
#admin-all-sites {
  height: 100%;
}

.expanded-scrollable-area {
  overflow-y: scroll;
}

.floating-add-button {
  position: absolute;
  bottom: 47px;
  right: 47px;
  height: 74px;
  width: 74px;
}
</style>