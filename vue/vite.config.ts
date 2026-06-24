import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 💡 绝杀 404：只要前端请求的是 /api/xxx，强制补齐为 /api/api/xxx 转发给后端
        rewrite: (path) => path.replace(/^\/api/, '/api')
      }
    }
  }
})