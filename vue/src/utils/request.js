import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '', // 留空，自动走 vite.config.ts 的 /api 代理
  timeout: 10000
})

// 请求拦截器：确保每次向后端发送数据时，头部的 Authorization 格式100%符合后端的严格规范
request.interceptors.request.use(
  config => {
    let token = localStorage.getItem('timeledger_token')
    if (token) {
      // 💡 双保险防御：如果提取出来的 token 字符串没有包含 Bearer 前缀，前端自动帮它补齐！
      if (!token.startsWith('Bearer ')) {
        token = 'Bearer ' + token
      }
      config.headers['Authorization'] = token
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 兼容后端 Result.success 或者是普通 200 返回值
    if (res.code && res.code !== 200) {
      ElMessage.error(res.message || '数据云同步发生异常')
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      ElMessage.error('您的登录同步凭证已失效，请重新登录')
      localStorage.removeItem('timeledger_token')
      window.location.href = '#/login'
    } else {
      ElMessage.error(error.message || '连接云端失败，请检查后端运行状态')
    }
    return Promise.reject(error)
  }
)

export default request