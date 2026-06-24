<template>
  <!-- 只有一个单纯的路由视图出口，用来承载不同的页面（登录页/主看板） -->
  <router-view />
</template>

<script setup>
// 纯净的内容出口，无需任何多余逻辑
</script>

<style>
/* 全局清除浏览器默认内外边距，确保自适应铺满全屏 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}
#app {
  height: 100%;
}
</style>