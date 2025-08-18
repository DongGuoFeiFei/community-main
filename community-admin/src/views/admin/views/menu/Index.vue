<template>
  <div class="menu-management-container">
    <!-- 搜索组件 -->
    <menu-search @search="handleSearch" @reset="handleReset"/>

    <el-row :gutter="10">
      <el-col :span="5">
        <menu-tree
          :menus="menuTrees"
          @node-click="handleNodeClick"
          @add="handleAdd"
          @edit="handleEdit"
        />
      </el-col>
      <el-col :span="19">
        <menu-table
          :table-data="tableData"
          :loading="loading"
          @add="handleAdd"
          @edit="handleEdit"
          @delete="handleDelete"
        />
      </el-col>
    </el-row>

    <!-- 分页组件 -->
    <menu-pagination
      :pagination="pagination"
      @page-change="handlePageChange"
    />

    <menu-form
      ref="menuFormRef"
      :visible="formVisible"
      :form-data="currentMenu"
      :menu-tree="menuTrees"
      @submit="handleSubmit"
      @cancel="formVisible = false"
    />
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import MenuTable from "@/views/admin/views/menu/components/MenuTable.vue";
import menuTree from "@/views/admin/views/menu/components/menuTree.vue";
import MenuForm from "@/views/admin/views/menu/components/MenuForm.vue";
import {addMenu, deleteMenu, getMenuList, getMenuTree, updateMenu} from "@/api/menu.js";
import MenuSearch from "@/views/admin/views/menu/components/MenuSearch.vue";
import MenuPagination from "@/views/admin/views/menu/components/MenuPagination.vue";

const loading = ref(false)
const menuTrees = ref([])
const tableData = ref([])
const formVisible = ref(false)
const currentMenu = ref({})
const menuFormRef = ref(null)


// 分页数据
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 搜索条件
const searchParams = ref({})

// 获取菜单数据
const fetchMenuData = async () => {
  try {
    loading.value = true
    const params = {
      ...searchParams.value,
      pageNum: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    }
    console.log(params)

    const [treeRes, listRes] = await Promise.all([
      getMenuTree(),
      getMenuList(params)
    ])

    menuTrees.value = treeRes.data
    tableData.value = listRes.data.rows || []
    pagination.value.total = listRes.data.total || 0
  } catch (error) {
    console.error('获取菜单数据失败:', error)
    ElMessage.error('获取菜单数据失败')
  } finally {
    loading.value = false
  }
}

// 处理搜索
const handleSearch = (params) => {
  searchParams.value = params
  pagination.value.currentPage = 1
  fetchMenuData()
}

// 处理重置
const handleReset = () => {
  searchParams.value = {}
  pagination.value.currentPage = 1
  fetchMenuData()
}

// 处理分页变化
const handlePageChange = (params) => {
  pagination.value = {...pagination.value, ...params}
  fetchMenuData()
}

// 处理节点点击
const handleNodeClick = (node) => {
  // 可以根据需要筛选表格数据
  console.log('选中节点:', node)
}

// 处理添加
const handleAdd = (parentId = 0) => {
  currentMenu.value = {
    parentId,
    menuType: 'M',
    visible: 1,
    status: 1,
    isExternal: 0,
    target: '_self'
  }
  formVisible.value = true
}


// 处理编辑
const handleEdit = (row) => {
  currentMenu.value = {...row}
  formVisible.value = true
}

// 处理删除
const handleDelete = async (menuId) => {
  try {
    await ElMessageBox.confirm('确认删除该菜单吗？删除后无法恢复！', '提示', {
      type: 'warning'
    })
    await deleteMenu(menuId)
    ElMessage.success('删除成功')
    fetchMenuData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 处理表单提交
const handleSubmit = async (formData) => {
  try {
    if (formData.menuId) {
      await updateMenu(formData)
      ElMessage.success('更新成功')
    } else {
      await addMenu(formData)
      ElMessage.success('添加成功')
    }
    formVisible.value = false
    fetchMenuData()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

onMounted(() => {
  fetchMenuData()
})
</script>

<style lang="scss" scoped>
.menu-management-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}
</style>
