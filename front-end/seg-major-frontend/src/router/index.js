import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

import AdminLayout from '../views/AdminLayout.vue'
import AdminHome from '../views/AdminHome.vue'
import AdminAllSites from '../views/AdminAllSites.vue'
import AdminNewSite from '../views/AdminNewSite.vue'
import AdminSiteLayout from '../views/AdminSiteLayout.vue'
import AdminAllPages from '../views/AdminAllPages.vue'
import AdminEditPage from '../views/AdminEditPage.vue'
import SiteContentViewerLayout from '../views/SiteContentViewerLayout.vue'
import PageViewer from '../views/PageViewer.vue'

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
        component: AdminAllSites,
      },
      {
        path: 'sites/new',
        component: AdminNewSite
      },
      {
        path: 'sites/:siteName',
        component: AdminSiteLayout,
        children: [
          {
            path: '',
            redirect: 'pages'
          },
          {
            path: 'pages',
            component: AdminAllPages
          },
          {
            path: 'pages/new',
            component: AdminEditPage,
            props: { newPage: true }
          },
          {
            path: 'pages/:pageSlug',
            component: AdminEditPage,
            props: { newPage: false }
          }
        ]
      }
    ]
  },
  {
    path: '/:siteName',
    component: SiteContentViewerLayout,
    children: [
      {
        path: ':pageSlug',
        component: PageViewer
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
