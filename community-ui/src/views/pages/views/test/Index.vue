<template>
  <div class="chat-room-test">
    <el-card class="chat-container">
      <template #header>
        <div class="chat-header">
          <h3>测试聊天室</h3>
          <el-button
              type="primary"
              size="small"
              @click="connectWebSocket"
              :loading="connecting"
              :disabled="isConnected"
          >
            {{ isConnected ? '已连接' : '连接WebSocket' }}
          </el-button>
          <el-button
              type="danger"
              size="small"
              @click="disconnectWebSocket"
              :disabled="!isConnected"
          >
            断开连接
          </el-button>
        </div>
      </template>

      <div class="chat-content">
        <div class="message-list" ref="messageListRef">
          <div
              v-for="(msg, index) in messages"
              :key="index"
              class="message-item"
              :class="{ 'self-message': msg.senderId === currentUserId }"
          >
            <div class="message-avatar">
              <el-avatar :src="msg.avatar || defaultAvatar" />
            </div>
            <div class="message-content">
              <div class="message-info">
                <span class="nickname">{{ msg.senderNickname || '匿名用户' }}</span>
                <span class="time">{{ formatTime(msg.timestamp) }}</span>
              </div>
              <div class="message-text">{{ msg.content }}</div>
            </div>
          </div>
        </div>

        <div class="message-input">
          <el-input
              v-model="inputMessage"
              placeholder="输入消息..."
              @keyup.enter="sendMessage"
              :disabled="!isConnected"
          >
            <template #append>
              <el-button
                  type="primary"
                  @click="sendMessage"
                  :disabled="!isConnected || !inputMessage.trim()"
              >
                发送
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';
import { useChatWebSocket } from '@/utils/websocket';
import { localStores } from '@/stores/localStores';

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

// 状态管理
const store = localStores();
const currentUserId = ref(store.userInfo.userInfo?.userId || 'test-user');

// WebSocket相关
const {
  connect,
  disconnect,
  subscribe,
  send,
  isConnected,
  error
} = useChatWebSocket();
const connecting = ref(false);
const subscription = ref(null);

// 聊天数据
const messages = ref([]);
const inputMessage = ref('');
const messageListRef = ref(null);

// 格式化时间
const formatTime = (timestamp) => {
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss');
};

// 连接WebSocket
const connectWebSocket = async () => {
  try {
    connecting.value = true;
    await connect();

    // 订阅聊天室
    const sessionId = '1145141919810'; // 测试用聊天室ID
    subscription.value = subscribe(
        `/topic/chatRoom.private.${sessionId}`,
        handleMessage
    );

    ElMessage.success('WebSocket连接成功');
  } catch (err) {
    console.error('WebSocket连接失败:', err);
    ElMessage.error(`连接失败: ${err.message || '未知错误'}`);
  } finally {
    connecting.value = false;
  }
};

// 断开WebSocket
const disconnectWebSocket = () => {
  try {
    if (subscription.value) {
      subscription.value.unsubscribe();
      subscription.value = null;
    }
    disconnect();
    ElMessage.success('已断开WebSocket连接');
  } catch (err) {
    console.error('断开连接失败:', err);
    ElMessage.error(`断开连接失败: ${err.message || '未知错误'}`);
  }
};

// 处理收到的消息
const handleMessage = (message) => {
  messages.value.push({
    ...message,
    timestamp: new Date().toISOString()
  });
  scrollToBottom();
};

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return;

  const message = {
    content: inputMessage.value,
    senderId: currentUserId.value,
    senderNickname: store.userInfo.userInfo?.nickname || '测试用户',
    avatar: store.userInfo.userInfo?.avatar,
    timestamp: new Date().toISOString()
  };

  try {
    const sessionId = '1145141919810'; // 测试用聊天室ID
    send(`/app/privateChat.${sessionId}`, message);

    // 本地显示自己发送的消息
    messages.value.push(message);
    inputMessage.value = '';
    scrollToBottom();
  } catch (err) {
    console.error('发送消息失败:', err);
    ElMessage.error(`发送消息失败: ${err.message || '未知错误'}`);
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

// 组件生命周期
onMounted(() => {

  connectWebSocket()
  // 添加一些测试消息
  messages.value = [
    {
      content: '欢迎来到测试聊天室！',
      senderId: 'system',
      senderNickname: '系统消息',
      timestamp: Date.now() - 60000,
      avatar: defaultAvatar
    },
    {
      content: '这是一个WebSocket聊天功能的测试',
      senderId: 'system',
      senderNickname: '系统消息',
      timestamp: Date.now() - 30000,
      avatar: defaultAvatar
    }
  ];
});

onUnmounted(() => {
  if (isConnected.value) {
    disconnectWebSocket();
  }
});
</script>

<style lang="scss" scoped>
.chat-room-test {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;

  .chat-container {
    height: 600px;
    display: flex;
    flex-direction: column;

    .chat-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .chat-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      height: 500px; // 解决flex布局下的滚动问题


      .message-list {
        flex: 1;
        overflow-y: auto;
        padding: 10px;
        border-bottom: 1px solid #eee;

        .message-item {
          display: flex;
          margin-bottom: 15px;

          .message-avatar {
            margin-right: 12px;
          }

          .message-content {
            flex: 1;

            .message-info {
              margin-bottom: 5px;

              .nickname {
                font-weight: bold;
                margin-right: 10px;
              }

              .time {
                font-size: 12px;
                color: #999;
              }
            }

            .message-text {
              padding: 8px 12px;
              background: #f5f5f5;
              border-radius: 4px;
              display: inline-block;
              max-width: 80%;
              word-break: break-word;
            }
          }

          &.self-message {
            flex-direction: row-reverse;

            .message-avatar {
              margin-right: 0;
              margin-left: 12px;
            }

            .message-content {
              text-align: right;

              .message-text {
                background: #409eff;
                color: white;
              }
            }
          }
        }
      }

      .message-input {
        padding-top: 10px;
      }
    }
  }
}
</style>
