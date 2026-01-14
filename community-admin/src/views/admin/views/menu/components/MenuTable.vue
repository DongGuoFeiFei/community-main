<template>
  <div class="menu-table-container">
    <el-table
      :data="tableData"
      row-key="menuId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      v-loading="loading"
      border
      style="width: 100%"
    >
      <el-table-column prop="menuName" label="菜单名称" width="120"/>
      <el-table-column prop="menuType" label="类型" width="80">
        <template #default="{ row }">
          <el-tag :type="getMenuTypeTag(row.menuType)">
            {{ getMenuTypeName(row.menuType) }}
          </el-tag>
        </template>
      </el-table-column>
      <!--   todo 修改菜单图标的映射功能，使得菜单内容可以随意修改   -->
      <el-table-column prop="icon" label="图标" width="60">
        <template #default="{ row }">
          <el-icon v-if="row.icon && row.icon !== '#'">
            <component :is="row.icon"/>
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路由路径" width="190"/>
      <el-table-column prop="component" label="组件路径" width="190"/>
      <el-table-column prop="perms" label="权限标识" width="200">
        <template #default="{ row }">
          <el-tag v-if="row.perms">
            {{ row.perms }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="visible" label="显示状态" width="90">
        <template #default="{ row }">
          <el-switch
            v-model="row.visible"
            :active-value="1"
            :inactive-value="0"
            disabled
          />
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status ? 'success' : 'danger'">
            {{ row.status ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="emit('edit', row)">编辑</el-button>
          <el-button size="small" type="danger" @click="emit('delete', row.menuId)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>


const props = defineProps({
  tableData: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['add', 'edit', 'delete'])

const getMenuTypeName = (type) => {
  const types = {
    M: '目录',
    C: '菜单',
    F: '按钮',
    L: '链接'
  }
  return types[type] || type
}

const getMenuTypeTag = (type) => {
  const tags = {
    M: 'primary', // 默认不设置type，使用默认样式
    C: 'success', // 成功样式
    F: 'warning', // 警告样式
    L: 'info'     // 信息样式
  }
  return tags[type] || '' // 确保返回的是有效值或空字符串
}
</script>

<style lang="scss" scoped>
.menu-table-container {
  background-color: #fff;
  border-radius: 4px;
  padding: 10px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}
</style>
