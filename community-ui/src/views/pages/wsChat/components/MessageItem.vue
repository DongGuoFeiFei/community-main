<template>
  <div class="message-item" :class="{ 'self-message': isSelf }">
    <div class="message-avatar">
      <el-avatar :src="store.baseURL + message.senderAvatar" :size="40" />
    </div>
    <div class="message-content">
      <div class="message-info">
        <span class="sender-name">{{ message.senderName }}</span>
        <span class="message-time">{{ formatTime(message.sendTime) }}</span>
      </div>
      <div class="message-body">
        <div class="message-bubble">
          <div class="bubble-tail"></div>
          <!-- 图片消息 -->
          <div v-if="isImageMessage" class="message-image">
            <el-image
              :src="imageUrl"
              :preview-src-list="[imageUrl]"
              fit="cover"
              class="chat-image"
              :lazy="true"
            />
          </div>
          <!-- 文本消息 -->
          <div
            v-else
            class="message-text"
            v-html="formatMessageContent(message.content)"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed } from "vue";
import dayjs from "dayjs";
import { localStores } from "@/stores/localStores.js";

const props = defineProps({
  message: {
    type: Object,
    required: true,
  },
  isSelf: {
    type: Boolean,
    default: false,
  },
});

const store = localStores();

/**
 * 格式化时间
 * @param {string|Date} time 时间
 * @returns {string} 格式化后的时间
 */
const formatTime = (time) => {
  return dayjs(time).format("HH:mm");
};

/**
 * 判断是否为图片消息
 */
const isImageMessage = computed(() => {
  return (
    props.message.messageType === "image" ||
    (props.message.content &&
      (props.message.content.startsWith("http://") ||
        props.message.content.startsWith("https://") ||
        props.message.content.startsWith("/")) &&
      /\.(jpg|jpeg|png|gif|webp|bmp|svg)$/i.test(props.message.content))
  );
});

/**
 * 获取图片URL
 */
const imageUrl = computed(() => {
  if (isImageMessage.value) {
    // 如果是完整URL，直接使用
    if (
      props.message.content.startsWith("http://") ||
      props.message.content.startsWith("https://")
    ) {
      return props.message.content;
    }
    // 如果是相对路径，拼接baseURL
    return store.baseURL + props.message.content;
  }
  return "";
});

/**
 * 格式化消息内容（支持emoji等）
 * @param {string} content 消息内容
 * @returns {string} 格式化后的HTML
 */
const formatMessageContent = (content) => {
  if (!content) return "";
  // 简单的文本转HTML，保持原有格式
  // 可以在这里添加emoji渲染、链接识别等功能
  return content.replace(/\n/g, "<br>");
};
</script>

<style lang="scss" scoped>
.message-item {
  display: flex;
  margin-bottom: 20px;
  animation: messageSlideIn 0.3s ease-out;

  .message-avatar {
    margin-right: 12px;
    flex-shrink: 0;
    transition: transform 0.3s;

    &:hover {
      transform: scale(1.1);
    }
  }

  .message-content {
    max-width: 65%;
    display: flex;
    flex-direction: column;

    .message-info {
      margin-bottom: 6px;
      display: flex;
      align-items: center;
      gap: 8px;

      .sender-name {
        font-size: 13px;
        font-weight: 600;
        color: #7e57c2;
      }

      .message-time {
        font-size: 11px;
        color: #b39ddb;
        opacity: 0.8;
      }
    }

    .message-body {
      position: relative;

      .message-bubble {
        position: relative;
        padding: 12px 16px;
        background: linear-gradient(
          135deg,
          rgba(255, 255, 255, 0.95) 0%,
          rgba(243, 229, 245, 0.3) 100%
        );
        border-radius: 18px;
        border: 2px solid rgba(179, 157, 219, 0.2);
        box-shadow: 0 2px 8px rgba(179, 157, 219, 0.15);
        word-break: break-word;
        transition: all 0.3s;

        &:hover {
          box-shadow: 0 4px 12px rgba(179, 157, 219, 0.25);
          transform: translateY(-1px);
        }

        // 气泡尾巴
        .bubble-tail {
          position: absolute;
          left: -8px;
          top: 12px;
          width: 0;
          height: 0;
          border-top: 8px solid transparent;
          border-bottom: 8px solid transparent;
          border-right: 8px solid rgba(179, 157, 219, 0.2);
        }

        .message-text {
          font-size: 14px;
          line-height: 1.6;
          color: #333;
          word-break: break-word;
        }

        .message-image {
          max-width: 300px;
          border-radius: 12px;
          overflow: hidden;

          .chat-image {
            width: 100%;
            max-height: 400px;
            border-radius: 12px;
            cursor: pointer;
            transition: transform 0.3s;

            &:hover {
              transform: scale(1.02);
            }
          }
        }
      }
    }
  }

  // 自己发送的消息
  &.self-message {
    flex-direction: row-reverse;

    .message-avatar {
      margin-right: 0;
      margin-left: 12px;
    }

    .message-content {
      align-items: flex-end;

      .message-info {
        flex-direction: row-reverse;

        .sender-name {
          color: #9fa8da;
        }
      }

      .message-body {
        .message-bubble {
          background: linear-gradient(135deg, #b39ddb 0%, #9fa8da 100%);
          border-color: rgba(159, 168, 218, 0.3);
          box-shadow: 0 2px 8px rgba(159, 168, 218, 0.25);

          &:hover {
            box-shadow: 0 4px 12px rgba(159, 168, 218, 0.35);
          }

          // 自己消息的尾巴在右边
          .bubble-tail {
            left: auto;
            right: -8px;
            border-right: none;
            border-left: 8px solid rgba(159, 168, 218, 0.3);
          }

          .message-text {
            color: white;
            text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
          }

          .message-image {
            .chat-image {
              border: 2px solid rgba(255, 255, 255, 0.3);
            }
          }
        }
      }
    }
  }
}

// 消息滑入动画
@keyframes messageSlideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
