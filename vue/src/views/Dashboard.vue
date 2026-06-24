<template>
  <div class="dashboard-page">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="8">
        <el-card class="data-card card-income">
          <div class="label">本月累计云同步收入</div>
          <div class="value">￥{{ totalIncome.toFixed(2) }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="data-card card-expense">
          <div class="label">本月累计云同步支出</div>
          <div class="value">￥{{ totalExpense.toFixed(2) }}</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="8">
        <el-card class="data-card card-budget">
          <div class="label">月度预算超支红线警报进度</div>
          <el-progress :percentage="budgetPercentage" :status="budgetPercentage >= 100 ? 'exception' : 'success'" :stroke-width="12" />
          <div class="sub-label">预算总额：￥{{ monthlyBudget }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>📊 联动财务流水实时账目</span>
          <el-button type="primary" icon="Plus" @click="showAddDialog = true">记一笔新增流水</el-button>
        </div>
      </template>

      <el-table :data="records" style="width: 100%" v-loading="loading">
        <el-table-column prop="date" label="交易日期" width="150" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.type === '收入' ? 'success' : 'danger'">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="amount" label="金额 (元)">
          <template #default="scope">
            <span :class="scope.row.type === '收入' ? 'text-income' : 'text-expense'">
              {{ scope.row.type === '收入' ? '+' : '-' }}￥{{ scope.row.amount.toFixed(2) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="交易备注" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-dialog v-model="showAddDialog" title="📝 记一笔新账目" width="90%" class="max-w-500">
      <el-form :model="newRecord" label-width="80px">
        <el-form-item label="流水类型">
          <el-radio-group v-model="newRecord.type">
            <el-radio-button label="支出" />
            <el-radio-button label="收入" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="交易分类">
          <el-select v-model="newRecord.category" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="item in categories[newRecord.type]" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="交易金额">
          <el-input-number v-model="newRecord.amount" :min="0.01" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="记账日期">
          <el-date-picker v-model="newRecord.date" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="事务备注">
          <el-input v-model="newRecord.remark" type="textarea" placeholder="请输入流水备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAddRecord">确认提交云端</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const showAddDialog = ref(false)
const records = ref([])
const monthlyBudget = ref(3000.00)

const newRecord = ref({ type: '支出', category: '餐饮美食', amount: 10, date: new Date().toISOString().split('T')[0], remark: '' })
const categories = {
  '支出': ['餐饮美食', '休闲娱乐', '交通出行', '数码电器', '人情往来', '其他支出'],
  '收入': ['薪资收入', '运营投资', '分红股息', '其他收入']
}

const totalIncome = computed(() => records.value.filter(r => r.type === '收入').reduce((sum, r) => sum + r.amount, 0))
const totalExpense = computed(() => records.value.filter(r => r.type === '支出').reduce((sum, r) => sum + r.amount, 0))
const budgetPercentage = computed(() => {
  if (monthlyBudget.value <= 0) return 0
  const pct = Math.round((totalExpense.value / monthlyBudget.value) * 100)
  return pct > 100 ? 100 : pct
})

// 💡 严格核对：Axios 发送 /api/finance/list，经过 Vite 代理重写后化为 /api/api/finance/list 完美命中后端！
const fetchRecords = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/finance/list')
    records.value = res.data.data || []
  } catch(e) {}
  finally { loading.value = false }
}

// 💡 严格核对：Axios 发送 /api/finance/add，经过 Vite 代理重写后化为 /api/api/finance/add 完美命中后端！
const handleAddRecord = async () => {
  try {
    await request.post('/api/finance/add', newRecord.value)
    ElMessage.success('财务流水同步成功！')
    showAddDialog.value = false
    fetchRecords()
    newRecord.value = { type: '支出', category: '餐饮美食', amount: 10, date: new Date().toISOString().split('T')[0], remark: '' }
  } catch(e) {}
}

onMounted(() => { fetchRecords() })
</script>

<style scoped>
.dashboard-page { padding: 10px; }
.data-card { margin-bottom: 20px; border-radius: 8px; }
.card-income { border-left: 5px solid #67C23A; }
.card-expense { border-left: 5px solid #F56C6C; }
.card-budget { border-left: 5px solid #E6A23C; }
.label { font-size: 14px; color: #909399; margin-bottom: 10px; }
.value { font-size: 24px; font-weight: bold; color: #303133; }
.sub-label { font-size: 12px; color: #909399; margin-top: 10px; }
.table-card { margin-top: 10px; border-radius: 8px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.text-income { color: #67C23A; font-weight: bold; }
.text-expense { color: #F56C6C; font-weight: bold; }
:deep(.max-w-500) { max-width: 500px; }
</style>