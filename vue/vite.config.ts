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
          // 💡 核心破局点：前端请求的是 /api/auth/...
          // 转发给带 context-path 的后端时，强行重写为 /api/api/auth/... 从而完美命中后端
          rewrite: (path) => path.replace(/^\/api/, '/api/api')
        }
      }
    }
})