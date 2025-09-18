<template>
  <div class="chat-room">
    <div class="chat-header">
      <div class="header-info">
        <el-avatar :src="currentSession.avatar" :size="40" />
        <div class="header-text">
          <div class="session-name">{{ currentSession.name }}</div>
          <div class="session-status">
            <span v-if="isConnected">在线</span>
            <span v-else class="offline">离线</span>
          </div>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="text" icon="el-icon-more" @click="showMoreActions" />
      </div>
    </div>

    <div class="chat-content">
      <div class="message-list" ref="messageListRef">
        <div v-if="loading" class="loading-more">
          <el-icon class="is-loading"><loading /></el-icon>
          <span>加载中...</span>
        </div>

        <div v-for="message in messages" :key="message.id">
          <MessageItem
              :message="message"
              :is-self="message.senderId === currentUserId"
          />
        </div>
      </div>

      <div class="message-input">
        <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息..."
            resize="none"
            @keyup.enter="sendMessage"
        />
        <div class="input-actions">
          <el-button type="text" icon="el-icon-picture" @click="showEmojiPicker" />
          <el-button type="text" icon="el-icon-folder" @click="showFilePicker" />
          <el-button
              type="primary"
              size="small"
              @click="sendMessage"
              :disabled="!inputMessage.trim()"
          >
            发送
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue';
import { Loading } from '@element-plus/icons-vue';
import { useChatWebSocket } from '@/utils/websocket';
import { localStores } from '@/stores/localStores';
import { getMessages, markMessageAsRead } from '@/api/message';
import MessageItem from './MessageItem.vue';

const props = defineProps({
  sessionId: {
    type: Number,
    required: true
  },
  sessionDetail: {
    type: Object,
    required: true
  }
});

const store = localStores();
const currentUserId = computed(() => store.userInfo.userInfo?.userId);

// WebSocket相关
const {
  connect,
  disconnect,
  subscribe,
  send,
  isConnected,
  error
} = useChatWebSocket();
const subscription = ref(null);

// 消息数据
const messages = ref([]);
const inputMessage = ref('');
const messageListRef = ref(null);
const loading = ref(false);
const hasMore = ref(true);
console.log(props.sessionDetail)
// 当前会话信息
const currentSession = computed(() => props.sessionDetail);

// 加载历史消息
const loadMessages = async () => {
  if (loading.value || !hasMore.value) return;

  try {
    loading.value = true;
    const lastMessageId = messages.value[0]?.id;
    const res = await getMessages(props.sessionId, lastMessageId);

    if (res.data.length === 0) {
      hasMore.value = false;
    } else {
      messages.value = [...res.data, ...messages.value];
    }
  } catch (error) {
    console.error('加载消息失败:', error);
  } finally {
    loading.value = false;
  }
};

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return;

  const message = {
    content: inputMessage.value,
    senderId: currentUserId.value,
    sessionId: props.sessionId
  };

  try {
    send(`/app/privateChat.${props.sessionId}`, message);
    inputMessage.value = '';
    scrollToBottom();
  } catch (err) {
    console.error('发送消息失败:', err);
  }
};

// 处理收到的消息
const handleMessage = (message) => {
  messages.value.push(message);
  scrollToBottom();

  // 如果是对方发来的消息，标记为已读
  if (message.senderId !== currentUserId.value) {
    markMessageAsRead(props.sessionId, message.id);
  }
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight;
    }
  });
};

// 初始化WebSocket连接
const initWebSocket = async () => {
  try {
    await connect();

    // 订阅当前会话的消息
    subscription.value = subscribe(
        `/topic/chatRoom.private.${props.sessionId}`,
        handleMessage
    );

    // 加载初始消息
    await loadMessages();
    scrollToBottom();
  } catch (err) {
    console.error('WebSocket连接失败:', err);
  }
};

// 滚动事件处理
const handleScroll = () => {
  if (!messageListRef.value) return;

  const { scrollTop } = messageListRef.value;
  if (scrollTop < 100 && hasMore.value) {
    loadMessages();
  }
};

onMounted(() => {
  initWebSocket();
  if (messageListRef.value) {
    messageListRef.value.addEventListener('scroll', handleScroll);
  }
});

onUnmounted(() => {
  if (subscription.value) {
    subscription.value.unsubscribe();
  }
  disconnect();

  if (messageListRef.value) {
    messageListRef.value.removeEventListener('scroll', handleScroll);
  }
});
</script>

<style lang="scss" scoped>
.chat-room {
  height: 100%;
  display: flex;
  flex-direction: column;

  .chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    border-bottom: 1px solid #eee;

    .header-info {
      display: flex;
      align-items: center;

      .header-text {
        margin-left: 12px;

        .session-name {
          font-weight: 500;
        }

        .session-status {
          font-size: 12px;
          color: #999;

          .offline {
            color: #f56c6c;
          }
        }
      }
    }
  }

  .chat-content {
    flex: 1;
    display: flex;
    flex-direction: column;

    .message-list {
      flex: 1;
      overflow-y: auto;
      padding: 16px;

      .loading-more {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 10px;
        color: #999;

        .el-icon {
          margin-right: 8px;
        }
      }
    }

    .message-input {
      border-top: 1px solid #eee;
      padding: 12px;

      .input-actions {
        display: flex;
        justify-content: flex-end;
        align-items: center;
        margin-top: 8px;
      }
    }
  }
}
</style>
