<template>
  <div class="cover-section">
    <div class="cover-container">
      <h3>文章封面</h3>
      <div class="cover-actions">
        <button @click="emit('select')" class="cover-button">
          {{ coverUrl ? "更换封面" : "选择封面" }}
        </button>
        <button
          v-if="coverUrl"
          @click="emit('remove')"
          class="cover-button remove-button"
        >
          移除封面
        </button>
      </div>
      <div v-if="coverUrl" class="cover-preview">
        <img :src="coverUrl" alt="封面预览" class="cover-image" />
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  coverUrl: {
    type: String,
    default: "",
  },
});

const emit = defineEmits(["select", "remove"]);
</script>

<style scoped lang="scss">
// 动漫风格封面区域
.cover-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 280px;
  padding: 20px;
  background: linear-gradient(
    135deg,
    rgba(232, 245, 233, 0.6) 0%,
    rgba(243, 229, 245, 0.6) 50%,
    rgba(227, 242, 253, 0.6) 100%
  );
  border-radius: 0 0 16px 16px;
  box-shadow: 0 4px 20px rgba(103, 58, 183, 0.1);
  border: 2px solid rgba(156, 39, 176, 0.15);
  border-top: none;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;

  // 装饰性星星
  &::before {
    content: "⭐";
    position: absolute;
    top: 20px;
    right: 30px;
    font-size: 28px;
    opacity: 0.4;
    animation: twinkle 2s ease-in-out infinite;
  }

  &::after {
    content: "✨";
    position: absolute;
    bottom: 30px;
    left: 40px;
    font-size: 24px;
    opacity: 0.4;
    animation: twinkle 2.5s ease-in-out infinite reverse;
  }

  &:hover {
    box-shadow: 0 12px 48px rgba(103, 58, 183, 0.18);
    transform: translateY(-4px);
    border-color: rgba(156, 39, 176, 0.25);
  }
}

.cover-container {
  width: 100%;
  text-align: center;
  padding: 0;
  background: transparent;
  position: relative;
  z-index: 1;
}

h3 {
  display: none; // 标题已在外部section-header显示
}

.cover-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.cover-button {
  padding: 10px 20px;
  border: none;
  border-radius: 14px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 14px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
  color: #ffffff;
  box-shadow: 0 4px 16px rgba(161, 140, 209, 0.3);
  position: relative;
  overflow: hidden;
  white-space: nowrap;

  // 波纹效果
  &::before {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.4);
    transform: translate(-50%, -50%);
    transition: width 0.6s, height 0.6s;
  }

  &:hover {
    transform: translateY(-4px) scale(1.05);
    box-shadow: 0 10px 30px rgba(161, 140, 209, 0.4);

    &::before {
      width: 300px;
      height: 300px;
    }
  }

  &:active {
    transform: translateY(-2px) scale(1.02);
  }
}

.remove-button {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  box-shadow: 0 6px 20px rgba(250, 112, 154, 0.3);

  &:hover {
    box-shadow: 0 10px 30px rgba(250, 112, 154, 0.4);
  }
}

.cover-preview {
  margin: 20px auto 0;
  max-width: 100%;
  position: relative;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);

  // 可爱的相框效果
  &::before {
    content: "";
    position: absolute;
    top: -6px;
    left: -6px;
    right: -6px;
    bottom: -6px;
    background: linear-gradient(45deg, #667eea, #764ba2, #f093fb, #4facfe);
    background-size: 400% 400%;
    border-radius: 14px;
    z-index: -1;
    opacity: 0.4;
    animation: gradientShift 3s ease infinite;
  }
}

.cover-image {
  max-width: 100%;
  max-height: 280px;
  border-radius: 12px;
  border: 3px solid #ffffff;
  box-shadow: 0 6px 24px rgba(103, 58, 183, 0.2);
  display: block;
  margin: 0 auto;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    transform: scale(1.02);
    box-shadow: 0 8px 32px rgba(103, 58, 183, 0.3);
  }
}

// 闪烁动画
@keyframes twinkle {
  0%,
  100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.2);
  }
}

// 脉冲动画
@keyframes pulse {
  0%,
  100% {
    opacity: 1;
    transform: translateX(-50%) scaleX(1);
  }
  50% {
    opacity: 0.7;
    transform: translateX(-50%) scaleX(1.1);
  }
}

// 渐变移动动画
@keyframes gradientShift {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .cover-section {
    padding: 16px;
    min-height: 240px;
  }

  .cover-actions {
    gap: 10px;
  }

  .cover-button {
    padding: 8px 16px;
    font-size: 13px;
  }

  .cover-image {
    max-height: 240px;
  }
}

@media (max-width: 576px) {
  .cover-section {
    min-height: 200px;
    padding: 12px;
  }

  .cover-actions {
    flex-direction: column;
    width: 100%;
  }

  .cover-button {
    width: 100%;
  }

  .cover-image {
    max-height: 200px;
  }
}
</style>
