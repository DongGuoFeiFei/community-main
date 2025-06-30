<template>
  <el-popover
      placement="bottom"
      width="300"
      trigger="click"
  >
    <template #reference>
      <el-button icon="el-icon-share" type="text">转发</el-button>
    </template>
    <div class="share-popover">
      <el-input
          v-model="shareContent"
          type="textarea"
          :rows="3"
          placeholder="添加转发评论"
      />
      <div class="share-actions">
        <el-button type="primary" size="small" @click="handleShare">确认转发</el-button>
      </div>
    </div>
  </el-popover>
</template>

<script setup>
import { ref } from 'vue';
import { sharePost } from '@/api/article.js';
import {ElMessage} from "element-plus";

const props = defineProps({
  postId: {
    type: Number,
    required: true
  }
});

const shareContent = ref('');

const handleShare = async () => {
  try {
    await sharePost({
      postId: props.postId,
      content: shareContent.value
    });
    ElMessage.success('转发成功');
    shareContent.value = '';
  } catch (error) {
    console.error('转发失败:', error);
    ElMessage.error('转发失败');
  }
};
</script>

<style scoped>
.share-popover {
  padding: 10px;
}
.share-actions {
  margin-top: 10px;
  text-align: right;
}
</style>