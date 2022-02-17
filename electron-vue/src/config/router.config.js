import { BasicLayout, UserLayout } from '@/layouts'

const RouteView = {
  name: 'RouteView',
  render: h => h('router-view'),
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
    component: () =>
      import(/* webpackChunkName: "fail" */ '@/views/exception/404'),
  },
]

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: 'menu.home' },
    redirect: '/connect/index',
    children: [

      {
        path: '/connect/index',
        name: 'connect',
        component: () => import('@/views/connect/Index'),
        meta: {
          title: 'menu.connect.default',
          keepAlive: true,
          permission: ['dashboard'],
        },
      },
      {
        path: '/code/index',
        name: 'code',
        component: () => import('@/views/code/Index'),
        meta: {
          title: 'menu.code.default',
          keepAlive: false,
          permission: ['dashboard'],
        },
      }, {
        path: '/welcome/index',
        name: 'welcome',
        component: () => import('@/views/welcome/Index'),
        meta: {
          title: 'menu.welcome.default',
          keepAlive: false,
          permission: ['dashboard'],
        },
      },
    ],
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true,
  },
]
