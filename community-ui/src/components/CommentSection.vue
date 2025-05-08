<template>
  <div class="comment-section">
    <h3 class="comment-title">评论区 ({{ totalComments }})</h3>

    <!-- 主评论输入框 -->
    <div class="comment-form">
      <el-input
          type="textarea"
          v-model="newComment"
          :rows="3"
          placeholder="写下你的评论..."
          resize="none"
      />
      <div class="form-actions">
        <el-button type="primary" @click="submitComment" :loading="submitting">
          发表评论
        </el-button>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-if="loading" class="loading-comments">
        <el-icon class="is-loading">
          <Loading/>
        </el-icon>
        加载中...
      </div>

      <div v-else-if="comments.length === 0" class="empty-comments">
        暂无评论，快来发表你的看法吧~
      </div>

      <template v-else>
        <comment-item
            v-for="comment in comments"
            :key="comment.id"
            :comment="comment"
            :depth="-1"
            @reply="handleReply"
            @cancel-reply="cancelReply"
        />
      </template>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {fetchCommentsByPostId, submitCommentToPost} from '@/api/index.js'
import CommentItem from './CommentItem.vue'

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true,
    default: null
  }
})

const comments = ref([])
const newComment = ref('')
const loading = ref(false)
const submitting = ref(false)

// 计算总评论数（包括回复）
const totalComments = computed(() => {
  let count = comments.value.length
  comments.value.forEach(comment => {
    count += countReplies(comment)
  })
  return count
})

// 递归计算回复数量
const countReplies = (comment) => {
  if (!comment.voList || comment.voList.length === 0) return 0
  let count = comment.voList.length
  comment.voList.forEach(reply => {
    count += countReplies(reply)
  })
  return count
}

// 加载评论
const loadComments = async () => {
  if (!props.postId) return

  try {
    loading.value = true
    const res = await fetchCommentsByPostId(props.postId)
    comments.value = res.data || []
  } catch (e) {
    ElMessage.error('加载评论失败: ' + (e.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

// 提交主评论
const submitComment = async () => {
  const content = newComment.value.trim()
  if (!content) {
    ElMessage.warning('请输入评论内容')
    return
  }

  try {
    submitting.value = true
    const response = await submitCommentToPost(props.postId, {
      content,
      parentId: null
    })

    // 静态添加新评论
    comments.value.unshift(response.data)

    ElMessage.success('评论成功')
    newComment.value = ''
  } catch (e) {
    ElMessage.error('评论失败: ' + (e.message || '未知错误'))
  } finally {
    submitting.value = false
  }
}

// 处理回复
const handleReply = async (comment) => {
  try {
    submitting.value = true
    const response = await submitCommentToPost(props.postId, comment)

    // 成功提交后，找到对应的父评论并静态添加回复
    const newReply = response.data

    if (comment.parentId) {
      // 如果是回复评论
      const parentComment = findCommentById(comments.value, comment.parentId)
      if (parentComment) {
        if (!parentComment.voList) {
          parentComment.voList = []
        }
        parentComment.voList.unshift(newReply)
      }
    } else {
      // 如果是主评论
      comments.value.unshift(newReply)
    }

    ElMessage.success('评论成功')
  } catch (e) {
    ElMessage.error('评论失败: ' + (e.message || '未知错误'))
  } finally {
    submitting.value = false
  }
}

// 辅助函数：递归查找评论
const findCommentById = (commentList, id) => {
  for (const comment of commentList) {
    if (comment.commentId === id) {
      return comment
    }
    if (comment.voList && comment.voList.length > 0) {
      const found = findCommentById(comment.voList, id)
      if (found) return found
    }
  }
  return null
}

// 取消回复
const cancelReply = () => {
  // 可以在这里处理取消回复的逻辑
}

// 监听 postId 变化
watch(() => props.postId, (newVal) => {
  if (newVal) {
    loadComments()
  }
})

onMounted(() => {
  if (props.postId) {
    loadComments()
  }
})
</script>

<style scoped>
.comment-section {
  margin-top: 40px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.comment-title {
  margin-bottom: 20px;
  font-size: 18px;
  color: #333;
}

.comment-form {
  margin-bottom: 30px;
}

.form-actions {
  margin-top: 10px;
  text-align: right;
}

.loading-comments {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #999;
}

.empty-comments {
  padding: 20px;
  text-align: center;
  color: #999;
}

.comment-list {
  border-top: 1px solid #eee;
  padding-top: 20px;
}
</style>