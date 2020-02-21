<template>
  <div class="root">
    <div class="flex-wrapper">
      <div class="sidebar is-hidden-mobile">
        <div class="section">
          <div class="container">
            <p class="sidebar-label">Contents</p>
            <nav class="sidebar-navigation">
              <div class="navigation-items">
                <router-link to="/" class="navigation-item is-unselectable">Introduction</router-link>

                <router-link to="Nutrition" class="navigation-item is-unselectable">Nutrition</router-link>

                <router-link to="Medications" class="navigation-item is-unselectable">Medications</router-link>
                <div class="navigation-item is-unselectable">Immunisation and Infection</div>
                <div class="navigation-item is-unselectable">Cholangitis</div>
                <div class="navigation-item is-unselectable">Ascites</div>
                <div class="navigation-item is-unselectable">Portal Hypertension</div>
                <div class="navigation-item is-unselectable">Progression</div>
                <div class="navigation-item is-unselectable">Contacts</div>
              </div>
            </nav>
          </div>
        </div>
      </div>

      <div class="page-content">
        <transition name="fade" mode="out-in">
          <router-view v-bind:key="$route.path"></router-view>
        </transition>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s ease-out, transform 0.2s ease-out;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}

.fade-enter {
  transform: translateY(-12px);
}

.root {
  height: 100%;
}

.flex-wrapper {
  height: 100%;
  display: flex;
}

.page-content {
  flex-grow: 1;
}

.sidebar {
  min-height: 100%;
  width: 285px;
  flex-shrink: 0;
  border-right: 2px solid #eee;

  overflow-y: auto;

  .sidebar-label {
    font-family: "Signika", serif;
    font-size: 28px;
    color: rgb(153, 153, 170);
  }
}

.navigation-items {
  margin-top: 20px;

  .navigation-item {
    display: block;
    color: rgb(35, 68, 72);
    padding: 7px 12px;
    margin-bottom: 15px;
    font-size: 18px;
    transition: background-color 0.2s, color 0.2s;
    border-radius: 4px;
    cursor: pointer;

    &:hover {
      background: rgba(180, 180, 192, 0.08);
    }

    &.router-link-exact-active {
      background: rgba(185, 180, 192, 0.12);
      color: #282235;
    }
  }
}
</style>

<script>
export default {};
</script>