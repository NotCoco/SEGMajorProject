<template>
  <div id="login">
    <Navbar></Navbar>

    <div class="custom-content-container">
      <div class="card login-box">
        <div class="notification" v-if="sessionExpired"><strong>Session has expired.</strong> Please re-login.</div>
        <h1 class="title">Login</h1>

        <div class="field">
          <label class="label">Email</label>
          <div class="control">
            <input class="input" v-model="username" type="text" placeholder="Enter email here..." />
          </div>
        </div>

        <div class="field">
          <label class="label">Password</label>
          <div class="control">
            <input class="input" v-model="password" type="password" placeholder="Enter password here..." />
          </div>
        </div>

        <button class="button is-primary is-medium" style="margin-top: 10px;" @click="login()">Login</button>
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar.vue";
import UserService from "@/services/user-service";

export default {
  components: {
    Navbar
  },
  props: {
    sessionExpired: {
      type: Boolean
    }
  },
  data() {
    return {
      username: "",
      password: ""
    };
  },
  methods: {
    async login() {
      try {
        await UserService.login(this.username, this.password);
        this.$router.push('/admin')
      } catch(e) {
        console.log("Couldn't log in")
        console.log(e)
      }
    }
  }
};
</script>

<style lang="scss" scoped>
#login {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: rgb(55, 255, 175);
  overflow: auto;
}

.custom-content-container {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  justify-content: center;

  width: 100%;
  max-width: 500px;
}

.login-box {
  padding: 50px;
  margin-bottom: 20px;
  border-top: 5px solid #353535;
}
</style>