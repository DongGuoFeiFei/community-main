<template>
  <div :class="['message-item', { 'is-self': isSelf, 'is-system': isSystem }]">
    <div class="message-avatar">
      <el-avatar v-if="!isSystem" :size="36" :src="message.senderAvatar"/>
    </div>

    <div class="message-content">
      <div class="message-sender" v-if="!isSelf && !isSystem">
        {{ message.senderName }}
      </div>
      <div :class="['message-bubble', { 'system-message': isSystem }]">
        <div v-if="isSystem" class="system-content">
          {{ message.content }}
        </div>
        <div v-else>
          {{ message.content }}
        </div>
      </div>

      <div class="message-time">
        {{ formatTime(message.sendTime || message.timestamp) }}
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed} from 'vue'
import {localStores} from '@/stores/localStores'
import dayjs from 'dayjs'

const props = defineProps({
  message: {
    type: Object,
    required: true
  },
  session: {
    type: Object,
    required: true
  }
})

console.log(props.message)

const store = localStores()

// 判断是否是自己的消息
const isSelf = computed(() => {
  return props.message.senderId === store.userInfo.userInfo.userId
})

// 判断是否是系统消息
const isSystem = computed(() => {
  return props.message.contentType === 6 // 系统消息类型
})

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return dayjs(time).format('HH:mm')
}
</script>

<style lang="scss" scoped>
.message-item {
  display: flex;
  margin-bottom: 16px;

  &.is-self {
    flex-direction: row-reverse;

    .message-content {
      align-items: flex-end;
    }

    .message-bubble {
      background-color: #1890ff;
      //color: white;
    }
  }

  &.is-system {
    justify-content: center;

    .message-bubble {
      background-color: transparent;
      color: #999;
    }
  }

  .message-avatar {
    margin: 0 12px;
  }

  .message-content {
    display: flex;
    flex-direction: column;
    max-width: 60%;

    .message-sender {
      font-size: 12px;
      color: #999;
      margin-bottom: 4px;
    }

    .message-bubble {
      padding: 8px 12px;
      border-radius: 4px;
      background-color: white;
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
      word-break: break-word;

      &.system-message {
        background-color: transparent;
        box-shadow: none;
        font-style: italic;
      }
    }

    .message-time {
      font-size: 12px;
      color: #999;
      margin-top: 4px;
    }
  }
}
</style>
