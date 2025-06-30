<template>
  <div class="comment-section">
    <h3 class="comment-title">评论区 ({{ totalComments }})</h3>

    <!-- 评论输入框 -->
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
        <div
            v-for="comment in comments"
            :key="comment.commentId"
            class="main-comment"
        >
          <!-- 主评论 -->
          <div class="comment-main">
            <div class="comment-header">
              <el-avatar :size="32" :src="getFullUrl(comment.accessUrl)"/>
              <span class="comment-author">{{ comment.nickname }}</span>
              <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>

              <!-- 展开/收起按钮 -->
              <el-button
                  v-if="comment.voList && comment.voList.length > 0"
                  link
                  type="primary"
                  class="toggle-replies"
                  @click="toggleReplies(comment.commentId)"
              >
                {{ expandedComments[comment.commentId] ? '收起回复' : `展开回复(${countReplies(comment)})` }}
              </el-button>
            </div>

            <div class="comment-content">{{ comment.content }}</div>

            <div class="comment-actions">
              <el-button link type="primary" @click="showReplyForm(comment.commentId)">
                回复
              </el-button>
            </div>

            <!-- 回复表单 -->
            <div v-if="activeReplyForm === comment.commentId" class="reply-form">
              <el-input
                  type="textarea"
                  v-model="replyContent"
                  :rows="3"
                  placeholder="写下你的回复..."
                  resize="none"
              />
              <div class="form-actions">
                <el-button size="small" @click="cancelReply">取消</el-button>
                <el-button
                    type="primary"
                    size="small"
                    @click="submitReply(comment.commentId,comment.commentId)"
                    :loading="replying"
                >
                  发送
                </el-button>
              </div>
            </div>
          </div>

          <!-- 回复列表（默认收起） -->
          <div
              v-if="comment.voList && comment.voList.length > 0 && expandedComments[comment.commentId]"
              class="reply-list"
          >
            <div
                v-for="reply in flattenReplies(comment.voList)"
                :key="reply.commentId"
                class="reply-item"
            >
              <div class="reply-header">
                <el-avatar :size="28" :src="getFullUrl(reply.accessUrl)"/>
                <span class="reply-author">
                  {{ reply.nickname || '匿名用户' }}
                  <span v-if="reply.repliedNickname" class="reply-to">
                    回复 {{ reply.repliedNickname }}
                  </span>
                </span>
                <span class="reply-time">{{ formatDate(reply.createdAt) }}</span>
              </div>
              <div class="reply-content">{{ reply.content }}</div>

              <!-- 子评论的回复按钮 -->
              <div class="reply-actions">
                <el-button
                    link
                    type="primary"
                    size="small"
                    @click="showSubReplyForm(reply.commentId)"
                >
                  回复
                </el-button>
              </div>

              <!-- 子评论的回复表单 -->
              <div v-if="activeSubReplyForm === reply.commentId" class="sub-reply-form">
                <el-input
                    type="textarea"
                    v-model="subReplyContent"
                    :rows="2"
                    placeholder="写下你的回复..."
                    resize="none"
                />
                <div class="form-actions">
                  <el-button size="small" @click="cancelSubReply">取消</el-button>
                  <el-button
                      type="primary"
                      size="small"
                      @click="submitSubReply(reply.commentId, reply.firstId)"
                      :loading="subReplying"
                  >
                    发送
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<!--todo 添加一个评论删除按钮 ， 将回复框设置为弹窗-->

<script setup>
import {computed, onMounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {sessionStores} from "@/stores/sessionStores.js"
import {fetchCommentsByPostId, submitCommentToPost} from '../../../../../../community-admin/src/api/index.js'

const sStore = sessionStores()

const props = defineProps({
  postId: {
    type: [String, Number],
    required: true,
    default: null
  }
})

// 数据状态
const comments = ref([])
const newComment = ref('')
const loading = ref(false)
const submitting = ref(false)
const replying = ref(false)
const subReplying = ref(false)
const replyContent = ref('')
const subReplyContent = ref('')
const activeReplyForm = ref(null)
const activeSubReplyForm = ref(null)

// 展开/收起状态 { commentId: boolean }
const expandedComments = ref({})

// 计算总评论数（包括回复）
const totalComments = computed(() => {
  let count = comments.value.length
  comments.value.forEach(comment => {
    count += countAllReplies(comment)
  })
  return count
})

// 计算单个评论的回复数
const countReplies = (comment) => {
  return countAllReplies(comment)
}

// 初始化展开状态
const initExpandedState = () => {
  comments.value.forEach(comment => {
    expandedComments.value[comment.commentId] = false
  })
}

// 加载评论
const loadComments = async () => {
  if (!props.postId) return

  try {
    loading.value = true
    const res = await fetchCommentsByPostId(props.postId)
    comments.value = res.data || []
    initExpandedState()
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
      parentId: null,
      firstId: null,
    })

    comments.value.unshift(response.data)
    expandedComments.value[response.data.commentId] = false
    ElMessage.success('评论成功')
    newComment.value = ''
  } catch (e) {
    ElMessage.error('评论失败: ' + (e.message || '未知错误'))
  } finally {
    submitting.value = false
  }
}

