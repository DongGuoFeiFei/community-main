<template>
  <div class="comment-item" :class="{ 'has-replies': hasReplies }">
    <div class="comment-main">
      <div class="comment-header">
        <!--  todo 添加后台信息，添加用户头像、id，设置评论的折叠度为一，类似于B站的评论区样式   -->
        <el-avatar :size="32" :src="sStore.baseURL+comment.accessUrl"/>
        <span class="comment-author">用户 {{ comment.nickname }}</span>
        <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
      </div>
      <div class="comment-content">{{ comment.content }}</div>
      <div class="comment-actions">
        <el-button link type="primary" @click="toggleReply">
          {{ showReplyForm ? '取消回复' : '回复' }}
        </el-button>
        <el-button
            v-if="hasReplies"
            link
            type="primary"
            @click="toggleReplies"
        >
          {{ showReplies ? '收起回复' : `展开回复 (${replyCount})` }}
        </el-button>
      </div>

      <!-- 回复表单 -->
      <div v-if="showReplyForm" class="reply-form">
        <el-input
            type="textarea"
            v-model="replyContent"
            :rows="3"
            placeholder="写下你的回复..."
            resize="none"
        />
        <div class="form-actions">
          <el-button size="small" @click="cancelReply">取消</el-button>
          <el-button type="primary" size="small" @click="submitReply" :loading="replying">
            回复
          </el-button>
        </div>
      </div>
    </div>

    <!-- 回复列表 - 修改了这里的逻辑 -->
    <div v-if="hasReplies && showReplies" class="comment-replies">
      <comment-item
          v-for="reply in comment.voList"
          :key="reply.id"
          :comment="reply"
          :depth="depth + 1"
          @reply="handleChildReply"
          @cancel-reply="cancelChildReply"
      />
    </div>
  </div>
</template>

<script setup>
import {computed, reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {sessionStore} from "@/stores/sessionStores.js";

const sStore = sessionStore()

// 组件通信
const props = defineProps({
  comment: {
    type: Object,
    required: true
  },
  depth: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['reply', 'cancel-reply'])

const showReplies = ref(false) // 默认不展开任何回复
const showReplyForm = ref(false)
const replyContent = ref('')
const replying = ref(false)

const hasReplies = computed(() => {
  return props.comment.voList && props.comment.voList.length > 0
})

// 只计算直接子回复的数量
const replyCount = computed(() => {
  return hasReplies.value ? props.comment.voList.length : 0
})

const formatDate = (time) => {
  return new Date(time).toLocaleString()
}

const toggleReplies = () => {
  showReplies.value = !showReplies.value
}

const toggleReply = () => {
  showReplyForm.value = !showReplyForm.value
}

// 子评论回复消息
const replyInfo = reactive({
  content: "",
  parentId: null
})

const submitReply = () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  replyInfo.content = replyContent.value
  replyInfo.parentId = props.comment.commentId
  emit('reply', replyInfo)

  replyContent.value = ''
  showReplyForm.value = false
}

const cancelReply = () => {
  replyContent.value = ''
  showReplyForm.value = false
  emit('cancel-reply')
}

const handleChildReply = (data) => {
  emit('reply', data)
}

const cancelChildReply = () => {
  emit('cancel-reply')
}
</script>

<style scoped>
.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-main {
  margin-bottom: 10px;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  font-size: 14px;
}

.comment-author {
  font-weight: bold;
  margin-right: 10px;
  color: #333;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-content {
  margin-bottom: 8px;
  line-height: 1.6;
  color: #333;
}

.comment-actions {
  margin-top: 5px;
  display: flex;
  gap: 10px;
}

.comment-actions .el-button {
  padding: 0;
  height: auto;
}

.reply-form {
  margin-top: 15px;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.form-actions {
  margin-top: 10px;
  text-align: right;
}

.comment-replies {
  margin-left: 30px;
  padding-left: 15px;
  border-left: 2px solid #eee;
}

.has-replies {
  padding-bottom: 0;
}
</style>