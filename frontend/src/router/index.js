import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('@/views/Machines')
  },
  {
    path: '/cabinet',
    component: () => import('@/views/Cabinet')
  },
  {
    path: '/balance',
    component: () => import('@/views/Balance')
  },
  {
    path: '/admin/cabinet',
    component: () => import('@/views/AdminCabinet')
  },
  {
    path: '/admin/auth',
    component: () => import('@/views/AdminAuth')
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
