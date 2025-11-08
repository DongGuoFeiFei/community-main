<template>
  <div
      class="share-container"
      @mouseenter="showPanel = true"
      @mouseleave="showPanel = false"
  >
    <el-popover
        placement="bottom"
        :width="200"
        trigger="hover"
        v-model:visible="showPanel"
        popper-class="share-popover"
        :show-arrow="false"
    >
      <template #reference>
        <el-button
            icon="Share"
            :loading="loading"
            class="share-button"
        >
          分享
        </el-button>
      </template>
      <div class="share-panel">
        <div class="share-option" @click="shareToWechat">
          <el-icon><Share /></el-icon>
          <span>微信</span>
        </div>
        <div class="share-option" @click="shareToQQ">
          <el-icon><Share /></el-icon>
          <span>QQ</span>
        </div>
        <div class="share-option" @click="shareToWeibo">
          <el-icon><Share /></el-icon>
          <span>微博</span>
        </div>
        <div class="share-option" @click="copyLink">
          <el-icon><DocumentCopy /></el-icon>
          <span>复制链接</span>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { Share, DocumentCopy } from '@element-plus/icons-vue';

const props = defineProps({
  itemId: {
    type: [Number, String],
    required: true
  },
  shareUrl: {
    type: String,
    default: ''
  },
  shareTitle: {
    type: String,
    default: ''
  },
  shareDesc: {
    type: String,
    default: ''
  },
  shareImage: {
    type: String,
    default: ''
  }
});

console.log(props)

const emit = defineEmits(['share']);

const loading = ref(false);
const showPanel = ref(false);

// 分享到微信
const shareToWechat = () => {
  loading.value = true;
  try {
    emit('share', {
      platform: 'wechat',
      itemId: props.itemId,
      url: props.shareUrl,
      title: props.shareTitle,
      desc: props.shareDesc,
      imgUrl: props.shareImage
    });
    ElMessage.success('已生成微信分享链接');
    showPanel.value = false;
  } catch (error) {
    ElMessage.error('微信分享失败');
  } finally {
    loading.value = false;
  }
};

// 分享到QQ
const shareToQQ = () => {
  loading.value = true;
  try {
    emit('share', {
      platform: 'qq',
      itemId: props.itemId,
      url: props.shareUrl,
      title: props.shareTitle,
      desc: props.shareDesc,
      imgUrl: props.shareImage
    });
    ElMessage.success('已生成QQ分享链接');
    showPanel.value = false;
  } catch (error) {
    ElMessage.error('QQ分享失败');
  } finally {
    loading.value = false;
  }
};

// 分享到微博
const shareToWeibo = () => {
  loading.value = true;
  try {
    emit('share', {
      platform: 'weibo',
      itemId: props.itemId,
      url: props.shareUrl,
      title: props.shareTitle,
      desc: props.shareDesc,
      imgUrl: props.shareImage
    });
    ElMessage.success('已生成微博分享链接');
    showPanel.value = false;
  } catch (error) {
    ElMessage.error('微博分享失败');
  } finally {
    loading.value = false;
  }
};

// 复制链接
const copyLink = () => {
  const url = props.shareUrl || window.location.href;
  console.log(url)
  navigator.clipboard.writeText(url)
      .then(() => {
        ElMessage.success('链接已复制到剪贴板');
        showPanel.value = false;
      })
      .catch(() => {
        ElMessage.error('复制失败，请手动复制');
      });
};
</script>

<style lang="scss" scoped>
.share-container {
  display: inline-block;
  margin-left: 12px;
}

.share-button {
  transition: all 0.3s ease;

  &:hover {
    color: var(--el-color-primary);
    border-color: var(--el-color-primary-light-5);
    background-color: var(--el-color-primary-light-9);
  }
}

.share-panel {
  padding: 8px 0;

  .share-option {
    display: flex;
    align-items: center;
    padding: 8px 16px;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background-color: var(--el-fill-color-light);
    }

    .el-icon {
      margin-right: 8px;
      font-size: 16px;
    }

    span {
      font-size: 14px;
    }
  }
}
.share-popover {
  padding: 0 !important;
  border-radius: 8px !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1) !important;
  border: none !important;

  .el-popper__arrow {
    display: none !important;
  }
}
</style>