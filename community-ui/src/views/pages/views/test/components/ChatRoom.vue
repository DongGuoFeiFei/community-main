<script setup>
import {onMounted, onUnmounted, ref, watch} from 'vue';
import {useChatWebSocket} from '@/utils/websocket';

const {
  connect,
  disconnect,
  isConnected,
  sendPublicMessage,
  sendPrivateMessage,
  sendTypingIndicator,
  publicMessages,
  privateMessagesList,
  isTyping,
  typingUsersList,
  error
} = useChatWebSocket();

const messageInput = ref('');
const privateMessageInput = ref('');
const recipientInput = ref('');
const activeTab = ref('public');
const isTypingTimeout = ref(null);

onMounted(() => {
  connect();
});

onUnmounted(() => {
  disconnect();
});

const handleSendMessage = () => {
  if (messageInput.value.trim()) {
    if (activeTab.value === 'public') {
      sendPublicMessage(messageInput.value);
    } else {
      if (!recipientInput.value.trim()) {
        alert('Please enter recipient username');
        return;
      }
      sendPrivateMessage(messageInput.value, recipientInput.value);
    }
    messageInput.value = '';
    sendTypingIndicator(false);
  }
};

const handleInputTyping = () => {
  if (isTypingTimeout.value) {
    clearTimeout(isTypingTimeout.value);
  }

  sendTypingIndicator(true);

  isTypingTimeout.value = setTimeout(() => {
    sendTypingIndicator(false);
  }, 1000);
};

watch(error, (newError) => {
  if (newError) {
    console.error('WebSocket error:', newError);
  }
});
</script>

<template>
  <div class="chat-container">
    <!-- 连接状态 -->
    <div class="connection-status" :class="{ connected: isConnected, disconnected: !isConnected }">
      {{ isConnected ? '🟢 Connected' : '🔴 Disconnected' }}
    </div>

    <!-- 错误显示 -->
    <div v-if="error" class="error-message">
      {{ error }}
    </div>

    <!-- 选项卡 -->
    <div class="chat-tabs">
      <button
          :class="{ active: activeTab === 'public' }"
          @click="activeTab = 'public'"
      >
        Public Chat
      </button>
      <button
          :class="{ active: activeTab === 'private' }"
          @click="activeTab = 'private'"
      >
        Private Chat
      </button>
    </div>

    <!-- 私聊收件人输入 -->
    <div v-if="activeTab === 'private'" class="recipient-input">
      <input
          v-model="recipientInput"
          placeholder="Enter recipient username"
      />
    </div>

    <!-- 消息列表 -->
    <div class="messages-container">
      <div
          v-for="(msg, index) in activeTab === 'public' ? publicMessages : privateMessagesList"
          :key="index"
          :class="['message', msg.type.toLowerCase(), { own: msg.sender === $store.user.username }]"
      >
        <div class="message-header">
          <span class="sender">{{ msg.sender }}</span>
          <span class="timestamp">{{ new Date(msg.timestamp).toLocaleTimeString() }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
      </div>
    </div>

    <!-- 输入状态指示 -->
    <div v-if="isTyping" class="typing-indicator">
      {{ typingUsersList.join(', ') }} is typing...
    </div>

    <!-- 消息输入 -->
    <div class="message-input">
      <input
          v-model="messageInput"
          @keyup.enter="handleSendMessage"
          @input="handleInputTyping"
          placeholder="Type your message..."
      />
      <button @click="handleSendMessage" :disabled="!isConnected">
        Send
      </button>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.chat-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

  .connection-status {
    padding: 8px 12px;
    border-radius: 6px;
    margin-bottom: 15px;
    font-weight: 500;

    &.connected {
      background-color: #e8f5e8;
      color: #2e7d32;
    }

    &.disconnected {
      background-color: #ffebee;
      color: #c62828;
    }
  }

  .error-message {
    background-color: #ffebee;
    color: #c62828;
    padding: 10px;
    border-radius: 6px;
    margin-bottom: 15px;
    border: 1px solid #ffcdd2;
  }

  .chat-tabs {
    display: flex;
    margin-bottom: 15px;
    border-bottom: 2px solid #f0f0f0;

    button {
      padding: 10px 20px;
      border: none;
      background: none;
      cursor: pointer;
      font-weight: 500;
      color: #666;
      border-bottom: 2px solid transparent;

      &.active {
        color: #2196f3;
        border-bottom-color: #2196f3;
      }

      &:hover {
        background-color: #f5f5f5;
      }
    }
  }

  .recipient-input {
    margin-bottom: 15px;

    input {
      width: 100%;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 6px;
      font-size: 14px;
    }
  }

  .messages-container {
    height: 400px;
    overflow-y: auto;
    margin-bottom: 15px;
    padding: 15px;
    background-color: #fafafa;
    border-radius: 8px;
    border: 1px solid #eee;

    .message {
      margin-bottom: 15px;
      padding: 12px;
      border-radius: 8px;
      background-color: #fff;
      border: 1px solid #e0e0e0;

      &.own {
        background-color: #e3f2fd;
        border-color: #bbdefb;
      }

      &.system {
        background-color: #fff3e0;
        border-color: #ffe0b2;
        font-style: italic;
      }

      .message-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 5px;
        font-size: 12px;
        color: #666;

        .sender {
          font-weight: 600;
        }
      }

      .message-content {
        font-size: 14px;
        line-height: 1.4;
      }
    }
  }

  .typing-indicator {
    padding: 8px 12px;
    background-color: #f5f5f5;
    border-radius: 6px;
    margin-bottom: 15px;
    font-size: 12px;
    color: #666;
    font-style: italic;
  }

  .message-input {
    display: flex;
    gap: 10px;

    input {
      flex: 1;
      padding: 12px;
      border: 1px solid #ddd;
      border-radius: 6px;
      font-size: 14px;

      &:focus {
        outline: none;
        border-color: #2196f3;
      }
    }

    button {
      padding: 12px 20px;
      background-color: #2196f3;
      color: white;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-weight: 500;

      &:hover:not(:disabled) {
        background-color: #1976d2;
      }

      &:disabled {
        background-color: #ccc;
        cursor: not-allowed;
      }
    }
  }
}
</style>
