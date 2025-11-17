<template>
  <div class="chat-room">
    <!-- 聊天室头部 -->
    <div class="chat-header">
      <div class="header-info">
        <div class="avatar-wrapper">
          <el-avatar :src="currentSession.avatar" :size="44" />
          <div class="status-dot" :class="{ online: isConnected }"></div>
        </div>
        <div class="header-text">
          <div class="session-name">
            {{ currentSession.name }}
            <span class="name-emoji">🌸</span>
          </div>
          <div class="session-status">
            <span v-if="isConnected" class="status-online">
              <span class="status-icon">●</span>
              在线
            </span>
            <span v-else class="status-offline">
              <span class="status-icon">●</span>
              离线
            </span>
          </div>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="text" class="action-btn" @click="showMoreActions">
          <span class="action-icon">⋯</span>
        </el-button>
      </div>
    </div>

    <!-- 聊天内容区 -->
    <div class="chat-content">
      <div class="message-list" ref="messageListRef">
        <!-- 加载更多 -->
        <div v-if="loading" class="loading-more">
          <div class="loading-spinner"></div>
          <span class="loading-text">加载中...</span>
        </div>

        <!-- 消息列表 -->
        <div
          v-for="message in messages"
          :key="message.id"
          class="message-wrapper"
        >
          <MessageItem
            :message="message"
            :is-self="message.senderId === currentUserId"
          />
        </div>
      </div>

      <!-- 消息输入区 -->
      <div class="message-input">
        <div class="input-wrapper">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :rows="3"
            placeholder="输入消息... ( •̀ ω •́ )✧"
            resize="none"
            @keyup.enter="sendMessage"
            class="message-textarea"
          />
        </div>
        <div class="input-actions">
          <div class="action-buttons">
            <el-button type="text" class="emoji-btn" @click="toggleEmojiPicker">
              <el-icon class="btn-icon">
                <ChatRound />
              </el-icon>
            </el-button>
            <el-button type="text" class="file-btn" @click="triggerFilePicker">
              <el-icon class="btn-icon">
                <Paperclip />
              </el-icon>
            </el-button>
            <!-- 隐藏的文件输入框 -->
            <input
              ref="fileInputRef"
              type="file"
              accept="image/*"
              style="display: none"
              @change="handleFileSelect"
            />
          </div>
          <!-- 表情选择器 -->
          <div v-if="showEmoji" class="emoji-picker-wrapper">
            <EmojiPicker
              @select="handleEmojiSelect"
              @close="closeEmojiPicker"
            />
          </div>
          <el-button
            class="send-btn"
            @click="sendMessage"
            :disabled="!inputMessage.trim() || uploading"
            :loading="uploading"
          >
            <el-icon class="send-icon">
              <Promotion />
            </el-icon>
            <span>发送</span>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, onUnmounted, ref } from "vue";
import { ChatRound, Paperclip, Promotion } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { useChatWebSocket } from "@/utils/websocket.js";
import { localStores } from "@/stores/localStores.js";
import { getMessages, markMessageAsRead } from "@/api/message.js";
import { uploadFile } from "@/api/files.js";
import MessageItem from "./MessageItem.vue";
import EmojiPicker from "./EmojiPicker.vue";

const props = defineProps({
  sessionId: {
    type: Number,
    required: true,
  },
  sessionDetail: {
    type: Object,
    required: true,
  },
});

const store = localStores();
const currentUserId = computed(() => store.userInfo.userInfo?.userId);

// WebSocket相关
const { connect, disconnect, subscribe, send, isConnected, error } =
  useChatWebSocket();
const subscription = ref(null);

// 消息数据
const messages = ref([]);
const inputMessage = ref("");
const messageListRef = ref(null);
const loading = ref(false);
const hasMore = ref(true);
const showEmoji = ref(false); // 表情选择器显示状态
const fileInputRef = ref(null); // 文件输入框引用
const uploading = ref(false); // 上传状态

console.log(props.sessionDetail);
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
    console.error("加载消息失败:", error);
  } finally {
    loading.value = false;
  }
};

/**
 * 发送消息
 */
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return;

  const message = {
    content: inputMessage.value,
    senderId: currentUserId.value,
    sessionId: props.sessionId,
    senderName: store.userInfo.userInfo.nickname,
    messageType: "text", // 文本消息类型
  };

  try {
    send(`/app/privateChat.${props.sessionId}`, message);
    inputMessage.value = "";
    showEmoji.value = false; // 关闭表情选择器
    scrollToBottom();
  } catch (err) {
    console.error("发送消息失败:", err);
    ElMessage.error("发送消息失败");
  }
};

