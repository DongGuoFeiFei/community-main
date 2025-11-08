<template>
  <div class="comment-form" :class="{ 'is-focused': formState.focused.value }">
    <div class="form-header" v-if="showHeader">
      <span class="form-title">{{ title }}</span>
    </div>

    <div class="form-body">
      <el-input
        v-model="formState.content.value"
        type="textarea"
        :rows="rows"
        :placeholder="formState.placeholder"
        :maxlength="formState.maxLength"
        :show-word-limit="false"
        resize="none"
        class="comment-textarea"
        @focus="formState.focus"
        @blur="formState.blur"
      />

      <div class="form-toolbar">
        <div class="toolbar-left">
          <!-- 可以在这里添加表情选择器等功能 -->
          <span class="emoji-hint">💭</span>
        </div>

        <div class="toolbar-right">
          <span
            class="char-count"
            :style="{ color: formState.charCountColor.value }"
            :class="{ 'is-over-limit': formState.isOverLimit.value }"
          >
            {{ formState.charCount.value }} / {{ formState.maxLength }}
          </span>
        </div>
      </div>
    </div>

    <div class="form-actions">
      <el-button
        v-if="showCancel"
        size="small"
        @click="handleCancel"
        class="cancel-btn"
      >
        取消
      </el-button>

      <el-button
        type="primary"
        size="small"
        :loading="formState.submitting.value"
        :disabled="!formState.canSubmit.value"
        @click="handleSubmit"
        class="submit-btn"
      >
        <template #icon>
          <el-icon><Promotion /></el-icon>
        </template>
        {{ submitText }}
      </el-button>
    </div>

    <!-- 粒子效果装饰 -->
    <div class="form-decoration">
      <span class="particle particle-1">✨</span>
      <span class="particle particle-2">💖</span>
      <span class="particle particle-3">⭐</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Promotion } from "@element-plus/icons-vue";
import { useCommentForm } from "@/composables/useCommentForm";

interface Props {
  title?: string;
  placeholder?: string;
  maxLength?: number;
  rows?: number;
  submitText?: string;
  showCancel?: boolean;
  showHeader?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  title: "发表评论",
  placeholder: "写下你的评论...",
  maxLength: 500,
  rows: 3,
  submitText: "发表评论",
  showCancel: false,
  showHeader: false,
});

const emit = defineEmits<{
  submit: [content: string];
  cancel: [];
}>();

/**
 * 使用评论表单组合式函数
 */
const formState = useCommentForm({
  maxLength: props.maxLength,
  placeholder: props.placeholder,
  onSubmit: async (content: string) => {
    emit("submit", content);
    return true;
  },
});

/**
 * 处理提交
 */
const handleSubmit = async () => {
  await formState.handleSubmit();
};

/**
 * 处理取消
 */
const handleCancel = () => {
  formState.reset();
  emit("cancel");
};
</script>

<style scoped lang="scss">
.comment-form {
  position: relative;
  padding: 20px;
  background: linear-gradient(
    135deg,
    rgba(255, 240, 245, 0.5),
    rgba(240, 230, 255, 0.5)
  );
  border-radius: 20px;
  border: 2px solid rgba(255, 182, 193, 0.2);
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 2px;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(255, 182, 193, 0.8),
      transparent
    );
    transition: left 0.6s ease;
  }

  &.is-focused {
    border-color: rgba(255, 182, 193, 0.5);
    box-shadow: 0 8px 25px rgba(255, 182, 193, 0.25),
      0 0 0 3px rgba(255, 182, 193, 0.1),
      inset 0 2px 10px rgba(255, 255, 255, 0.8);
    transform: translateY(-2px);

    &::before {
      left: 100%;
    }

    .particle {
      animation: particleFloat 2s infinite ease-in-out;
    }
  }
}

.form-header {
  margin-bottom: 15px;

  .form-title {
    font-size: 16px;
    font-weight: 700;
    background: linear-gradient(135deg, #ff6b9d, #c9a0dc);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }
}

.form-body {
  margin-bottom: 15px;
}

.comment-textarea {
  :deep(.el-textarea__inner) {
    border-radius: 15px;
    border: 2px solid rgba(255, 182, 193, 0.25);
    background: rgba(255, 255, 255, 0.95);
    transition: all 0.3s ease;
    font-size: 14px;
    line-height: 1.6;
    padding: 12px 15px;
    color: #555;

    &::placeholder {
      color: #bbb;
    }

    &:hover {
      border-color: rgba(255, 182, 193, 0.4);
    }

    &:focus {
      border-color: #ffb6c1;
      box-shadow: 0 0 0 3px rgba(255, 182, 193, 0.15),
        inset 0 2px 5px rgba(255, 182, 193, 0.1);
      background: rgba(255, 255, 255, 1);
    }
  }
}

.form-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  padding: 0 5px;
}

.toolbar-left {
  .emoji-hint {
    font-size: 18px;
    opacity: 0.4;
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      opacity: 0.8;
      transform: scale(1.2) rotate(10deg);
    }
  }
}

.toolbar-right {
  .char-count {
    font-size: 12px;
    font-weight: 600;
    transition: all 0.3s ease;

    &.is-over-limit {
      animation: shake 0.5s ease;
    }
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
}

.cancel-btn,
.submit-btn {
  border-radius: 20px !important;
  padding: 10px 20px;
  font-weight: 600;
  font-size: 13px;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    top: 50%;
    left: 50%;
    width: 0;
    height: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.3);
    transform: translate(-50%, -50%);
    transition: width 0.6s ease, height 0.6s ease;
  }

  &:hover::before {
    width: 300px;
    height: 300px;
  }

  &:active {
    transform: scale(0.95);
  }
}

.cancel-btn {
  background: rgba(255, 255, 255, 0.8);
  border: 2px solid rgba(200, 200, 200, 0.3);
  color: #666;

  &:hover {
    border-color: rgba(200, 200, 200, 0.5);
    background: rgba(255, 255, 255, 0.95);
  }
}

.submit-btn {
  background: linear-gradient(135deg, #ffb6c1, #c9a0dc);
  border: none;
  box-shadow: 0 6px 15px rgba(255, 182, 193, 0.3);

  &:hover:not(:disabled) {
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 10px 25px rgba(255, 182, 193, 0.4);
  }

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .el-icon {
    transition: transform 0.3s ease;
  }

  &:hover:not(:disabled) .el-icon {
    transform: translateX(3px);
  }
}

// 粒子装饰
.form-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  overflow: hidden;
}

.particle {
  position: absolute;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.3s ease;

  .is-focused & {
    opacity: 0.3;
  }
}

.particle-1 {
  top: 15px;
  right: 15px;
  animation-delay: 0s;
}

.particle-2 {
  top: 50%;
  right: 10px;
  animation-delay: 0.5s;
}

.particle-3 {
  bottom: 15px;
  right: 20px;
  animation-delay: 1s;
}

// 动画
@keyframes particleFloat {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-10px) rotate(180deg);
  }
}

@keyframes shake {
  0%,
  100% {
    transform: translateX(0);
  }
  25% {
    transform: translateX(-5px);
  }
  75% {
    transform: translateX(5px);
  }
}
</style>
