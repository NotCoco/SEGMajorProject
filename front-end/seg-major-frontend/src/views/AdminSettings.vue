<template>
  <div id="admin-settings">
    <section class="section">
      <div class="custom-content-container">
        <h1 class="title">Settings</h1>

        <div class="card">
          <header class="card-header">
            <p class="card-header-title">General</p>
          </header>
          <div class="card-content">
            <div class="field">
              <label class="label">Hospital Name</label>
              <div class="control">
                <input class="input" type="text" placeholder="Enter hospital name..." />
              </div>
            </div>

            <div class="field">
              <label class="label">Department Name</label>
              <div class="control">
                <input class="input" type="text" placeholder="Enter department name..." />
              </div>
            </div>
            <div class="field is-grouped is-grouped-right">
              <p class="control">
                <button class="button is-success">Save</button>
              </p>
            </div>
          </div>
        </div>

        <div id="your-account" class="card">
          <header class="card-header">
            <p class="card-header-title">Your account</p>
          </header>
          <div class="card-content">
            <change-user-details v-on:nameChanged="$emit('nameChanged', $event)" style="margin-bottom: 1.5rem"></change-user-details>
            
            <article class="message is-danger">
              <div class="message-header">
                <p>Delete your account</p>
              </div>
              <div class="message-body has-background-light">
                <p>
                  <strong>Warning:</strong> This action cannot be undone.
                </p>
                <div class="field is-grouped is-grouped-right">
                  <p class="control">
                    <router-link
                      to="/admin/settings/delete-account"
                      class="button is-danger"
                    >Delete account</router-link>
                  </p>
                </div>
              </div>
            </article>
          </div>
        </div>

        <div class="card">
          <header class="card-header">
            <p class="card-header-title">Users</p>
          </header>
          <div class="card-content">
            <router-link
              to="/admin/settings/create-new-user"
              class="button is-primary is-medium"
            >Create New User</router-link>

            <hr />

            <article class="message">
              <div class="message-header">
                <p>View all users</p>
              </div>
              <div class="message-body">
                <users-table></users-table>
              </div>
            </article>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import UsersTable from "@/components/UsersTable";
import ChangeUserDetails from "@/components/ChangeUserDetails";

export default {
  components: {
    UsersTable,
    ChangeUserDetails
  },
  watch: {
    $route: {
      immediate: true,
      handler() {
        const anchorHash = this.$route.hash;
        if (anchorHash && anchorHash.length > 1) {
          setTimeout(() => {
            document
              .querySelector(anchorHash)
              .scrollIntoView({ behavior: "smooth" });
          });
          const routeWithoutHash = this.$route.fullPath.replace(anchorHash, "");
          this.$router.push(routeWithoutHash);
        }
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.card {
  margin-bottom: 50px;

  .card-header-title {
    font-size: 20px;
  }
}
</style>