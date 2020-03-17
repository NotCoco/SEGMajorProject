<template>
  <div id="admin-layout">
    <nav class="navbar is-dark" role="navigation" aria-label="main navigation">
      <div class="container">
        <div class="navbar-brand">
          <a class="navbar-item text-brand">Admin</a>

          <a class="navbar-item">Sites</a>

          <a class="navbar-item">News</a>

          <a class="navbar-item">Settings</a>
        </div>

        <div class="navbar-menu">
          <div class="navbar-end">
            <div class="navbar-item has-dropdown is-hoverable">
              <a class="navbar-link">
                <i class="material-icons" style="margin-right: 12px; opacity: 0.92;">person</i>
                Username
              </a>

              <div class="navbar-dropdown is-right">
                <a class="navbar-item">User Settings</a>
                <a class="navbar-item">Change Email</a>
                <a class="navbar-item">Change Password</a>
                <hr class="navbar-divider" />
                <a class="navbar-item log-out">Log out</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <router-view></router-view>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss">
.text-brand {
  font-size: 20px;
  font-weight: bold;
  margin-right: 20px;
}
</style>