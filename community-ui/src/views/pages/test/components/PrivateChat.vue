<template>
  <div class="chat-container">
    <div class="chat-header">
      <h3>私聊: {{ 1 }}</h3>
      <el-button
          @click="toggleConnection"
          :type="isConnected ? 'danger' : 'success'"
      >
        {{ isConnected ? '断开连接' : '连接' }}
      </el-button>
    </div>

    <div class="chat-messages">
      <div
          v-for="(msg, index) in messages"
          :key="index"
          :class="['message', msg.senderId === store.userInfo.userInfo.userId ? 'sent' : 'received']"
      >
        <div class="message-header">
          <span class="sender">{{ msg.senderName }}</span>
          <span class="time">{{ formatTime(msg.timestamp) }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
      </div>
    </div>

    <div class="chat-input">
      <el-input
          v-model="inputMessage"
          placeholder="输入消息..."
          @keyup.enter="sendMessage"
      />
      <el-button
          type="primary"
          @click="sendMessage"
          :disabled="!isConnected"
      >
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useWebSocket } from './websocket.js'
import { localStores } from '@/stores/localStores.js'
import dayjs from 'dayjs'

const store = localStores()
const { connect, disconnect, subscribe, send, isConnected } = useWebSocket()
const messages = ref([])
const inputMessage = ref('')
const privateSubscription = ref(null)

// 格式化时间
const formatTime = (timestamp) => {
  return dayjs(timestamp).format('HH:mm')
}

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim()) return

  const message = {
    senderId: store.userInfo.userInfo.userId,
    senderName: store.userInfo.userInfo.username || '匿名',
    content: inputMessage.value,
    timestamp: Date.now()
  }

  if (send(`/app/privateChat.1`, message)) {
    messages.value.push(message)
    inputMessage.value = ''
  }
}

// 切换连接状态
const toggleConnection = () => {
  if (isConnected.value) {
    disconnect()
  } else {
    connect()
    setupSubscriptions()
  }
}

// 设置订阅
const setupSubscriptions = () => {
  // 订阅公共聊天室
  privateSubscription.value = subscribe(
      `/topic/chatRoom.private.1`,
      (message) => {
        messages.value.push(message)
      }
  )

  // 订阅私人队列（可选）
  subscribe(
      `/user/queue/private`,
      (message) => {
        if (message.senderId !== props.currentUserId) {
          messages.value.push(message)
        }
      }
  )
}

onMounted(() => {
  connect()
  setupSubscriptions()
})

onUnmounted(() => {
  disconnect()
})
</script>

<style lang="scss" scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.chat-header {
  padding: 12px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-messages {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
  background-color: #fff;
}

.message {
  margin-bottom: 12px;
  padding: 8px 12px;
  border-radius: 4px;
  max-width: 70%;

  &.sent {
    margin-left: auto;
    background-color: #409eff;
    color: white;
  }

  &.received {
    margin-right: auto;
    background-color: #f5f7fa;
  }
}

.message-header {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  margin-bottom: 4px;
}

.message-content {
  word-break: break-word;
}

.chat-input {
  display: flex;
  padding: 12px;
  border-top: 1px solid #ebeef5;
  background-color: #f5f7fa;

  .el-input {
    flex: 1;
    margin-right: 12px;
  }
}
</style>
