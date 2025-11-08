<template>
  <transition name="back-to-top-fade">
    <div
      v-show="visible"
      class="back-to-top-btn"
      :style="{ zIndex: zIndex, bottom: bottom, right: right }"
      @click="scrollToTop"
      :title="title"
    >
      <component :is="icon" class="back-to-top-icon" />
    </div>
  </transition>
</template>

<script setup lang="ts">
import {onMounted, onUnmounted, ref } from "vue";
import { Top } from "@element-plus/icons-vue";

/**
 * 回到顶部组件属性
 */
interface Props {
  /**
   * 滚动多少像素后显示按钮
   * @default 300
   */
  visibilityHeight?: number;
  /**
   * z-index 层级
   * @default 600
   */
  zIndex?: number;
  /**
   * 距离底部的距离
   * @default '40px'
   */
  bottom?: string;
  /**
   * 距离右侧的距离
   * @default '40px'
   */
  right?: string;
  /**
   * 按钮图标组件
   * @default Top (Element Plus 图标)
   */
  icon?: Component;
  /**
   * 鼠标悬停提示文字
   * @default '回到顶部'
   */
  title?: string;
}

/**
 * 组件事件
 */
interface Emits {
  /**
   * 点击回到顶部时触发
   */
  (e: "click"): void;
  /**
   * 显示状态改变时触发
   * @param visible 是否可见
   */
  (e: "visibilityChange", visible: boolean): void;
}

// 定义 props 和默认值
const props = withDefaults(defineProps<Props>(), {
  visibilityHeight: 300,
  zIndex: 600,
  bottom: "40px",
  right: "40px",
  icon: () => Top,
  title: "回到顶部",
});

// 定义 emits
const emit = defineEmits<Emits>();

// 按钮是否可见
const visible = ref(false);

/**
 * 处理滚动事件
 * 当滚动距离超过 visibilityHeight 时显示按钮
 */
const handleScroll = () => {
  const scrollTop =
    window.pageYOffset ||
    document.documentElement.scrollTop ||
    document.body.scrollTop;

  const newVisible = scrollTop > props.visibilityHeight;

  // 只在状态改变时触发事件和更新状态
  if (newVisible !== visible.value) {
    visible.value = newVisible;
    emit("visibilityChange", newVisible);
  }
};

/**
 * 回到顶部
 * 使用平滑滚动效果
 */
const scrollToTop = () => {
  emit("click");

  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

// 生命周期：挂载时添加滚动监听
onMounted(() => {
  window.addEventListener("scroll", handleScroll, { passive: true });
});

// 生命周期：卸载时移除滚动监听
onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped lang="scss">
// 回到顶部按钮样式
.back-to-top-btn {
  position: fixed;
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  // 添加边框装饰
  border: 3px solid rgba(255, 255, 255, 0.3);

  // 悬停效果
  &:hover {
    transform: translateY(-5px) scale(1.1);
    box-shadow: 0 12px 32px rgba(102, 126, 234, 0.6);
    background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);

    .back-to-top-icon {
      animation: rocket-fly 0.6s ease-in-out;
    }
  }

  // 点击效果
  &:active {
    transform: translateY(-3px) scale(1.05);
  }

  // 图标样式
  .back-to-top-icon {
    width: 28px;
    height: 28px;
    color: #ffffff;
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
  }
}

// 按钮淡入淡出动画
.back-to-top-fade-enter-active,
.back-to-top-fade-leave-active {
  transition: all 0.3s ease;
}

.back-to-top-fade-enter-from {
  opacity: 0;
  transform: translateY(20px) scale(0.8);
}

.back-to-top-fade-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.8);
}

// 图标向上飞行动画
@keyframes rocket-fly {
  0% {
    transform: translateY(0);
  }
  25% {
    transform: translateY(-6px);
  }
  50% {
    transform: translateY(-10px);
  }
  75% {
    transform: translateY(-6px);
  }
  100% {
    transform: translateY(0);
  }
}

// 响应式设计 - 移动端适配
@media (max-width: 576px) {
  .back-to-top-btn {
    width: 48px;
    height: 48px;

    .back-to-top-icon {
      width: 24px;
      height: 24px;
    }
  }
}
</style>
