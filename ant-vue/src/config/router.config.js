import { BasicLayout, UserLayout } from '@/layouts'

const RouteView = {
  name: 'RouteView',
  render: (h) => h('router-view'),
}

export const constantRouterMap = [
  {
    path: '/user',
    name: 'user',
    component: UserLayout,
    children: [
      {
        path: '/user/login',
        name: 'login',
        component: () => import('@/views/user/Login'),
      },
    ],
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404'),
  },
]

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: 'menu.home' },
    redirect: '/welcome/index',
    children: [
      {
        path: '/welcome/index',
        name: 'welcome',
        component: () => import('@/views/welcome/Index'),
        meta: { title: 'menu.welcome.default', keepAlive: false, permission: ['dashboard'] },
      }, {
        path: '/connect/index',
        name: 'connect',
        component: () => import('@/views/connect/Index'),
        meta: { title: 'menu.connect.default', keepAlive: true, permission: ['dashboard'] },
      }, {
        path: '/code/index',
        name: 'code',
        component: () => import('@/views/code/Index'),
        meta: { title: 'menu.code.default', keepAlive: false, permission: ['dashboard'] },
      },
      // dashboard
      {
        path: '/dashboard',
        name: 'dashboard',
        redirect: '/dashboard/welcome',
        hidden: true,
        component: RouteView,
        meta: { title: 'menu.dashboard.default', keepAlive: true, icon: 'dashboard', permission: ['dashboard'] },
        children: [
          {
            path: '/dashboard/welcome',
            name: 'Welcome',
            component: () => import('@/views/dashboard/Welcome'),
            meta: { title: 'menu.dashboard.welcome', keepAlive: false, permission: ['dashboard'] },
          },
        ],
      },
      {
        path: '/form',
        name: 'form',
        hidden: true,
        meta: {
          keepAlive: true,
          title: 'menu.form.default',
          icon: 'video-camera',
        },
        component: RouteView,
        children: [
          {
            path: '/form/basic-form',
            name: 'basic-form',
            meta: {
              keepAlive: true,
              icon: 'smile',
              title: 'menu.form.basicform',
            },
            component: () => import(/* webpackChunkName: "about" */ '../views/form/basic-form'),
          },
          {
            path: '/form/step-form',
            name: 'step-form',
            meta: {
              keepAlive: true,
              icon: 'smile',
              title: 'menu.form.stepform',
            },
            component: () => import(/* webpackChunkName: "about" */ '../views/form/step-form'),
          },
          {
            path: '/form/advanced-form',
            name: 'advanced-form',
            meta: {
              keepAlive: true,
              icon: 'smile',
              title: 'menu.form.advancedform',
            },
            component: () => import(/* webpackChunkName: "about" */ '../views/form/advanced-form'),
          },
        ],
      },
    ],
  },
  {
    path: '*', redirect: '/404', hidden: true,
  },
]
