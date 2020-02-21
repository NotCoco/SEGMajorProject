import Vue from 'vue'
import VueRouter from 'vue-router'

import ContentViewerLayout from '../views/ContentViewerLayout.vue'
import ContentViewerPage from '../views/ContentViewerPage.vue'

import Introduction from '../views/demo-pages/Introduction.vue'
import Nutrition from '../views/demo-pages/Nutrition.vue'
import Medications from '../views/demo-pages/Medications.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: ContentViewerLayout,
    children: [
      {
        path: '/',
        component: ContentViewerPage,
        children: [
          {
            path: '/',
            component: Introduction
          },
          {
            path: 'Nutrition',
            component: Nutrition
          },
          {
            path: 'Medications',
            component: Medications
          },
        ]
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
