import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '', // 留空，自动走 vite.config.js 的 /api 代理
  timeout: 10000 // 10秒超时控制，保障移动多端在恶劣网络环境下的同步稳定
})

// 请求拦截器：每次向后端请求时，自动从浏览器中提取 JWT 凭证挂载到 Header
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('timeledger_token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器：解析业务状态码，保护财务隐私
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
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