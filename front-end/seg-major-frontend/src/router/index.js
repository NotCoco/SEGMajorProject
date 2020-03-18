import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

import AdminLayout from '../views/AdminLayout.vue'
import AdminHome from '../views/AdminHome.vue'
import AdminAllSites from '../views/AdminAllSites.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      {
        path: '',
        component: AdminHome
      },
      {
        path: 'sites',
        component: AdminAllSites
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
