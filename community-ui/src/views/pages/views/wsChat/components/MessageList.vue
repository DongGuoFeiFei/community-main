<template>
  <div class="message-list" ref="messageListRef">
    <div class="load-more" v-if="hasMore" @click="loadMore">
      <el-button link type="primary">加载更多消息</el-button>
    </div>

    <div class="date-divider" v-if="showDateDivider">
      <span>今天</span>
    </div>

    <div v-for="message in messages" :key="message.id" class="message-container">
      <MessageItem :message="message" :session="session" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUpdated, nextTick } from 'vue'
import MessageItem from './MessageItem.vue'

const props = defineProps({
  messages: {
    type: Array,
    default: () => []
  },
  session: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['load-more'])

const messageListRef = ref(null)
const hasMore = ref(true)
const showDateDivider = ref(true)

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageListRef.value) {
      messageListRef.value.scrollTop = messageListRef.value.scrollHeight
    }
  })
}

// 加载更多消息
const loadMore = () => {
  if (props.messages.length > 0) {
    const firstMessage = props.messages[0]
    emit('load-more', firstMessage.id)
  }
}

// 组件挂载和更新时自动滚动到底部
onMounted(scrollToBottom)
onUpdated(scrollToBottom)
</script>

<style lang="scss" scoped>
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: #f8f9fa;
  display: flex;
  flex-direction: column;

  .load-more {
    text-align: center;
    margin-bottom: 20px;
  }

  .date-divider {
    display: flex;
    align-items: center;
    margin: 16px 0;
    color: #6c757d;
    font-size: 12px;

    &::before,
    &::after {
      content: "";
      flex: 1;
      height: 1px;
      background-color: #dee2e6;
    }

    &::before {
      margin-right: 16px;
    }

    &::after {
      margin-left: 16px;
    }
  }

  .message-container {
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