/**
 * 发送图片消息
 * @param {string} imageUrl 图片URL
 */
const sendImageMessage = async (imageUrl) => {
  const message = {
    content: imageUrl,
    senderId: currentUserId.value,
    sessionId: props.sessionId,
    messageType: "image", // 图片消息类型
  };

  try {
    send(`/app/privateChat.${props.sessionId}`, message);
    scrollToBottom();
  } catch (err) {
    console.error("发送图片消息失败:", err);
    ElMessage.error("发送图片消息失败");
  }
};

/**
 * 切换表情选择器显示状态
 */
const toggleEmojiPicker = () => {
  showEmoji.value = !showEmoji.value;
};

/**
 * 关闭表情选择器
 */
const closeEmojiPicker = () => {
  showEmoji.value = false;
};

/**
 * 处理表情选择
 * @param {string} emoji 选中的表情
 */
const handleEmojiSelect = (emoji) => {
  inputMessage.value += emoji;
  // 选择表情后不关闭选择器，方便继续选择
  showEmoji.value = false;
};

/**
 * 触发文件选择器
 */
const triggerFilePicker = () => {
  if (fileInputRef.value) {
    fileInputRef.value.click();
  }
};

/**
 * 处理文件选择
 * @param {Event} event 文件选择事件
 */
const handleFileSelect = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 验证文件类型（仅支持图片）
  if (!file.type.startsWith("image/")) {
    ElMessage.warning("仅支持上传图片文件");
    // 清空文件输入框
    if (fileInputRef.value) {
      fileInputRef.value.value = "";
    }
    return;
  }

  // 验证文件大小（限制为10MB）
  const maxSize = 10 * 1024 * 1024; // 10MB
  if (file.size > maxSize) {
    ElMessage.warning("图片大小不能超过10MB");
    if (fileInputRef.value) {
      fileInputRef.value.value = "";
    }
    return;
  }

  try {
    uploading.value = true;
    ElMessage.info("正在上传图片...");

    // 上传文件
    const imageUrl = await uploadFile(file);

    // 发送图片消息
    await sendImageMessage(imageUrl);

    ElMessage.success("图片发送成功");
  } catch (error) {
    console.error("上传图片失败:", error);
    ElMessage.error(error.message || "上传图片失败");
  } finally {
    uploading.value = false;
    // 清空文件输入框
    if (fileInputRef.value) {
      fileInputRef.value.value = "";
    }
  }
};

/**
 * 显示更多操作（占位函数）
 */
const showMoreActions = () => {
  // TODO: 实现更多操作
  console.log("显示更多操作");
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
    console.error("WebSocket连接失败:", err);
  }
};

// 滚动事件处理
const handleScroll = () => {
  if (!messageListRef.value) return;

  const { scrollTop } = messageListRef.value;
  if (scrollTop < 100 && hasMore.value) {
    // loadMessages();
  }
};

onMounted(() => {
  initWebSocket();
  if (messageListRef.value) {
    messageListRef.value.addEventListener("scroll", handleScroll);
  }
});

onUnmounted(() => {
  if (subscription.value) {
    subscription.value.unsubscribe();
  }
  disconnect();

  if (messageListRef.value) {
    messageListRef.value.removeEventListener("scroll", handleScroll);
  }
});
</script>

