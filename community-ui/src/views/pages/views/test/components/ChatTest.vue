<template>
  <div class="chat-room">
    <el-card class="chat-container">
      <template #header>
        <div class="chat-header">
          <span>聊天室 ({{ onlineUsers.length }}人在线)</span>
          <div class="header-actions">
            <el-button
                v-if="!connected"
                type="primary"
                @click="connect"
                :loading="connecting"
            >
              加入聊天室
            </el-button>
            <el-button
                v-else
                type="danger"
                @click="disconnect"
            >
              退出聊天室
            </el-button>
          </div>
        </div>
      </template>

      <div class="chat-content">
        <!-- 用户列表 -->
        <div class="user-list">
          <h3>在线用户</h3>
          <div class="user-item" v-for="user in onlineUsers" :key="user">
            <el-tag
                :type="user === currentUserId ? 'primary' : 'info'"
                size="small"
            >
              {{ user }}
            </el-tag>
          </div>
        </div>

        <!-- 聊天区域 -->
        <div class="message-area">
          <div class="messages" ref="messagesContainer">
            <div
                v-for="(message, index) in messages"
                :key="index"
                class="message"
                :class="{
                'system': message.type === 'system',
                'own': message.senderId === currentUserId && message.type === 'chat',
                'private': message.type === 'private',
                'other': message.senderId !== currentUserId && message.type === 'chat'
              }"
            >
              <div class="message-header">
                <span class="sender" v-if="message.type === 'chat' || message.type === 'private'">
                  {{ message.senderId }}{{ message.type === 'private' ? ' (私聊)' : '' }}
                </span>
                <span class="timestamp">{{ formatTime(message.timestamp) }}</span>
              </div>
              <div class="message-content">{{ message.content }}</div>
            </div>
          </div>

          <div class="input-area" v-if="connected">
            <div class="message-type">
              <el-radio-group v-model="messageType" size="small">
                <el-radio-button label="public">群聊</el-radio-button>
                <el-radio-button label="private">私聊</el-radio-button>
              </el-radio-group>

              <el-select
                  v-if="messageType === 'private'"
                  v-model="targetUser"
                  placeholder="选择用户"
                  size="small"
                  style="margin-left: 10px; width: 120px;"
              >
                <el-option
                    v-for="user in onlineUsers.filter(u => u !== currentUserId)"
                    :key="user"
                    :label="user"
                    :value="user"
                />
              </el-select>
            </div>

            <div class="input-box">
              <el-input
                  v-model="inputMessage"
                  placeholder="输入消息..."
                  @keyup.enter="sendMessage"
              />
              <el-button
                  type="primary"
                  @click="sendMessage"
                  :disabled="!inputMessage || (messageType === 'private' && !targetUser)"
              >
                发送
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {computed, nextTick, onUnmounted, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {localStores} from '@/stores/localStores'

const localStore = localStores()
const currentUserId = computed(() => localStore.userInfo.userInfo.userId || '匿名用户')

// WebSocket状态
const connected = ref(false)
const connecting = ref(false)
const socket = ref(null)

// 聊天数据
const onlineUsers = ref([])
const messages = ref([])
const inputMessage = ref('')
const messageType = ref('public')
const targetUser = ref('')
const messagesContainer = ref(null)

// 连接WebSocket
const connect = async () => {
  if (connecting.value) return

  connecting.value = true

  try {
    const BASIC_URL = "http://127.0.0.1:8081"
    const wsUrl = `ws://${BASIC_URL.split('//')[1]}/api/websocket/${currentUserId.value}`

    socket.value = new WebSocket(wsUrl)

    socket.value.onopen = () => {
      connected.value = true
      connecting.value = false
      ElMessage.success('已加入聊天室')
    }

    socket.value.onmessage = (event) => {
      handleMessage(event.data)
    }

    socket.value.onerror = (error) => {
      connecting.value = false
      ElMessage.error('连接失败')
      console.error('WebSocket错误:', error)
    }

    socket.value.onclose = () => {
      connected.value = false
      ElMessage.info('已退出聊天室')
    }
  } catch (error) {
    connecting.value = false
    ElMessage.error(`连接失败: ${error.message}`)
  }
}

// 断开连接
const disconnect = () => {
  if (socket.value) {
    socket.value.close()
    socket.value = null
  }
}

// 处理收到的消息
const handleMessage = (data) => {
  try {
    const message = JSON.parse(data)

    switch (message.type) {
      case 'chat':
      case 'private':
      case 'system':
        messages.value.push(message)
        scrollToBottom()
        break

      case 'users':
        onlineUsers.value = message.users || []
        break

      default:
        console.log('未知消息类型:', message)
    }
  } catch (error) {
    // 处理非JSON消息
    messages.value.push({
      type: 'system',
      content: data,
      timestamp: Date.now()
    })
    scrollToBottom()
  }
}

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim()) return
  if (!socket.value || socket.value.readyState !== WebSocket.OPEN) {
    ElMessage.warning('未连接到聊天室')
    return
  }

  const messageData = {
    type: 'chat',
    content: inputMessage.value.trim()
  }

  if (messageType.value === 'private' && targetUser.value) {
    messageData.targetUserId = targetUser.value
  }

  socket.value.send(JSON.stringify(messageData))
  inputMessage.value = ''
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 格式化时间
const formatTime = (timestamp) => {
  return new Date(timestamp).toLocaleTimeString()
}

// 组件卸载时断开连接
onUnmounted(() => {
  disconnect()
})
</script>

<style lang="scss" scoped>
.chat-room {
  padding: 20px;
  height: 100vh;

  .chat-container {
    height: 100%;

    .chat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .chat-content {
      display: flex;
      height: calc(100% - 60px);
      gap: 20px;

      .user-list {
        width: 200px;
        border-right: 1px solid #e4e7ed;
        padding-right: 20px;

        h3 {
          margin-bottom: 15px;
        }

        .user-item {
          margin-bottom: 8px;
        }
      }

      .message-area {
        flex: 1;
        display: flex;
        flex-direction: column;

        .messages {
          flex: 1;
          overflow-y: auto;
          margin-bottom: 20px;
          padding: 10px;
          border: 1px solid #e4e7ed;
          border-radius: 4px;
          max-height: 500px;

          .message {
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 8px;

            &.system {
              background-color: #fff2e8;
              text-align: center;
              font-style: italic;
            }

            &.own {
              background-color: #e6f7ff;
              margin-left: 20%;
            }

            &.other {
              background-color: #f5f7fa;
              margin-right: 20%;
            }

            &.private {
              background-color: #fff0f6;
              border-left: 3px solid #eb2f96;
            }

            .message-header {
              display: flex;
              justify-content: space-between;
              margin-bottom: 5px;
              font-size: 12px;
              color: #666;

              .sender {
                font-weight: bold;
              }
            }

            .message-content {
              word-break: break-word;
            }
          }
        }

        .input-area {
          .message-type {
            margin-bottom: 10px;
          }

          .input-box {
            display: flex;
            gap: 10px;
          }
        }
      }
    }
  }
}
</style>
