<template>
  <div id="admin-layout">
    <nav class="navbar is-dark" role="navigation" aria-label="main navigation">
      <div class="container">
        <div class="navbar-brand">
          <router-link to="/admin" class="navbar-item text-brand">Admin</router-link>

          <div class="navbar-burger is-hidden-desktop" @click="mobileNavActive=!mobileNavActive">
            <font-awesome-icon icon="bars" size="2x" />
          </div>
        </div>

        <transition name="fade" mode="out-in">
          <div
            class="navbar-menu"
            :class="{ 'is-active': mobileNavActive }"
            v-bind:key="mobileNavActive"
          >
            <div class="navbar-start">
              <router-link to="/admin/sites" class="navbar-item" active-class="is-active">Sites</router-link>
              <router-link
                to="/admin/drug-chart"
                class="navbar-item"
                active-class="is-active"
              >Drug Chart</router-link>

              <router-link to="/admin/news" class="navbar-item" active-class="is-active">News</router-link>

              <router-link
                to="/admin/settings"
                class="navbar-item"
                active-class="is-active"
              >Settings</router-link>
            </div>

            <div class="navbar-end">
              <div class="navbar-item has-dropdown is-hoverable is-hidden-touch">
                <a class="navbar-link">
                  <i class="material-icons" style="margin-right: 12px; opacity: 0.92;">person</i>
                  {{username}}
                </a>

                <div class="navbar-dropdown is-right">
                  <router-link to="/admin/settings#your-account" class="navbar-item">Your account</router-link>
                  <router-link to="/admin/settings#change-email" class="navbar-item">Change Email</router-link>
                  <router-link
                    to="/admin/settings#change-password"
                    class="navbar-item"
                  >Change Password</router-link>
                  <hr class="navbar-divider" />
                  <router-link
                    to="/admin/settings/create-new-user"
                    class="navbar-item"
                  >Create new user</router-link>
                  <hr class="navbar-divider" />
                  <a class="navbar-item log-out" @click="logout()">Log out</a>
                </div>
              </div>
            </div>
          </div>
        </transition>
      </div>
    </nav>

    <div class="expanded-scrollable-area">
      <transition name="fade" mode="out-in" v-bind:duration="{ leave: 50 }">
        <router-view v-bind:username="username" v-on:nameChanged="onNameChanged($event)" />
      </transition>
    </div>
  </div>
</template>

<script>
import UserService from "@/services/user-service";

export default {
  data() {
    return {
      username: "...",
      mobileNavActive: false
    };
  },
  methods: {
    async logout() {
      await UserService.logout();
      this.$router.push("/");
    },
    onNameChanged(newName) {
      this.username = newName;
    }
  },
  async mounted() {
    this.username = await UserService.getUserName();
  }
};
</script>

<style lang="scss">
@import "@/variables";

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

.navbar {
  height: 55px;

  .navbar-burger {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>