<style lang="scss" scoped>
.chat-room {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(
    180deg,
    rgba(227, 242, 253, 0.2) 0%,
    rgba(255, 255, 255, 0.5) 100%
  );

  // 聊天室头部
  .chat-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    background: linear-gradient(
      135deg,
      rgba(179, 157, 219, 0.08) 0%,
      rgba(159, 168, 218, 0.08) 100%
    );
    border-bottom: 2px solid rgba(179, 157, 219, 0.15);
    backdrop-filter: blur(10px);

    .header-info {
      display: flex;
      align-items: center;
      gap: 12px;

      .avatar-wrapper {
        position: relative;

        .status-dot {
          position: absolute;
          bottom: 2px;
          right: 2px;
          width: 12px;
          height: 12px;
          background: #bdbdbd;
          border: 2px solid white;
          border-radius: 50%;
          transition: all 0.3s;

          &.online {
            background: linear-gradient(135deg, #81c784 0%, #66bb6a 100%);
            animation: pulse 2s infinite;
          }
        }
      }

      .header-text {
        .session-name {
          font-weight: 600;
          font-size: 16px;
          color: #333;
          display: flex;
          align-items: center;
          gap: 6px;
          margin-bottom: 4px;

          .name-emoji {
            font-size: 14px;
            animation: rotate 3s infinite ease-in-out;
          }
        }

        .session-status {
          font-size: 13px;
          display: flex;
          align-items: center;
          gap: 4px;

          .status-icon {
            font-size: 8px;
          }

          .status-online {
            color: #66bb6a;
            font-weight: 500;
          }

          .status-offline {
            color: #bdbdbd;
          }
        }
      }
    }

    .header-actions {
      .action-btn {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: rgba(179, 157, 219, 0.1);
        transition: all 0.3s;

        &:hover {
          background: rgba(179, 157, 219, 0.2);
          transform: rotate(90deg);
        }

        .action-icon {
          font-size: 20px;
          color: #b39ddb;
          font-weight: bold;
        }
      }
    }
  }

  // 聊天内容区
  .chat-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .message-list {
      flex: 1;
      overflow-y: auto;
      padding: 20px;

      // 自定义滚动条
      &::-webkit-scrollbar {
        width: 8px;
      }

      &::-webkit-scrollbar-track {
        background: rgba(179, 157, 219, 0.05);
        border-radius: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(179, 157, 219, 0.3);
        border-radius: 4px;
        transition: background 0.3s;

        &:hover {
          background: rgba(179, 157, 219, 0.5);
        }
      }

      // 加载更多
      .loading-more {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 15px;
        gap: 10px;

        .loading-spinner {
          width: 20px;
          height: 20px;
          border: 3px solid rgba(179, 157, 219, 0.2);
          border-top-color: #b39ddb;
          border-radius: 50%;
          animation: spin 1s linear infinite;
        }

        .loading-text {
          color: #b39ddb;
          font-size: 14px;
        }
      }

      .message-wrapper {
        animation: fadeInUp 0.3s ease-out;
      }
    }

    // 消息输入区
    .message-input {
      border-top: 2px solid rgba(179, 157, 219, 0.15);
      padding: 16px 20px;
      background: white;

      .input-wrapper {
        margin-bottom: 12px;

        .message-textarea {
          :deep(.el-textarea__inner) {
            border-radius: 16px;
            border: 2px solid rgba(179, 157, 219, 0.2);
            background: linear-gradient(
              135deg,
              rgba(227, 242, 253, 0.3) 0%,
              rgba(243, 229, 245, 0.3) 100%
            );
            padding: 12px 16px;
            font-size: 14px;
            transition: all 0.3s;

            &:focus {
              border-color: #b39ddb;
              box-shadow: 0 0 0 3px rgba(179, 157, 219, 0.1);
              background: white;
            }

            &::placeholder {
              color: #b39ddb;
              opacity: 0.6;
            }
          }
        }
      }

      .input-actions {
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: relative; // 为表情选择器提供定位上下文

        .action-buttons {
          display: flex;
          gap: 8px;

          .emoji-btn,
          .file-btn {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: linear-gradient(
              135deg,
              rgba(179, 157, 219, 0.1) 0%,
              rgba(159, 168, 218, 0.1) 100%
            );
            transition: all 0.3s;
            color: #b39ddb;

            &:hover {
              background: linear-gradient(
                135deg,
                rgba(179, 157, 219, 0.2) 0%,
                rgba(159, 168, 218, 0.2) 100%
              );
              transform: scale(1.1);
              color: #7e57c2;
            }

            .btn-icon {
              font-size: 20px;
            }
          }
        }

        // 表情选择器包装器
        .emoji-picker-wrapper {
          position: absolute;
          bottom: 100%;
          left: 0;
          margin-bottom: 8px;
          z-index: 100; // 根据开发规范使用合适的 z-index
        }

        .send-btn {
          padding: 10px 24px;
          border-radius: 20px;
          background: linear-gradient(135deg, #b39ddb 0%, #9fa8da 100%);
          border: none;
          color: white;
          font-weight: 600;
          font-size: 14px;
          display: flex;
          align-items: center;
          gap: 6px;
          transition: all 0.3s;
          box-shadow: 0 4px 12px rgba(179, 157, 219, 0.3);

          &:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(179, 157, 219, 0.4);
          }

          &:active:not(:disabled) {
            transform: translateY(0);
          }

          &:disabled {
            opacity: 0.5;
            cursor: not-allowed;
          }

          .send-icon {
            font-size: 16px;
            animation: fly 2s infinite ease-in-out;
            color: white;
          }
        }
      }
    }
  }
}

// 动画定义
@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.8;
  }
}

@keyframes rotate {
  0%,
  100% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(-15deg);
  }
  75% {
    transform: rotate(15deg);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fly {
  0%,
  100% {
    transform: translateX(0) rotate(0deg);
  }
  50% {
    transform: translateX(3px) rotate(-10deg);
  }
}
</style>
