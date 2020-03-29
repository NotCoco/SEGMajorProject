<template>
  <div id="login">
    <div id="bg"></div>
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
@import '@/styles';

#login {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: auto;

  #bg {
    z-index: -1;
    position: absolute;
    width: 100%;
    height: 100%;
    background-color: rgb(52, 243, 167);
    background-image: linear-gradient(45deg, rgb(55, 255, 175) 25%, transparent 25%, transparent 75%, rgb(37, 247, 163) 75%, rgb(82, 247, 181)),
                      linear-gradient(45deg, rgb(43, 238, 160) 25%, transparent 25%, transparent 75%, rgb(62, 250, 175) 75%, rgb(55, 255, 175));
    background-size: 200px 200px;
    background-position: 0 0, 100px 100px;
    filter: blur(1px);
  }
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