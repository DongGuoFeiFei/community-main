<!--<template>-->
<!--  <div class="chat-room">-->
<!--    <div class="chat-container">-->
<!--      &lt;!&ndash; 侧边栏 &ndash;&gt;-->
<!--      <ChatSidebar-->
<!--          :sessions="sessions"-->
<!--          :current-session="currentSession"-->
<!--          @select-session="handleSelectSession"-->
<!--          @create-session="handleCreateSession"-->
<!--      />-->

<!--      &lt;!&ndash; 主聊天区域 &ndash;&gt;-->
<!--      <div class="main-chat-area" v-if="currentSession">-->
<!--        &lt;!&ndash; 聊天头部 &ndash;&gt;-->
<!--        <ChatHeader-->
<!--            :session="currentSession"-->
<!--            @show-session-info="showSessionInfo"-->
<!--        />-->

<!--        &lt;!&ndash; 消息列表 &ndash;&gt;-->
<!--        <div class="message-container">-->
<!--          <MessageList-->
<!--              :messages="currentMessages"-->
<!--              :session="currentSession"-->
<!--              @load-more="loadMoreMessages"-->
<!--          />-->

<!--          &lt;!&ndash;          &lt;!&ndash; 输入状态指示器 &ndash;&gt;&ndash;&gt;-->
<!--          &lt;!&ndash;          <TypingIndicator&ndash;&gt;-->
<!--          &lt;!&ndash;              v-if="isTyping"&ndash;&gt;-->
<!--          &lt;!&ndash;              :typing-users="typingUsersList"&ndash;&gt;-->
<!--          &lt;!&ndash;          />&ndash;&gt;-->
<!--        </div>-->

<!--        &lt;!&ndash; 消息输入框 &ndash;&gt;-->
<!--        <ChatInput-->
<!--            :session="currentSession"-->
<!--            @send-message="sendMessage"-->
<!--            @typing-start="handleTypingStart"-->
<!--            @typing-stop="handleTypingStop"-->
<!--        />-->
<!--      </div>-->

<!--      &lt;!&ndash; 无会话时的提示 &ndash;&gt;-->
<!--      <div class="no-session" v-else>-->
<!--        <div class="no-session-content">-->
<!--          <div class="illustration">-->
<!--            <el-icon :size="80" color="var(&#45;&#45;el-color-primary)">-->
<!--              <ChatDotRound/>-->
<!--            </el-icon>-->
<!--          </div>-->
<!--          <h3>欢迎来到聊天室</h3>-->
<!--          <p>选择一个会话开始聊天，或者创建新的会话</p>-->
<!--        </div>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import {computed, onMounted, onUnmounted, ref, watch} from 'vue'-->
<!--import {ElMessage} from 'element-plus'-->
<!--import {ChatDotRound} from '@element-plus/icons-vue'-->

<!--// 组件导入-->
<!--import ChatSidebar from './components/ChatSidebar.vue'-->
<!--import ChatHeader from './components/ChatHeader.vue'-->
<!--import MessageList from './components/MessageList.vue'-->
<!--import ChatInput from './components/ChatInput.vue'-->

<!--// API导入-->
<!--import {createSession, getSessionMessages, getSessions} from '@/api/chat'-->

<!--// WebSocket Hook-->
<!--import {useChatWebSocket} from '@/utils/websocket'-->
<!--import {localStores} from "@/stores/localStores.js";-->

<!--// 状态管理-->
<!--const sessions = ref([])-->
<!--const currentSession = ref(null)-->
<!--const messagesMap = ref(new Map()) // 按会话ID存储消息-->

<!--const store = localStores()-->
<!--// WebSocket连接-->
<!--const {-->
<!--  connect,-->
<!--  disconnect,-->
<!--  isConnected,-->
<!--  subscribeToRoom,-->
<!--  unsubscribeFromRoom,-->
<!--  sendPrivateMessage,-->
<!--  getMessagesBySession,-->
<!--  error-->
<!--} = useChatWebSocket()-->

<!--// 计算当前会话的消息-->
<!--const currentMessages = computed(() => {-->
<!--  if (!currentSession.value) return []-->
<!--  return messagesMap.value.get(currentSession.value.id) || []-->
<!--})-->

<!--// 初始化-->
<!--onMounted(async () => {-->
<!--  try {-->
<!--    // 加载会话列表-->
<!--    await loadSessions()-->

<!--    // 连接WebSocket-->
<!--    connect()-->

<!--    // 监听错误-->
<!--    watch(error, (err) => {-->
<!--      if (err) ElMessage.error('WebSocket错误: ' + err)-->
<!--    })-->
<!--  } catch (error) {-->
<!--    ElMessage.error('初始化聊天室失败: ' + error.message)-->
<!--  }-->
<!--})-->

<!--// 清理-->
<!--onUnmounted(() => {-->
<!--  unsubscribeFromRoom(currentSession.value?.id)-->
<!--  disconnect()-->
<!--})-->

