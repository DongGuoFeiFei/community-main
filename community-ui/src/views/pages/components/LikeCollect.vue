<template>
  <div class="interaction-buttons">
    <!-- 点赞按钮 -->
    <el-button
        :icon="isLiked ? 'CircleCheckFilled' : 'CircleCheck'"
        :type="isLiked ? 'primary' : ''"
        @click="handleLike"
        :loading="likeLoading"
        class="interaction-button"
    >
      {{ likeCount }}
    </el-button>

    <!-- 收藏按钮 -->
    <el-button
        :icon="isCollected ? 'StarFilled' : 'Star'"
        :type="isCollected ? 'warning' : ''"
        @click="handleCollect"
        :loading="collectLoading"
        class="interaction-button"
    >
      {{ collectedCount }}
    </el-button>

    <!-- 分享按钮 -->
    <ShareButton
        :item-id="itemId"
        :share-url="shareUrl"
        :share-title="shareTitle"
        @share="handleShare"
        class="interaction-button"
    />

    <!-- 收藏弹窗 -->
    <CollectDialog
        v-if="showCollectDialog"
        v-model:visible="collectDialogVisible"
        :articleId="itemId"
        @success="handleCollectSuccess"
    />
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { localStores } from "@/stores/localStores.js";
import CollectDialog from "@/views/pages/components/CollectDialog.vue";
import ShareButton from "@/views/pages/components/ShareButton.vue";

const props = defineProps({
  itemId: {
    type: [Number, String],
    required: true,
  },
  initialLikeCount: {
    type: Number,
    default: 0
  },
  initialCollectedCount: {
    type: Number,
    default: 0
  },
  initialIsLiked: {
    type: [Boolean, Number],  // 接受 Boolean 或 Number
    default: 0,
    validator: (value) => [0, 1, true, false].includes(value)
  },
  initialIsCollected: {
    type: [Boolean, Number],
    default: 0,
    validator: (value) => [0, 1, true, false].includes(value)
  },
  shareUrl: {
    type: String,
    default: ''
  },
  shareTitle: {
    type: String,
    default: ''
  }
});

const emit = defineEmits(['like', 'collect', 'collect-success', 'share']);

const lStore = localStores();


// 使用计算属性确保响应式更新
const likeCount = ref(props.initialLikeCount);
const collectedCount = ref(props.initialCollectedCount);
const isLiked = ref(props.initialIsLiked);
const isCollected = ref(props.initialIsCollected);
const likeLoading = ref(false);
const collectLoading = ref(false);
const collectDialogVisible = ref(false);
const showCollectDialog = ref(false);

// 监听 props 变化
watch(() => props.initialLikeCount, (newVal) => {
  likeCount.value = newVal;
});

watch(() => props.initialCollectedCount, (newVal) => {
  collectedCount.value = newVal;
});

watch(() => props.initialIsLiked, (newVal) => {
  isLiked.value = newVal;
});

watch(() => props.initialIsCollected, (newVal) => {
  isCollected.value = newVal;
});

// 点赞处理
const handleLike = async () => {
  if (lStore.isTokenExpired) {
    ElMessage.warning('请先登录');
    return;
  }

  try {
    likeLoading.value = true;
    // 先更新本地状态，提供即时反馈
    const newLikeStatus = !isLiked.value;
    isLiked.value = newLikeStatus;
    likeCount.value += newLikeStatus ? 1 : -1;

    // 触发父组件事件
    await emit('like', {
      itemId: props.itemId,
      isLiked: newLikeStatus
    });
  } catch (error) {
    // 回滚状态
    isLiked.value = !isLiked.value;
    likeCount.value += isLiked.value ? 1 : -1;

    ElMessage.error(error.message || '点赞操作失败');
    console.error('点赞失败:', error);
  } finally {
    likeLoading.value = false;
  }
};

// 收藏处理
const handleCollect = async () => {
  if (lStore.isTokenExpired) {
    ElMessage.warning('请先登录');
    return;
  }

  try {
    collectLoading.value = true;
    if (isCollected.value) {
      // 先更新本地状态
      isCollected.value = false;
      collectedCount.value -= 1;

      await emit('collect', {
        itemId: props.itemId,
        action: 'uncollect'
      });
      ElMessage.success('已取消收藏');
    } else {
      showCollectDialog.value = true;
      collectDialogVisible.value = true;
    }
  } catch (error) {
    // 回滚状态
    isCollected.value = !isCollected.value;
    collectedCount.value += isCollected.value ? 1 : -1;

    ElMessage.error(error.message || '收藏操作失败');
    console.error('收藏失败:', error);
  } finally {
    collectLoading.value = false;
  }
};

// 收藏成功回调
const handleCollectSuccess = () => {
  isCollected.value = true;
  collectedCount.value += 1;
  emit('collect-success');
  ElMessage.success('收藏成功');
};

// 处理分享事件
const handleShare = (data) => {
  console.log('分享到:', data.platform);
  emit('share', data);
};
</script>

<style lang="scss" scoped>
.interaction-buttons {
  display: flex;
  gap: 10px;
  position: relative;
}

.interaction-button {
  display: flex;
  align-items: center;
  gap: 5px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  &.el-button--primary:hover {
    background-color: var(--el-color-primary-light-3);
    border-color: var(--el-color-primary-light-3);
  }

  &.el-button--warning:hover {
    background-color: var(--el-color-warning-light-3);
    border-color: var(--el-color-warning-light-3);
  }

  &:not(.el-button--primary):not(.el-button--warning):hover {
    color: var(--el-color-primary);
    border-color: var(--el-color-primary-light-5);
    background-color: var(--el-color-primary-light-9);
  }
}
</style>