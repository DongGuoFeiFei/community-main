<template>
  <div class="typing-indicator">
    <div class="typing-dots">
      <span></span>
      <span></span>
      <span></span>
    </div>
    <div class="typing-text">
      {{ typingText }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  typingUsers: {
    type: Array,
    default: () => []
  }
})

const typingText = computed(() => {
  if (props.typingUsers.length === 0) return ''

  if (props.typingUsers.length === 1) {
    return `${props.typingUsers[0]}正在输入...`
  }

  if (props.typingUsers.length === 2) {
    return `${props.typingUsers[0]}和${props.typingUsers[1]}正在输入...`
  }

  return `${props.typingUsers[0]}等${props.typingUsers.length}人正在输入...`
})
</script>

<style lang="scss" scoped>
.typing-indicator {
  display: flex;
  align-items: center;
  padding: 8px 16px;
  color: #999;
  font-size: 12px;

  .typing-dots {
    display: flex;
    margin-right: 8px;

    span {
      width: 4px;
      height: 4px;
      border-radius: 50%;
      background-color: #999;
      margin: 0 1px;
      animation: typing 1.4s infinite ease-in-out;

      &:nth-child(1) {
        animation-delay: 0s;
      }

      &:nth-child(2) {
        animation-delay: 0.2s;
      }

      &:nth-child(3) {
        animation-delay: 0.4s;
      }
    }
  }
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-5px);
  }
}
</style>
