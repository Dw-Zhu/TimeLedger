<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="title-container">
        <h2>⏱️ 拾光账本 TimeLedger</h2>
        <p>{{ isRegister ? '新用户注册中心' : '在线云同步财务空间' }}</p>
      </div>

      <el-form :model="form" label-width="0">
        <el-form-item>
          <el-input v-model="form.username" :placeholder="isRegister ? '请输入用户名' : '请输入绑定的邮箱账号'" prefix-icon="User" />
        </el-form-item>

        <el-form-item>
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item v-if="isRegister">
          <el-input v-model="form.email" placeholder="请输入绑定邮箱" prefix-icon="Message" />
        </el-form-item>

        <el-button type="primary" class="submit-btn" @click="handleSubmit" :loading="loading">
          {{ isRegister ? '立即注册并开通空间' : '安全登录并开始记账' }}
        </el-button>
      </el-form>

      <div class="switch-link">
        <el-link type="info" @click="isRegister = !isRegister">
          {{ isRegister ? '已有拾光账号？前往登录' : '没有账号？立即开通新账本空间' }}
        </el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const isRegister = ref(false)
const loading = ref(false)

const form = reactive({ username: '', password: '', email: '', nickname: '' })

const handleSubmit = async () => {
  loading.value = true
  try {
    // 💡 绝杀对齐点：后端的控制器映射是 /api/auth/register 和 /api/auth/login
    const url = isRegister.value ? '/api/auth/register' : '/api/auth/login'

    if (isRegister.value) {
      form.nickname = form.username
    } else {
      form.email = form.username
    }

    const res = await request.post(url, form)
    if (!isRegister.value) {
      const tokenStr = res.data.data.token
      userStore.setToken(tokenStr)
      ElMessage.success('拾光凭证校验成功，正在进入个人记账空间...')
      router.push('/')
    } else {
      ElMessage.success('新空间开通成功！已自动切换为登录模式')
      isRegister.value = false
    }
  } catch (e) {
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container { height: 100vh; display: flex; justify-content: center; align-items: center; background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); }
.login-card { width: 100%; max-width: 400px; padding: 20px; border-radius: 12px; }
.title-container { text-align: center; margin-bottom: 30px; }
.submit-btn { width: 100%; margin-top: 10px; }
.switch-link { text-align: center; margin-top: 20px; }
</style>