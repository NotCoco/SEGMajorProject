import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Print from "vue-print-nb"
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faCircleNotch, faStream, faBars, faTimes, faSyncAlt, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons'
import { faFrown, faPaperPlane } from '@fortawesome/free-regular-svg-icons'

import Vuelidate from 'vuelidate'

Vue.config.productionTip = false

Vue.use(Print)

library.add(faCircleNotch, faFrown, faStream, faBars, faTimes, faPaperPlane, faSyncAlt, faEye, faEyeSlash)

Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.use(Vuelidate)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
