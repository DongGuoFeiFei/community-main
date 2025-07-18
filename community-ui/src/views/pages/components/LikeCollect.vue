<template>
  <div class="interaction-buttons">
    <!-- 点赞按钮 -->
    <el-button
        :icon="isLiked===0 ? 'CircleCheckFilled' : 'CircleCheck'"
        :type="isLiked ? 'primary' : ''"
        @click="handleLike"
        :loading="likeLoading"
    >
      {{ likeCount }}
    </el-button>

    <!-- 收藏按钮 -->
    <el-button
        :icon="isCollected===0 ? 'StarFilled' : 'Star'"
        :type="isCollected ? 'warning' : ''"
        @click="handleCollect"
        :loading="collectLoading"
    >
      {{ collectedCount }}
    </el-button>

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
  }
});

const emit = defineEmits(['like', 'collect', 'collect-success']);

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
</script>

<style scoped>
.interaction-buttons {
  display: flex;
  gap: 10px;
  position: relative;
}

.el-button {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>