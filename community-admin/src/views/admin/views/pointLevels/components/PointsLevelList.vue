<template>
  <div class="points-level-list">
    <div class="header">
      <h2>等级管理</h2>
      <el-button type="primary" @click="showAddDialog">新增等级</el-button>
    </div>

    <el-table :data="levelList" border style="width: 100%">
      <el-table-column prop="levelId" label="等级ID" width="100" />
      <el-table-column prop="levelName" label="等级名称" width="150" />
      <el-table-column label="等级图标" width="120">
        <template #default="{row}">
          <el-image
            v-if="row.levelIcon"
            :src="row.levelIcon"
            style="width: 40px; height: 40px"
            fit="cover"
          />
          <span v-else>无</span>
        </template>
      </el-table-column>
      <el-table-column label="经验范围" width="200">
        <template #default="{row}">
          {{ row.minExperience }} - {{ row.maxExperience }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row.levelId)"
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

    <PointsLevelDialog
      v-model="dialogVisible"
      :current-level="currentLevel"
      :dialog-type="dialogType"
      @refresh="fetchLevelList"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PointsLevelDialog from './PointsLevelDialog.vue'

import {ElMessage, ElMessageBox} from "element-plus";
import {deletePointsLevel, getPointsLevels} from "@/api/levels.js";

const levelList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentLevel = ref({})
const dialogType = ref('add')

const queryParams = ref({
  pageNum: 1,
  pageSize: 10
})

onMounted(() => {
  fetchLevelList()
})

const fetchLevelList = async () => {
  try {
    const res = await getPointsLevels(queryParams.value)
    levelList.value = res.data.list
    total.value = res.data.total
  } catch (error) {
    console.error('获取等级列表失败:', error)
  }
}

const showAddDialog = () => {
  currentLevel.value = {}
  dialogType.value = 'add'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  currentLevel.value = { ...row }
  dialogType.value = 'edit'
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该等级吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePointsLevel(id)
    ElMessage.success('删除成功')
    fetchLevelList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (val) => {
  queryParams.value.pageSize = val
  fetchLevelList()
}

const handleCurrentChange = (val) => {
  queryParams.value.pageNum = val
  fetchLevelList()
}
</script>

<style lang="scss" scoped>
.points-level-list {
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
