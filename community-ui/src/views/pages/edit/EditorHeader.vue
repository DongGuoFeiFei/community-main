<template>
  <div class="editor-header">
    <button @click="emit('back')" class="back-button">← 返回</button>
    <input
      v-model="title"
      type="text"
      placeholder="输入文章标题"
      class="title-input"
      @input="updateTitle"
    />
    <el-button-group>
      <el-button type="primary" @click="emit('save', 1)">{{
        isEditMode ? "更新草稿" : "保存草稿"
      }}</el-button>
      <el-button type="success" @click="emit('save', 0)">{{
        isEditMode ? "更新文章" : "发布文章"
      }}</el-button>
    </el-button-group>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  isEditMode: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["back", "save", "update:title"]);

const title = ref(props.title);

watch(
  () => props.title,
  (newVal) => {
    title.value = newVal;
  }
);

const updateTitle = () => {
  emit("update:title", title.value);
};
</script>

<style scoped lang="scss">
// 动漫风格编辑器头部
.editor-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  gap: 15px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  padding: 16px 20px;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(142, 68, 173, 0.15),
    0 0 0 1px rgba(142, 68, 173, 0.1);
  border: 2px solid rgba(142, 68, 173, 0.2);
  position: relative;
  overflow: hidden;
  animation: slideInDown 0.5s ease-out;

  // 彩虹渐变装饰条
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(
      90deg,
      #667eea 0%,
      #764ba2 25%,
      #f093fb 50%,
      #4facfe 75%,
      #00f2fe 100%
    );
    animation: rainbow 3s linear infinite;
  }

  // 悬停效果
  &:hover {
    box-shadow: 0 8px 30px rgba(142, 68, 173, 0.25),
      0 0 0 2px rgba(142, 68, 173, 0.2);
    transform: translateY(-2px);
  }
}

// 标题输入框
.title-input {
  flex: 1;
  padding: 14px 20px;
  border: 2px solid rgba(103, 58, 183, 0.2);
  border-radius: 16px;
  font-size: 18px;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.9);
  color: #5e35b1;
  box-shadow: 0 2px 8px rgba(103, 58, 183, 0.08);

  &::placeholder {
    color: #b39ddb;
    font-weight: 400;
  }

  &:focus {
    border-color: #7e57c2;
    outline: none;
    background: #ffffff;
    box-shadow: 0 4px 16px rgba(126, 87, 194, 0.2),
      0 0 0 4px rgba(126, 87, 194, 0.1);
    transform: translateY(-1px);
  }

  // 可爱的闪烁光标效果
  &:focus::placeholder {
    opacity: 0;
  }
}

// 返回按钮
.back-button {
  padding: 12px 20px;
  border: none;
  border-radius: 14px;
  cursor: pointer;
  font-size: 15px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  position: relative;
  overflow: hidden;

  // 按钮悬停波纹效果
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
    transition: width 0.6s, height 0.6s;
  }

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);

    &::before {
      width: 300px;
      height: 300px;
    }
  }

  &:active {
    transform: translateY(-1px);
  }
}

// Element Plus 按钮组样式覆盖
:deep(.el-button-group) {
  .el-button {
    border-radius: 14px;
    padding: 12px 24px;
    font-weight: 600;
    font-size: 15px;
    border: none;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
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
      transition: width 0.6s, height 0.6s;
    }

    &:hover::before {
      width: 300px;
      height: 300px;
    }

    &:first-child {
      margin-right: 10px;
    }

    &.el-button--primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
      }
    }

    &.el-button--success {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      box-shadow: 0 4px 12px rgba(79, 172, 254, 0.3);

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 20px rgba(79, 172, 254, 0.4);
      }
    }

    &:active {
      transform: translateY(-1px);
    }
  }
}

// 滑入动画
@keyframes slideInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 彩虹流动动画
@keyframes rainbow {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .editor-header {
    flex-wrap: wrap;
    padding: 12px 16px;
    gap: 12px;
  }

  .title-input {
    order: 1;
    width: 100%;
    margin-top: 10px;
    font-size: 16px;
  }

  .back-button {
    font-size: 14px;
    padding: 10px 16px;
  }

  :deep(.el-button-group .el-button) {
    padding: 10px 18px;
    font-size: 14px;
  }
}
</style>
