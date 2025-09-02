<template>
  <div class="chat-input">
    <div class="input-tools">
      <el-button link @click="toggleEmoji">
        <el-icon>
          <Star/>
        </el-icon>
      </el-button>
      <el-button link @click="showFilePicker">
        <el-icon>
          <Paperclip/>
        </el-icon>
      </el-button>
    </div>

    <div class="input-area">
      <el-input
          v-model="messageText"
          type="textarea"
          :rows="3"
          :maxlength="1000"
          placeholder="输入消息..."
          @keydown.enter="handleKeydown"
          @input="handleInput"
      />
    </div>

    <div class="input-actions">
      <el-button type="primary" :disabled="!messageText.trim()" @click="sendMessage">
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {Paperclip, Star} from '@element-plus/icons-vue'

const messageText = ref('')
const isTyping = ref(false)
let typingTimeout = null

const emit = defineEmits(['send-message', 'typing-start', 'typing-stop'])

// 发送消息
const sendMessage = () => {
  if (messageText.value.trim()) {
    emit('send-message', messageText.value.trim())
    messageText.value = ''
    emitTypingStop()
  }
}

// 处理键盘事件
const handleKeydown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

// 处理输入事件
const handleInput = () => {
  if (!isTyping.value) {
    isTyping.value = true
    emit('typing-start')
  }

  // 清除之前的定时器
  if (typingTimeout) {
    clearTimeout(typingTimeout)
  }

  // 设置新的定时器
  typingTimeout = setTimeout(() => {
    emitTypingStop()
  }, 1000)
}

// 发送停止输入事件
const emitTypingStop = () => {
  isTyping.value = false
  emit('typing-stop')
  if (typingTimeout) {
    clearTimeout(typingTimeout)
    typingTimeout = null
  }
}

// 切换表情选择
const toggleEmoji = () => {
  // 实现表情选择逻辑
  console.log('打开表情选择器')
}

// 显示文件选择器
const showFilePicker = () => {
  // 实现文件选择逻辑
  console.log('打开文件选择器')
}
</script>

<style lang="scss" scoped>
.chat-input {
  padding: 16px;
  border-top: 1px solid #e0e0e0;
  background: white;

  .input-tools {
    margin-bottom: 8px;
  }

  .input-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: 8px;
  }
}
</style>
