<template>
  <div class="points-rule-list">
    <div class="header">
      <h2>积分规则管理</h2>
      <el-button type="primary" @click="showAddDialog">新增规则</el-button>
    </div>

    <el-table :data="ruleList" border style="width: 100%">
      <el-table-column prop="ruleCode" label="规则代码" width="150" />
      <el-table-column prop="ruleName" label="规则名称" width="150" />
      <el-table-column prop="ruleType" label="规则类型" width="120">
        <template #default="{row}">
          {{ ruleTypeMap[row.ruleType] }}
        </template>
      </el-table-column>
      <el-table-column prop="pointsValue" label="积分值" width="100" />
      <el-table-column prop="dailyLimit" label="每日上限" width="100">
        <template #default="{row}">
          {{ row.dailyLimit === 0 ? '无限制' : row.dailyLimit }}
        </template>
      </el-table-column>
      <el-table-column prop="totalLimit" label="总上限" width="100">
        <template #default="{row}">
          {{ row.totalLimit === 0 ? '无限制' : row.totalLimit }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row.ruleId)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="queryParams.pageNum"
      v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 30, 50]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <PointsRuleDialog
      v-model="dialogVisible"
      :current-rule="currentRule"
      :dialog-type="dialogType"
      @refresh="fetchRuleList"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PointsRuleDialog from './PointsRuleDialog.vue'
import {deletePointsRule, getPointsRules} from "@/api/rules.js";
import {ElMessage, ElMessageBox} from "element-plus";


const ruleList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentRule = ref({})
const dialogType = ref('add')

const ruleTypeMap = {
  1: '获取积分',
  2: '消费积分'
}

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  ruleName: '',
  ruleType: null,
  status: null
})

onMounted(() => {
  fetchRuleList()
})

const fetchRuleList = async () => {
  try {
    const res = await getPointsRules(queryParams.value)
    ruleList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    console.error('获取积分规则列表失败:', error)
  }
}

const showAddDialog = () => {
  currentRule.value = {}
  dialogType.value = 'add'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  currentRule.value = { ...row }
  dialogType.value = 'edit'
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该积分规则吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePointsRule(id)
    ElMessage.success('删除成功')
    fetchRuleList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (val) => {
  queryParams.value.pageSize = val
  fetchRuleList()
}

const handleCurrentChange = (val) => {
  queryParams.value.pageNum = val
  fetchRuleList()
}
</script>

<style lang="scss" scoped>
.points-rule-list {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    justify-content: flex-end;
  }
}
</style>
