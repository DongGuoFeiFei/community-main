<template>
  <div class="interaction-buttons">
    <!-- 点赞按钮 -->
    <el-button
        :icon="isLiked===0 ? 'CircleCheckFilled' : 'CircleCheck'"
        :type="isLiked ? 'primary' : ''"
        @click="handleLike"
        :loading="likeLoading"
        class="interaction-button"
    >
      {{ likeCount }}
    </el-button>

    <!-- 收藏按钮 -->
    <el-button
        :icon="isCollected===0 ? 'StarFilled' : 'Star'"
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
import {ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {localStores} from "@/stores/localStores.js";
import CollectDialog from "@/views/pages/components/CollectDialog.vue";
import ShareButton from "@/views/pages/components/ShareButton.vue";

const props = defineProps({
  itemId: {
    type: [Number, String],
    required: true,
    default: 0
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
    type: Number,
    default: 0
  },
  initialIsCollected: {
    type: Number,
    default: 0
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

// 状态
const likeCount = ref(props.initialLikeCount);
const collectedCount = ref(props.initialCollectedCount);
const isLiked = ref(props.initialIsLiked);
const isCollected = ref(props.initialIsCollected);
const likeLoading = ref(false);
const collectLoading = ref(false);
const collectDialogVisible = ref(false);
const showCollectDialog = ref(false);

// 监听 props 变化并更新本地状态
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
    await emit('like', {itemId: props.itemId});
  } catch (error) {
    ElMessage.error(error.message || '操作失败');
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
      // 取消收藏
      await emit('collect', {itemId: props.itemId, action: 'uncollect'});
      collectedCount.value--;
      isCollected.value = 0;
      ElMessage.success('已取消收藏');
    } else {
      // 显示收藏弹窗
      showCollectDialog.value = true;
      collectDialogVisible.value = true;
    }
  } catch (error) {
    ElMessage.error(error.message || '操作失败');
  } finally {
    collectLoading.value = false;
  }
};

// 收藏成功回调
const handleCollectSuccess = () => {
  collectedCount.value++;
  isCollected.value = 1;
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