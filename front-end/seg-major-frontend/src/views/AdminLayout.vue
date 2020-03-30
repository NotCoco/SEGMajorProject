<template>
  <div id="admin-layout">
    <nav class="navbar is-dark" role="navigation" aria-label="main navigation">
      <div class="container">
        <div class="navbar-brand">
          <router-link to="/admin" class="navbar-item text-brand">Admin</router-link>

          <router-link to="/admin/sites" class="navbar-item">Sites</router-link>
          <router-link to="/admin/drug-chart" class="navbar-item">Drug Chart</router-link>

          <a class="navbar-item">News</a>

          <a class="navbar-item">Settings</a>
        </div>

        <div class="navbar-menu">
          <div class="navbar-end">
            <div class="navbar-item has-dropdown is-hoverable">
              <a class="navbar-link">
                <i class="material-icons" style="margin-right: 12px; opacity: 0.92;">person</i>
                {{username}}
              </a>

              <div class="navbar-dropdown is-right">
                <a class="navbar-item">User Settings</a>
                <a class="navbar-item">Change Email</a>
                <router-link to="/admin/password-reset" class="navbar-item">Change Password</router-link>
                <hr class="navbar-divider" />
                <a class="navbar-item log-out">Log out</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <div class="expanded-scrollable-area">
      <transition name="fade" mode="out-in" v-bind:duration="{ leave: 50 }">
        <router-view v-bind:username="username"></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
import UserService from "@/services/user-service";

export default {
  data() {
    return {
      username: "..."
    };
  },
  async mounted() {
    this.username = await UserService.getUserName();
  }
};
</script>

<style lang="scss">
.text-brand {
  font-size: 20px;
  font-weight: bold;
  margin-right: 20px;
}

#admin-layout {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>