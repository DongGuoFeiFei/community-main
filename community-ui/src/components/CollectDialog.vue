<template>
  <el-dialog
      v-model="dialogVisible"
      title="收藏文章"
      width="400px"
      :before-close="handleClose"
  >
    <div class="collect-dialog">
      <!-- 收藏夹列表 -->
      <div class="folder-list">
        <el-checkbox-group v-model="selectedFolders">
          <div
              v-for="folder in folders"
              :key="folder.id"
              class="folder-item"
          >
            <el-checkbox :value="folder.id">
              <div class="folder-info">
                <span class="folder-name">{{ folder.name }}</span>
                <span class="folder-count">{{ folder.articleCount }}篇</span>
              </div>
            </el-checkbox>
          </div>
        </el-checkbox-group>
      </div>

      <!-- 新建收藏夹区域 -->
      <div class="new-folder-section">
        <el-input
            v-model="newFolderName"
            placeholder="新建收藏夹"
            size="small"
            clearable
            @keyup.enter="handleCreateFolder"
        >
          <template #append>
            <el-button
                type="primary"
                size="small"
                @click="handleCreateFolder"
                :disabled="!newFolderName"
            >
              创建
            </el-button>
          </template>
        </el-input>
      </div>

      <!-- 底部操作按钮 -->
      <div class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button
            type="primary"
            @click="handleConfirm"
            :disabled="selectedFolders.length === 0"
        >
          确定({{ selectedFolders.length }})
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import {defineEmits, defineProps, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {collectArticle, createFolder, fetchFolders} from '@/api/collectApi'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  articleId: {
    type: [String, Number],
    required: true
  }
})

console.log("props", props)

const emit = defineEmits(['update:visible', 'success'])

const dialogVisible = ref(props.visible)
const folders = ref([])
const selectedFolders = ref([])
const newFolderName = ref('')
const isLoading = ref(false)

// 获取用户收藏夹列表
const fetchFolderList = async () => {
  try {
    const res = await fetchFolders()
    folders.value = res.data || []
  } catch (error) {
    console.error('获取收藏夹失败:', error)
    ElMessage.error('获取收藏夹失败')
  }
}

// 创建新收藏夹
const handleCreateFolder = async () => {
  if (!newFolderName.value.trim()) {
    ElMessage.warning('请输入收藏夹名称')
    return
  }

  try {
    isLoading.value = true
    const res = await createFolder({name: newFolderName.value})
    console.log(res)
    folders.value.unshift({
      ...res.data,
      articleCount: 0
    })
    console.log("folders.value", folders.value)
    selectedFolders.value.push(res.data.id)
    newFolderName.value = ''
    ElMessage.success('收藏夹创建成功')
  } catch (error) {
    console.error('创建收藏夹失败:', error)
    ElMessage.error(error.response?.data?.msg || '创建收藏夹失败')
  } finally {
    isLoading.value = false
  }
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
  emit('update:visible', false)
}

// 取消操作
const handleCancel = () => {
  handleClose()
}

// 确认收藏
const handleConfirm = async () => {
  try {
    const promises = selectedFolders.value.map(folderId =>
        collectArticle({
          articleId: props.articleId,
          folderId
        })
    )
    await Promise.all(promises)
    emit('success')
    handleClose()
  } catch (error) {
    console.error('收藏失败:', error)
    ElMessage.error(error.response?.data?.msg || '收藏失败')
  }
}

// 监听props变化
watch(() => props.visible, (val) => {
  dialogVisible.value = val
  if (val) {
    fetchFolderList()
    selectedFolders.value = []
    newFolderName.value = ''
  }
})

watch(dialogVisible, (val) => {
  emit('update:visible', val)
})
</script>

<style scoped>
.collect-dialog {
  display: flex;
  flex-direction: column;
  max-height: 60vh;
}

.folder-list {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 16px;
  padding-right: 8px;
}

.folder-item {
  padding: 8px 0;
  border-bottom: 1px solid var(--el-border-color-light);
}

.folder-info {
  display: flex;
  align-items: center;
  width: 100%;
}

.folder-name {
  flex: 1;
  margin-left: 8px;
}

.folder-count {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.new-folder-section {
  margin: 12px 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

/* 滚动条样式 */
.folder-list::-webkit-scrollbar {
  width: 6px;
}

.folder-list::-webkit-scrollbar-thumb {
  background-color: var(--el-border-color-dark);
  border-radius: 3px;
}

.folder-list::-webkit-scrollbar-track {
  background-color: var(--el-border-color-lighter);
}
</style>