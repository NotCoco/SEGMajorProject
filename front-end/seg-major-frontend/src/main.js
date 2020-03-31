import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Print from "vue-print-nb"
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCircleNotch } from '@fortawesome/free-solid-svg-icons'
import { faFrown } from '@fortawesome/free-regular-svg-icons'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'

import Vuelidate from 'vuelidate'

Vue.config.productionTip = false

Vue.use(Print)
// Install BootstrapVue
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)
library.add(faCircleNotch, faFrown)
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.use(Vuelidate)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