// 显示回复表单
const showReplyForm = (commentId) => {
  activeReplyForm.value = commentId
  replyContent.value = ''
}

// 取消回复
const cancelReply = () => {
  activeReplyForm.value = null
  replyContent.value = ''
}

// 提交回复
const submitReply = async (parentId, firstId) => {
  const content = replyContent.value.trim()
  if (!content) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    replying.value = true
    const response = await submitCommentToPost(props.postId, {
      content,
      parentId,
      firstId
    })

    // 找到父评论并添加回复
    const parentComment = findComment(comments.value, parentId)
    if (parentComment) {
      if (!parentComment.voList) {
        parentComment.voList = []
      }
      parentComment.voList.unshift(response.data)
      // 自动展开回复列表
      expandedComments.value[parentId] = true
    }

    ElMessage.success('回复成功')
    cancelReply()
  } catch (e) {
    ElMessage.error('回复失败: ' + (e.message || '未知错误'))
  } finally {
    replying.value = false
  }
}

// 显示子评论回复表单
const showSubReplyForm = (commentId) => {
  activeSubReplyForm.value = commentId
  subReplyContent.value = ''
}

// 取消子评论回复
const cancelSubReply = () => {
  activeSubReplyForm.value = null
  subReplyContent.value = ''
}

// 提交子评论回复
const submitSubReply = async (parentId, firstId) => {
  const content = subReplyContent.value.trim()
  if (!content) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    subReplying.value = true
    const response = await submitCommentToPost(props.postId, {
      content,
      parentId,
      firstId
    })

    // 找到父评论并添加回复
    const parentComment = findComment(comments.value, parentId)
    if (parentComment) {
      if (!parentComment.voList) {
        parentComment.voList = []
      }
      parentComment.voList.unshift(response.data)
    }

    ElMessage.success('回复成功')
    cancelSubReply()
  } catch (e) {
    ElMessage.error('回复失败: ' + (e.message || '未知错误'))
  } finally {
    subReplying.value = false
  }
}

// 切换展开/收起状态
const toggleReplies = (commentId) => {
  expandedComments.value[commentId] = !expandedComments.value[commentId]
}

// 辅助方法：获取完整URL
const getFullUrl = (path) => {
  return path ? `${sStore.baseURL}${path}` : ''
}

// 辅助方法：格式化日期
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString()
}

// 辅助方法：平铺回复列表
const flattenReplies = (replies) => {
  const result = []
  replies.forEach(reply => {
    result.push(reply)
    if (reply.voList && reply.voList.length > 0) {
      result.push(...flattenReplies(reply.voList))
    }
  })
  return result
}

// 辅助方法：计算所有回复数量（递归）
const countAllReplies = (comment) => {
  if (!comment.voList || comment.voList.length === 0) return 0
  let count = comment.voList.length
  comment.voList.forEach(reply => {
    count += countAllReplies(reply)
  })
  return count
}

// 辅助方法：查找评论
const findComment = (commentList, commentId) => {
  for (const comment of commentList) {
    if (comment.commentId === commentId) {
      return comment
    }
    if (comment.voList && comment.voList.length > 0) {
      const found = findComment(comment.voList, commentId)
      if (found) return found
    }
  }
  return null
}

onMounted(() => {
  loadComments();
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

.main-comment {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-main {
  margin-bottom: 10px;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  position: relative;
  padding-right: 100px;
}

.comment-author {
  font-weight: bold;
  margin: 0 10px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.toggle-replies {
  position: absolute;
  right: 0;
  font-size: 12px;
}

.comment-content {
  margin-bottom: 8px;
  line-height: 1.6;
  color: #333;
}

.comment-actions {
  margin-top: 5px;
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

.reply-list {
  margin-top: 12px;
  padding-left: 40px;
  border-left: 2px solid #eee;
}

.reply-item {
  padding: 10px 0;
  border-bottom: 1px dashed #f0f0f0;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-header {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
  font-size: 14px;
}

.reply-author {
  margin-left: 8px;
}

.reply-to {
  color: #666;
  margin-left: 5px;
  font-size: 12px;
}

.reply-time {
  margin-left: auto;
  color: #999;
  font-size: 12px;
}

.reply-content {
  margin-left: 36px;
  color: #333;
  line-height: 1.5;
}

.reply-actions {
  margin-left: 36px;
  margin-top: 5px;
}

.reply-actions .el-button {
  padding: 0;
  height: auto;
  font-size: 12px;
}

.sub-reply-form {
  margin-top: 10px;
  margin-left: 36px;
  padding: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style>