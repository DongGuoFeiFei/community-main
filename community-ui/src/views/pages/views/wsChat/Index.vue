<template>
  <div class="chat-room">
    <div class="chat-container">
      <!-- 侧边栏 -->
      <ChatSidebar
          :sessions="sessions"
          :current-session="currentSession"
          @select-session="handleSelectSession"
          @create-session="handleCreateSession"
      />

      <!-- 主聊天区域 -->
      <div class="main-chat-area" v-if="currentSession">
        <!-- 聊天头部 -->
        <ChatHeader
            :session="currentSession"
            @show-session-info="showSessionInfo"
        />

        <!-- 消息列表 -->
        <div class="message-container">
          <MessageList
              :messages="currentMessages"
              :session="currentSession"
              @load-more="loadMoreMessages"
          />

          <!--          &lt;!&ndash; 输入状态指示器 &ndash;&gt;-->
          <!--          <TypingIndicator-->
          <!--              v-if="isTyping"-->
          <!--              :typing-users="typingUsersList"-->
          <!--          />-->
        </div>

        <!-- 消息输入框 -->
        <ChatInput
            :session="currentSession"
            @send-message="sendMessage"
            @typing-start="handleTypingStart"
            @typing-stop="handleTypingStop"
        />
      </div>

      <!-- 无会话时的提示 -->
      <div class="no-session" v-else>
        <div class="no-session-content">
          <div class="illustration">
            <el-icon :size="80" color="var(--el-color-primary)">
              <ChatDotRound/>
            </el-icon>
          </div>
          <h3>欢迎来到聊天室</h3>
          <p>选择一个会话开始聊天，或者创建新的会话</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, onUnmounted, ref, watch} from 'vue'
import {ElMessage} from 'element-plus'
import {ChatDotRound} from '@element-plus/icons-vue'

// 组件导入
import ChatSidebar from './components/ChatSidebar.vue'
import ChatHeader from './components/ChatHeader.vue'
import MessageList from './components/MessageList.vue'
import ChatInput from './components/ChatInput.vue'

// API导入
import {createSession, getSessionMessages, getSessions} from '@/api/chat'

// WebSocket Hook
import {useChatWebSocket} from '@/utils/websocket'

// 状态管理
const sessions = ref([])
const currentSession = ref(null)
const messagesMap = ref(new Map()) // 按会话ID存储消息

// WebSocket连接
const {
  connect,
  disconnect,
  isConnected,
  sendPrivateMessage,
  privateMessages,
  error
} = useChatWebSocket()

// 计算当前会话的消息
const currentMessages = computed(() => {
  if (!currentSession.value) return []
  return messagesMap.value.get(currentSession.value.id) || []
})

// 初始化
onMounted(async () => {
  try {
    // 加载会话列表
    await loadSessions()

    // 连接WebSocket
    connect()

    // 监听私聊消息
    watch(privateMessages, (newMessages) => {
      if (newMessages.length === 0) return

      newMessages.forEach(message => {
        const sessionId = message.sessionId
        if (sessionId) {
          const existingMessages = messagesMap.value.get(sessionId) || []
          messagesMap.value.set(sessionId, [...existingMessages, message])
        } else {
          console.warn('收到无sessionId的私聊消息:', message)
        }
      })
    })

    // 监听错误
    watch(error, (err) => {
      if (err) {
        ElMessage.error('WebSocket错误: ' + err)
      }
    })
  } catch (error) {
    ElMessage.error('初始化聊天室失败: ' + error.message)
  }
})

// 清理
onUnmounted(() => {
  disconnect()
})

// 加载会话列表
const loadSessions = async () => {
  try {
    const response = await getSessions()
    sessions.value = response.data
  } catch (error) {
    ElMessage.error('加载会话列表失败: ' + error.message)
  }
}

// 加载更多消息
const loadMoreMessages = async (lastMessageId) => {
  if (!currentSession.value) return

  try {
    const response = await getSessionMessages(currentSession.value.id, lastMessageId)
    const existingMessages = messagesMap.value.get(currentSession.value.id) || []
    messagesMap.value.set(currentSession.value.id, [...response.data, ...existingMessages])
  } catch (error) {
    ElMessage.error('加载更多消息失败: ' + error.message)
  }
}

// 选择会话
const handleSelectSession = async (session) => {
  currentSession.value = session

  // 如果还没有加载过此会话的消息，则加载
  if (!messagesMap.value.has(session.id)) {
    try {
      const response = await getSessionMessages(session.id)
      messagesMap.value.set(session.id, response.data)
    } catch (error) {
      ElMessage.error('加载消息失败: ' + error.message)
    }
  }
}

// 创建新会话
const handleCreateSession = async (userId) => {
  try {
    const response = await createSession(userId)
    sessions.value.push(response.data)
    currentSession.value = response.data
  } catch (error) {
    ElMessage.error('创建会话失败: ' + error.message)
  }
}

// 发送消息
const sendMessage = (content) => {
  if (!currentSession.value) return

  try {
    console.log('发送消息:', content, '到会话:', currentSession.value.id)
    sendPrivateMessage(content, currentSession.value.id)
  } catch (error) {
    ElMessage.error('发送消息失败: ' + error.message)
  }
}

const handleTypingStop = () => {
  console.log("停止输入")
}
const handleTypingStart = () => {
  console.log("开始输入")
}

// 显示会话信息
const showSessionInfo = () => {
  console.log('显示会话信息:', currentSession.value)
}
</script>

<style lang="scss" scoped>
.chat-room {
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  box-sizing: border-box;

  .chat-container {
    display: flex;
    height: 90vh;
    width: 100%;
    max-width: 1400px;
    background: white;
    border-radius: 16px;
    box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    .main-chat-area {
      flex: 1;
      display: flex;
      flex-direction: column;
      background: #f8f9fa;
      position: relative;

      .message-container {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        overflow: hidden;
      }
    }

    .no-session {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f8f9fa;

      .no-session-content {
        text-align: center;
        color: #6c757d;

        .illustration {
          margin-bottom: 20px;
          opacity: 0.7;
        }

        h3 {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 12px;
          color: #343a40;
        }

        p {
          font-size: 16px;
          margin: 0;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .chat-room {
    padding: 0;

    .chat-container {
      height: 100vh;
      border-radius: 0;
    }
  }
}
</style>
