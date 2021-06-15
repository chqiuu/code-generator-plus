import 'core-js/stable'
import 'regenerator-runtime/runtime'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import i18n from './locales'
import bootstrap from './core/bootstrap'
import {
  ConfigProvider, Icon, Button, Tag, Menu, Dropdown, Avatar, Spin, Result, Form, Tabs, Input, Checkbox, Row,
  Col, Modal, Alert, Divider, notification, message,
  Radio, Card, InputNumber, Table, Tooltip, Select, Badge, Popover,
} from 'ant-design-vue'
import ProLayout, { PageHeaderWrapper } from '@ant-design-vue/pro-layout'
import { PageLoading } from '@/components'
import themeConfig from './config/theme.config.js'

// 路由守卫
import './router/router-guards'
// 其他
import './styles/global.less'

import 'github-markdown-css/github-markdown.css'
import hljs from 'highlight.js'

// Ant Design Vue
Vue.use(ConfigProvider)
Vue.use(Icon)
Vue.use(Tag)
Vue.use(Button)
Vue.use(Menu)
Vue.use(Dropdown)
Vue.use(Avatar)
Vue.use(Spin)
Vue.use(Result)
Vue.use(Form)
Vue.use(Tabs)
Vue.use(Input)
Vue.use(Checkbox)
Vue.use(Select)
Vue.use(Row)
Vue.use(Col)
Vue.use(Modal)
Vue.use(Alert)
Vue.use(Divider)
Vue.use(Radio)
Vue.use(Card)
Vue.use(InputNumber)
Vue.use(Table)
Vue.use(Tooltip)
Vue.use(Badge)
Vue.use(Popover)

Vue.prototype.$confirm = Modal.confirm
Vue.prototype.$message = message
Vue.prototype.$notification = notification
Vue.prototype.$info = Modal.info
Vue.prototype.$success = Modal.success
Vue.prototype.$error = Modal.error
Vue.prototype.$warning = Modal.warning

// ProLayout
Vue.component('pro-layout', ProLayout)
Vue.component('page-container', PageHeaderWrapper)
Vue.component('page-header-wrapper', PageHeaderWrapper)
window.umi_plugin_ant_themeVar = themeConfig.theme

// Global imports
Vue.use(PageLoading)

Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n,
  created: bootstrap,
  render: h => h(App),
}).$mount('#app')

// 如果开启了typescript 需要额外安装 npm install @types/highlight.js
// 通过 import * as hljs from 'highlight.js' 引入
Vue.directive('highlight', function (el) {
  const blocks = el.querySelectorAll('pre code')
  blocks.forEach(block => {
    hljs.highlightBlock(block)
  })
})