<!--// 加载会话列表-->
<!--const loadSessions = async () => {-->
<!--  try {-->
<!--    const response = await getSessions()-->
<!--    sessions.value = response.data-->
<!--    // 默认选择第一个会话-->
<!--    if (sessions.value.length > 0) {-->
<!--      handleSelectSession(sessions.value[0])-->
<!--    }-->
<!--  } catch (error) {-->
<!--    ElMessage.error('加载会话列表失败: ' + error.message)-->
<!--  }-->
<!--}-->

<!--// 选择会话-->
<!--const handleSelectSession = async (session) => {-->
<!--  // 取消订阅之前的会话-->
<!--  if (currentSession.value) {-->
<!--    unsubscribeFromRoom(currentSession.value.id)-->
<!--  }-->

<!--  currentSession.value = session-->
<!--  subscribeToRoom(session.id)-->

<!--  // 如果还没有加载过此会话的消息，则加载-->
<!--  if (!messagesMap.value.has(session.id)) {-->
<!--    try {-->
<!--      const response = await getSessionMessages(session.id)-->
<!--      messagesMap.value.set(session.id, response.data)-->
<!--    } catch (error) {-->
<!--      ElMessage.error('加载消息失败: ' + error.message)-->
<!--    }-->
<!--  }-->
<!--}-->

<!--// 创建新会话-->
<!--const handleCreateSession = async (userId) => {-->
<!--  try {-->
<!--    const response = await createSession(userId)-->
<!--    const newSession = response.data-->
<!--    sessions.value.push(newSession)-->
<!--    handleSelectSession(newSession)-->
<!--  } catch (error) {-->
<!--    ElMessage.error('创建会话失败: ' + error.message)-->
<!--  }-->
<!--}-->

<!--// 发送消息-->
<!--const sendMessage = (content) => {-->
<!--  if (!currentSession.value) return-->
<!--  try {-->
<!--    sendPrivateMessage(content, currentSession.value.id)-->
<!--  } catch (error) {-->
<!--    ElMessage.error('发送消息失败: ' + error.message)-->
<!--  }-->
<!--}-->

<!--// 加载更多消息-->
<!--const loadMoreMessages = async (lastMessageId) => {-->
<!--  if (!currentSession.value) return-->

<!--  try {-->
<!--    const response = await getSessionMessages(currentSession.value.id, lastMessageId)-->
<!--    const existingMessages = messagesMap.value.get(currentSession.value.id) || []-->
<!--    messagesMap.value.set(currentSession.value.id, [...response.data, ...existingMessages])-->
<!--  } catch (error) {-->
<!--    ElMessage.error('加载更多消息失败: ' + error.message)-->
<!--  }-->
<!--}-->

<!--// 输入状态处理-->
<!--const handleTypingStart = () => {-->
<!--  console.log("开始输入")-->
<!--}-->

<!--const handleTypingStop = () => {-->
<!--  console.log("停止输入")-->
<!--}-->

<!--// 显示会话信息-->
<!--const showSessionInfo = () => {-->
<!--  console.log('显示会话信息:', currentSession.value)-->
<!--}-->
<!--</script>-->


<!--<style lang="scss" scoped>-->
<!--.chat-room {-->
<!--  height: 100vh;-->
<!--  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: center;-->
<!--  padding: 20px;-->
<!--  box-sizing: border-box;-->

<!--  .chat-container {-->
<!--    display: flex;-->
<!--    height: 90vh;-->
<!--    width: 100%;-->
<!--    max-width: 1400px;-->
<!--    background: white;-->
<!--    border-radius: 16px;-->
<!--    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);-->
<!--    overflow: hidden;-->

<!--    .main-chat-area {-->
<!--      flex: 1;-->
<!--      display: flex;-->
<!--      flex-direction: column;-->
<!--      background: #f8f9fa;-->
<!--      position: relative;-->

<!--      .message-container {-->
<!--        flex: 1;-->
<!--        display: flex;-->
<!--        flex-direction: column;-->
<!--        justify-content: flex-end;-->
<!--        overflow: hidden;-->
<!--      }-->
<!--    }-->

<!--    .no-session {-->
<!--      flex: 1;-->
<!--      display: flex;-->
<!--      align-items: center;-->
<!--      justify-content: center;-->
<!--      background: #f8f9fa;-->

<!--      .no-session-content {-->
<!--        text-align: center;-->
<!--        color: #6c757d;-->

<!--        .illustration {-->
<!--          margin-bottom: 20px;-->
<!--          opacity: 0.7;-->
<!--        }-->

<!--        h3 {-->
<!--          font-size: 24px;-->
<!--          font-weight: 600;-->
<!--          margin-bottom: 12px;-->
<!--          color: #343a40;-->
<!--        }-->

<!--        p {-->
<!--          font-size: 16px;-->
<!--          margin: 0;-->
<!--        }-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--}-->

<!--// 响应式设计-->
<!--@media (max-width: 768px) {-->
<!--  .chat-room {-->
<!--    padding: 0;-->

<!--    .chat-container {-->
<!--      height: 100vh;-->
<!--      border-radius: 0;-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</style>-->
<template/>
