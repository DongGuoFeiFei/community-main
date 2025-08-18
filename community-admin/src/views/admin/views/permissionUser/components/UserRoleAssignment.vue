<template>
  <div class="user-role-assignment">
    <div class="user-info">
      <h3>用户信息</h3>
      <div class="info-content">
        <div class="avatar">
          <el-avatar :size="80" :src="store.baseURL + user.avatar"/>
        </div>
        <div class="details">
          <p><strong>用户名:</strong> {{ user.username }}</p>
          <p><strong>昵称:</strong> {{ user.nickname || '未设置' }}</p>
          <p><strong>邮箱:</strong> {{ user.email }}</p>
          <p><strong>电话:</strong> {{ user.phone || '未设置' }}</p>
        </div>
      </div>
    </div>

    <div class="role-assignment">
      <h3>角色分配</h3>
      <el-transfer
        v-model="selectedRoleIds"
        :data="formattedRoles"
        :titles="['可用角色', '已分配角色']"
        :props="{
          key: 'roleId',
          label: 'displayName'
        }"
        filterable
        filter-placeholder="搜索角色"
      />
    </div>

    <div class="actions">
      <el-button type="primary" @click="handleSubmit">保存更改</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { localStores } from "@/stores/localStores.js"

const props = defineProps({
  user: {
    type: Object,
    required: true,
    default: () => ({ roles: [] })
  },
  roles: {
    type: Array,
    required: true,
    default: () => []
  }
})

const store = localStores()
const emit = defineEmits(['update-roles'])

// 格式化角色数据，添加显示名称
const formattedRoles = computed(() => {
  return props.roles.map(role => ({
    ...role,
    displayName: `${role.roleName} (${role.roleKey})`
  }))
})

// 使用roleId数组作为v-model
const selectedRoleIds = ref([])

// 初始化已选角色
const initSelectedRoles = () => {
  selectedRoleIds.value = props.user.roles.map(role => role.roleId)
}

// 监听props变化初始化数据
watch(() => props.user, initSelectedRoles, { immediate: true })

// 提交更改
const handleSubmit = () => {
  if (selectedRoleIds.value.length === 0) {
    ElMessage.warning('请至少分配一个角色')
    return
  }
  console.log(selectedRoleIds.value)

  emit('update-roles', selectedRoleIds)
}

// 重置更改
const handleReset = () => {
  initSelectedRoles()
}
</script>

<style lang="scss" scoped>
.user-role-assignment {
  h3 {
    margin-top: 0;
    margin-bottom: 15px;
    color: #303133;
    font-size: 16px;
  }

  .user-info {
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #ebeef5;

    .info-content {
      display: flex;
      gap: 20px;

      .avatar {
        flex-shrink: 0;
      }

      .details {
        p {
          margin: 5px 0;
          line-height: 1.5;

          strong {
            display: inline-block;
            width: 60px;
            color: #909399;
          }
        }
      }
    }
  }

  .role-assignment {
    margin-bottom: 20px;
  }

  .actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
