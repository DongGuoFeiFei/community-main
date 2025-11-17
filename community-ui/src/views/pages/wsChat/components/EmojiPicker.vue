<template>
  <div class="emoji-picker-container" ref="pickerRef" @click.stop>
    <div class="emoji-picker">
      <div class="emoji-header">
        <span class="picker-title">选择表情</span>
      </div>
      <div class="emoji-list">
        <div
          v-for="(emoji, index) in emojiList"
          :key="index"
          class="emoji-item"
          @click.stop="selectEmoji(emoji)"
          :title="emoji"
        >
          {{ emoji }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, onMounted, onUnmounted, nextTick } from "vue";

const pickerRef = ref(null);

// 定义常用表情列表（精简版，只包含最常用的表情）
const emojiList = ref([
  "😊",
  "😃",
  "😄",
  "😁",
  "😆",
  "😅",
  "😂",
  "🤣",
  "😉",
  "😍",
  "🥰",
  "😘",
  "😗",
  "😙",
  "😚",
  "😋",
  "😛",
  "😝",
  "😜",
  "🤪",
  "🤨",
  "🤓",
  "😎",
  "🤩",
  "🥳",
  "😏",
  "😒",
  "😞",
  "😔",
  "😟",
  "😕",
  "🙁",
  "😣",
  "😖",
  "😫",
  "😩",
  "🥺",
  "😢",
  "😭",
  "😤",
  "😠",
  "😡",
  "🤬",
  "🤯",
  "😳",
  "🥵",
  "🥶",
  "😱",
  "😨",
  "😰",
  "😥",
  "😓",
  "🤗",
  "🤔",
  "🤭",
  "🤫",
  "😶",
  "😐",
  "😑",
  "😬",
  "🙄",
  "😯",
  "😧",
  "😮",
  "😲",
  "🥱",
  "😴",
  "🤤",
  "😪",
  "😵",
  "🤐",
  "🥴",
  "🤢",
  "🤮",
  "🤧",
  "😷",
  "🤒",
  "🤕",
  "🤑",
  "🤡",
  "💩",
  "👻",
  "💀",
  "☠️",
  "👽",
  "👾",
  "🤖",
  "👍",
  "👎",
  "👊",
  "✊",
  "🤛",
  "🤜",
  "🤞",
  "✌️",
  "🤟",
  "🤘",
  "👌",
  "🤌",
  "🤏",
  "👋",
  "✋",
  "🖖",
  "👏",
  "🙌",
  "🤲",
  "🤝",
  "🙏",
  "💪",
  "👂",
  "👃",
  "❤️",
  "🧡",
  "💛",
  "💚",
  "💙",
  "💜",
  "🖤",
  "🤍",
  "🤎",
  "💯",
  "💢",
  "💥",
  "💫",
  "💦",
  "💨",
  "💣",
  "💬",
  "🗨",
  "🗯",
  "💭",
  "💤",
  "✨",
  "⭐",
  "🌟",
  "💫",
]);

const emit = defineEmits(["select", "close"]);

/**
 * 选择表情
 * @param {string} emoji 选中的表情
 */
const selectEmoji = (emoji) => {
  emit("select", emoji);
};

/**
 * 点击外部区域关闭表情选择器
 */
const handleClickOutside = (event) => {
  if (pickerRef.value && !pickerRef.value.contains(event.target)) {
    emit("close");
  }
};

onMounted(() => {
  // 添加点击外部区域的监听，使用捕获阶段以确保先执行
  nextTick(() => {
    document.addEventListener("click", handleClickOutside, true);
  });
});

onUnmounted(() => {
  // 移除监听
  document.removeEventListener("click", handleClickOutside, true);
});
</script>

<style lang="scss" scoped>
.emoji-picker-container {
  position: relative;
  z-index: 100; // 根据开发规范使用合适的 z-index
}

.emoji-picker {
  width: 320px;
  max-height: 400px;
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.98) 0%,
    rgba(243, 229, 245, 0.98) 100%
  );
  border-radius: 16px;
  border: 2px solid rgba(179, 157, 219, 0.3);
  box-shadow: 0 8px 24px rgba(179, 157, 219, 0.25);
  backdrop-filter: blur(10px);
  overflow: hidden;
  animation: fadeInUp 0.3s ease-out;

  .emoji-header {
    padding: 12px 16px;
    border-bottom: 1px solid rgba(179, 157, 219, 0.2);
    background: linear-gradient(
      135deg,
      rgba(179, 157, 219, 0.1) 0%,
      rgba(159, 168, 218, 0.1) 100%
    );

    .picker-title {
      font-size: 14px;
      font-weight: 600;
      color: #7e57c2;
    }
  }

  .emoji-list {
    display: grid;
    grid-template-columns: repeat(8, 1fr);
    gap: 4px;
    padding: 12px;
    max-height: 350px;
    overflow-y: auto;

    // 自定义滚动条
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(179, 157, 219, 0.05);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(179, 157, 219, 0.3);
      border-radius: 3px;

      &:hover {
        background: rgba(179, 157, 219, 0.5);
      }
    }

    .emoji-item {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 36px;
      height: 36px;
      font-size: 24px;
      cursor: pointer;
      border-radius: 8px;
      transition: all 0.2s;
      user-select: none;

      &:hover {
        background: linear-gradient(
          135deg,
          rgba(179, 157, 219, 0.2) 0%,
          rgba(159, 168, 218, 0.2) 100%
        );
        transform: scale(1.2);
      }

      &:active {
        transform: scale(1.1);
      }
    }
  }
}

// 淡入上升动画
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
</style>
