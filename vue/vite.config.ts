server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 👇 加入这行：如果后端接口本身没有 /api 前缀，自动将其去掉
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }