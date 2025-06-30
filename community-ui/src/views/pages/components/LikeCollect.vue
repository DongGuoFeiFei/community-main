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
  </div>
</template>

<script setup>
import {ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {localStores} from "@/stores/localStores.js";

// 获取父组件数据
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

// 向父组件传递数据
const emit = defineEmits(['like', 'collect']);

const lStore = localStores();

// 状态
const likeCount = ref(props.initialLikeCount);
const collectedCount = ref(props.initialCollectedCount);
const isLiked = ref(props.initialIsLiked);
const isCollected = ref(props.initialIsCollected);
const likeLoading = ref(false);
const collectLoading = ref(false);
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
    if (isLiked.value) {
      // 取消点赞
      await emit('like', {itemId: props.itemId});
    } else {
      // 点赞
      await emit('like', {itemId: props.itemId});
    }
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
    } else {
      // 收藏
      await emit('collect', {itemId: props.itemId, action: 'collect'});
    }
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