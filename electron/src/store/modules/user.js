import storage from 'store'
import { login, getInfo, logout } from '@/api/auth/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    info: {},
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          console.log('res:', response)
          const { token } = response
          console.log('token', token)
          storage.set(ACCESS_TOKEN, token)
          commit('SET_TOKEN', token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        var userInfo = {
          name: 'New Me',
          avatar: 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png',
          userid: '00000001',
          email: 'antdesign@alipay.com',
          signature: '海纳百川，有容乃大',
          title: '交互专家',
          group: '蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED',
          address: '西湖区工专路 77 号',
          phone: '0752-268888888',
          role: {
            id: 'admin',
            name: '管理员',
            describe: '拥有所有权限',
            status: 1,
            creatorId: 'system',
            createTime: 1497160610259,
            deleted: 0,
            permissions: [{
              roleId: 'admin',
              permissionId: 'dashboard',
              permissionName: '仪表盘',
              actionEntitySet: [{
                action: 'add',
                describe: '新增',
                defaultCheck: false,
              }, {
                action: 'query',
                describe: '查询',
                defaultCheck: false,
              }, {
                action: 'get',
                describe: '详情',
                defaultCheck: false,
              }, {
                action: 'update',
                describe: '修改',
                defaultCheck: false,
              }, {
                action: 'delete',
                describe: '删除',
                defaultCheck: false,
              }],
            }, {
              roleId: 'admin',
              permissionId: 'permission',
              permissionName: '权限管理',
              actionEntitySet: [{
                action: 'add',
                describe: '新增',
                defaultCheck: false,
              }, {
                action: 'get',
                describe: '详情',
                defaultCheck: false,
              }, {
                action: 'update',
                describe: '修改',
                defaultCheck: false,
              }, {
                action: 'delete',
                describe: '删除',
                defaultCheck: false,
              }],
            }],
          },
        }
        if (userInfo.role && userInfo.role.permissions.length > 0) {
          const role = userInfo.role
          role.permissions = userInfo.role.permissions
          role.permissions.map(per => {
            if (per.actionEntitySet != null && per.actionEntitySet.length > 0) {
              const action = per.actionEntitySet.map(action => { return action.action })
              per.actionList = action
            }
          })
          role.permissionList = role.permissions.map(permission => { return permission.permissionId })
          commit('SET_ROLES', userInfo.role)
          commit('SET_INFO', userInfo)
        } else {
          reject(new Error('getInfo: roles must be a non-null array !'))
        }

        commit('SET_NAME', { name: userInfo.name, welcome: '' })
        commit('SET_AVATAR', userInfo.avatar)
        resolve(userInfo)

        // getInfo().then(response => {
        //   if (response.role && response.role.permissions.length > 0) {
        //     const role = response.role
        //     role.permissions = response.role.permissions
        //     role.permissions.map(per => {
        //       if (per.actionEntitySet != null && per.actionEntitySet.length > 0) {
        //         const action = per.actionEntitySet.map(action => { return action.action })
        //         per.actionList = action
        //       }
        //     })
        //     role.permissionList = role.permissions.map(permission => { return permission.permissionId })
        //     commit('SET_ROLES', response.role)
        //     commit('SET_INFO', response)
        //   } else {
        //     reject(new Error('getInfo: roles must be a non-null array !'))
        //   }

        //   commit('SET_NAME', { name: response.name, welcome: '' })
        //   commit('SET_AVATAR', response.avatar)
        //   resolve(response)
        // }).catch(error => {
        //   reject(error)
        // })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          storage.remove(ACCESS_TOKEN)
        })
      })
    },

  },
}

export default user
