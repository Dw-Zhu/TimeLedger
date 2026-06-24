<template>
  <el-container class="layout-container">
    <el-aside width="240px" class="hidden-xs-only">
      <el-menu router default-active="/dashboard" class="side-menu">
        <div class="logo-area">⏱️ 拾光账本</div>
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>收支核心工作台</span>
        </el-menu-item>
        <el-menu-item index="/diary">
          <el-icon><Calendar /></el-icon>
          <span>时光随笔日记</span>
        </el-menu-item>
        <el-menu-item @click="handleLogout" class="logout-item">
          <el-icon><SwitchButton /></el-icon>
          <span>安全注销退出</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="mobile-header hidden-sm-and-up">
        <span>⏱️ 拾光账本</span>
        <el-button size="small" type="danger" @click="handleLogout">安全退出</el-button>
      </el-header>
      <el-main class="main-body">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { ElMessageBox, ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  ElMessageBox.confirm('确定要安全注销退出当前的记账空间吗？', '提示', { type: 'warning' }).then(() => {
    userStore.clearToken()
    ElMessage.success('已安全退出')
    router.push('/login')
  })
}
</script>

<style scoped>
.layout-container { height: 100vh; }
.side-menu { height: 100%; border-right: none; background-color: #304156; color: #fff; }
.side-menu :deep(.el-menu-item) { color: #bfcbd9; }
.side-menu :deep(.el-menu-item.is-active) { color: #409EFF; background-color: #263445; }
.logo-area { height: 60px; line-height: 60px; text-align: center; font-size: 18px; font-weight: bold; color: #fff; background-color: #2b3b4e; }
.mobile-header { background-color: #304156; color: white; display: flex; justify-content: space-between; align-items: center; padding: 0 15px; height: 50px; }
.main-body { background-color: #f0f2f5; padding: 20px; overflow-y: auto; }
.logout-item { margin-top: auto; }
</style>