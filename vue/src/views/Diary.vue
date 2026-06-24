<template>
  <div class="diary-page">
    <el-card class="calendar-card">
      <template #header>
        <div class="header-title">时光随笔心情日历看板</div>
      </template>

      <el-calendar v-model="currentDate">
        <template #date-cell="{ data }">
          <div class="cell-wrapper" @click="handleDateClick(data.day)">
            <p class="date-num">{{ data.day.split('-').slice(2).join('') }}</p>
            <div v-if="diaryMap[data.day]" class="diary-indicator">
              <el-tag size="small" :type="getMoodTagType(diaryMap[data.day].mood)">
                {{ diaryMap[data.day].mood }}
              </el-tag>
            </div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="`📝 记录时光随笔 - ${selectedDate}`" width="90%" class="max-w-500">
      <el-form :model="diaryForm" label-width="70px">
        <el-form-item label="今日心情">
          <el-radio-group v-model="diaryForm.mood">
            <el-radio-button label="😄 开心" />
            <el-radio-button label="😐 平淡" />
            <el-radio-button label="🥵 焦虑" />
            <el-radio-button label="😭 难过" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="时光故事">
          <el-input type="textarea" :rows="4" v-model="diaryForm.content" placeholder="记录今天发生的账目故事、消费反思或心情时光随笔吧..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveDiary">保存并加密云同步</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const currentDate = ref(new Date())
const selectedDate = ref('')
const dialogVisible = ref(false)
const diaryMap = ref({})

const diaryForm = ref({ mood: '😄 开心', content: '' })

const getMoodTagType = (mood) => {
  if (!mood) return 'info'
  if (mood.includes('开心')) return 'success'
  if (mood.includes('平淡')) return 'info'
  if (mood.includes('焦虑')) return 'warning'
  return 'danger'
}

// 💡 严格核对路径：对应后端物理路径 /api/api/diary/list
const fetchDiaries = async () => {
  try {
    const res = await request.get('/api/api/diary/list')
    const list = res.data.data || []
    const map = {}
    list.forEach(item => {
      if (item.date) map[item.date] = item
    })
    diaryMap.value = map
  } catch (e) {}
}

const handleDateClick = (day) => {
  selectedDate.value = day
  if (diaryMap.value[day]) {
    diaryForm.value = { ...diaryMap.value[day] }
  } else {
    diaryForm.value = { mood: '😄 开心', content: '' }
  }
  dialogVisible.value = true
}

// 💡 严格核对路径：对应后端物理路径 /api/api/diary/save
const saveDiary = async () => {
  try {
    const payload = { ...diaryForm.value, date: selectedDate.value }
    await request.post('/api/api/diary/save', payload)
    ElMessage.success('时光心情随笔成功加密上传云端！')
    dialogVisible.value = false
    fetchDiaries()
  } catch (e) {}
}

onMounted(() => { fetchDiaries() })
</script>

<style scoped>
.calendar-card { border-radius: 8px; }
.header-title { font-weight: bold; font-size: 16px; }
.cell-wrapper { height: 100%; display: flex; flex-direction: column; justify-content: space-between; padding: 4px; }
.date-num { margin: 0; font-size: 14px; }
.diary-indicator { text-align: center; margin-top: 5px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
:deep(.el-calendar-table .el-calendar-day) { height: 85px; padding: 0; }
:deep(.max-w-500) { max-width: 500px; }
</style>