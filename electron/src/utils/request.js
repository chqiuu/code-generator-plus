import axios from 'axios'
import storage from 'store'
import notification from 'ant-design-vue/es/notification'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 6000, // 请求超时时间
})

// 异常拦截处理器
const errorHandler = error => {
  if (error.response) {
    const data = error.response.data
    // 从 localstorage 获取 token
    if (error.response.status === 403) {
      notification.error({
        message: 'Forbidden',
        description: data.message,
      })
    }
    if (
      error.response.status === 401 &&
      !(data.result && data.result.isLogin)
    ) {
      notification.error({
        message: 'Unauthorized',
        description: 'Authorization verification failed',
      })
    }
  }
  return Promise.reject(error)
}

// request 拦截器
request.interceptors.request.use(request => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    request.headers['Access-Token'] = token
  }
  return request
}, errorHandler)

// response  拦截器
request.interceptors.response.use(response => {
  return response.data
}, errorHandler)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  },
}

/*
本质上，export default就是输出一个叫做default的变量或方法，然后系统允许你为它取任意名字。所以，下面的写法是有效的。
所以通过以上用法可以总结export default和export区别
1、export default 向外暴露的成员，可以使用任意变量来接收
2、在一个模块中，export default 只允许向外暴露一次
3、在一个模块中，可以同时使用export default 和export 向外暴露成员
4、使用export向外暴露的成员，只能使用{  }的形式来接收，这种形式，叫做【按需导出】
5、export可以向外暴露多个成员，同时，如果某些成员，在import导入时，不需要，可以不在{ }中定义
6、使用export导出的成员，必须严格按照导出时候的名称，来使用{ }按需接收
7、使用export导出的成员，如果想换个变量名称接收，可以使用as来起别名
*/
// export default request
export default axios

export { installer as VueAxios, request as axios }

export const get = (url, params) => {
  return new Promise((resolve, reject) => {
    request({
      url: url,
      method: 'get',
      params: params,
    })
      .then(res => {
        if (res.code === 1) {
          resolve(res.data)
        } else {
          reject(res.message)
        }
      })
      .catch(() => {
        reject(new Error('服务器错误'))
      })
  })
}

export const post = (url, data) => {
  return new Promise((resolve, reject) => {
    request({
      url: url,
      method: 'post',
      data: data,
    })
      .then(res => {
        if (res.code === 1) {
          resolve(res.data)
        } else {
          reject(res.message)
        }
      })
      .catch(() => {
        reject(new Error('服务器错误'))
      })
  })
}

export const postDownload = (url, data) => {
  return new Promise((resolve, reject) => {
    axios({
      // API 请求的默认前缀
      baseURL: process.env.VUE_APP_API_BASE_URL,
      url: url,
      responseType: 'blob',
      method: 'post',
      data: data,
    })
      .then(res => {
        if (res.status === 200) {
          const content = res.data
          const blob = new Blob([content])
          let fileName = ''
          if (res.headers['content-disposition'].split(';').length > 0) {
            fileName = res.headers['content-disposition']
              .split(';')[1]
              .split('=')[1]
          } else {
            fileName = 'data.csv'
          }
          if ('download' in document.createElement('a')) {
            // 非IE下载
            const elink = document.createElement('a')
            elink.download = fileName
            elink.style.display = 'none'
            elink.href = URL.createObjectURL(blob)
            document.body.appendChild(elink)
            elink.click()
            URL.revokeObjectURL(elink.href) // 释放URL 对象
            document.body.removeChild(elink)
          } else {
            // IE10+下载
            navigator.msSaveBlob(blob, fileName)
          }
        } else {
          reject(new Error('文件下载失败'))
        }
      })
      .catch(() => {
        reject(new Error('服务器错误'))
      })
  })
}
