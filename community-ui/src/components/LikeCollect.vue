<template>
  <div class="interaction-buttons">
    <!-- 点赞按钮 -->
    <el-button
        :icon="isLiked ? 'CircleCheckFilled' : 'CircleCheck'"
        :type="isLiked ? 'primary' : ''"
        @click="handleLike"
        :loading="likeLoading"
    >
      {{ likeCount }}
    </el-button>

    <!-- 收藏按钮 -->
    <el-button
        :icon="isCollected ? 'StarFilled' : 'Star'"
        :type="isCollected ? 'warning' : ''"
        @click="handleCollect"
        :loading="collectLoading"
    >
      {{ collectedCount }}
    </el-button>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import {localStore} from "@/stores/localStores.js";

const props = defineProps({
  itemId: {
    type: [Number, String],
    required: true
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
    type: Boolean,
    default: false
  },
  initialIsCollected: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['like', 'collect']);

const lStore = localStore();

// 状态
const likeCount = ref(props.initialLikeCount);
const collectedCount = ref(props.initialCollectedCount);
const isLiked = ref(props.initialIsLiked);
const isCollected = ref(props.initialIsCollected);
const likeLoading = ref(false);
const collectLoading = ref(false);

// 点赞处理
const handleLike = async () => {
  if (lStore.isTokenExpired) {
    ElMessage.warning('请先登录');
    return;
  }

  try {
    likeLoading.value = true;
    if (isLiked.value) {
      // 取消点赞
      await emit('like', {itemId: props.itemId, action: 'unlike'});
      likeCount.value--;
    } else {
      // 点赞
      await emit('like', {itemId: props.itemId, action: 'like'});
      likeCount.value++;
    }
    isLiked.value = !isLiked.value;
  } catch (error) {
    ElMessage.error(error.message || '操作失败');
  } finally {
    likeLoading.value = false;
  }
};

// 收藏处理
const handleCollect = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录');
    return;
  }

  try {
    collectLoading.value = true;
    if (isCollected.value) {
      // 取消收藏
      await emit('collect', {itemId: props.itemId, action: 'uncollect'});
      collectedCount.value--;
    } else {
      // 收藏
      await emit('collect', {itemId: props.itemId, action: 'collect'});
      collectedCount.value++;
    }
    isCollected.value = !isCollected.value;
  } catch (error) {
    ElMessage.error(error.message || '操作失败');
  } finally {
    collectLoading.value = false;
  }
};
</script>

<style scoped>
.interaction-buttons {
  display: flex;
  gap: 10px;
}

.el-button {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>