<template>
  <div class="table-of-contents">
    <!-- 标题 -->
    <div class="toc-header">
      <Icon icon="ri:list-ordered" class="toc-icon" />
      <h3 class="toc-title">目录</h3>
    </div>

    <!-- 目录列表 -->
    <div class="toc-content" v-if="headings.length > 0">
      <div
        v-for="(heading, index) in headings"
        :key="index"
        :class="[
          'toc-item',
          `level-${heading.level}`,
          { active: activeId === heading.id },
        ]"
        @click="scrollToHeading(heading.id)"
      >
        <span class="toc-bullet">{{ getBullet(heading.level) }}</span>
        <span class="toc-text">{{ heading.text }}</span>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="toc-empty">
      <Icon icon="ri:file-list-3-line" class="empty-icon" />
      <p class="empty-text">暂无目录</p>
      <p class="empty-hint">添加标题后自动生成</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onBeforeUnmount } from "vue";
import { Icon } from "@iconify/vue";

// 定义 props
const props = defineProps<{
  html: string;
}>();

// 标题数据结构
interface Heading {
  id: string;
  level: number;
  text: string;
}

// 标题列表
const headings = ref<Heading[]>([]);
// 当前激活的标题ID
const activeId = ref<string>("");

/**
 * 解析HTML内容，提取标题
 */
const parseHeadings = () => {
  if (!props.html) {
    headings.value = [];
    return;
  }

  const parser = new DOMParser();
  const doc = parser.parseFromString(props.html, "text/html");
  const headingElements = doc.querySelectorAll("h1, h2, h3, h4, h5, h6");

  const newHeadings: Heading[] = [];
  headingElements.forEach((element, index) => {
    const level = parseInt(element.tagName.substring(1));
    const text = element.textContent?.trim() || "";

    if (text) {
      // 生成唯一ID
      const id = `heading-${level}-${index}`;

      // 为实际DOM元素添加ID（用于跳转）
      const actualElement = document.querySelector(
        `.ProseMirror ${element.tagName}:nth-of-type(${index + 1})`
      );
      if (actualElement && !actualElement.id) {
        actualElement.id = id;
      }

      newHeadings.push({ id, level, text });
    }
  });

  headings.value = newHeadings;
};

/**
 * 滚动到指定标题
 */
const scrollToHeading = (id: string) => {
  // 尝试多种方式查找元素
  let element = document.getElementById(id);

  if (!element) {
    // 如果通过ID找不到，尝试通过选择器查找
    const match = id.match(/heading-(\d+)-(\d+)/);
    if (match) {
      const level = match[1];
      const index = parseInt(match[2]);
      element = document.querySelector(
        `.ProseMirror h${level}:nth-of-type(${index + 1})`
      ) as HTMLElement;
      if (element && !element.id) {
        element.id = id;
      }
    }
  }

  if (element) {
    activeId.value = id;
    element.scrollIntoView({ behavior: "smooth", block: "start" });

    // 添加高亮动画
    element.classList.add("heading-highlight");
    setTimeout(() => {
      element?.classList.remove("heading-highlight");
    }, 2000);
  }
};

/**
 * 根据标题级别返回不同的图标
 */
const getBullet = (level: number): string => {
  const bullets: { [key: number]: string } = {
    1: "★",
    2: "◆",
    3: "●",
    4: "◇",
    5: "○",
    6: "▪",
  };
  return bullets[level] || "•";
};

/**
 * 监听滚动，更新当前激活的标题
 */
const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop;

  // 找到当前视口中的标题
  for (let i = headings.value.length - 1; i >= 0; i--) {
    const heading = headings.value[i];
    const element = document.getElementById(heading.id);

    if (element) {
      const rect = element.getBoundingClientRect();
      const elementTop = rect.top + scrollTop;

      if (scrollTop >= elementTop - 100) {
        activeId.value = heading.id;
        break;
      }
    }
  }
};

// 监听HTML内容变化
watch(
  () => props.html,
  () => {
    // 延迟解析，确保DOM已更新
    setTimeout(parseHeadings, 100);
  },
  { immediate: true }
);

// 挂载时解析标题并添加滚动监听
onMounted(() => {
  parseHeadings();
  window.addEventListener("scroll", handleScroll);
});

// 卸载时移除滚动监听
onBeforeUnmount(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style lang="scss" scoped>
.table-of-contents {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(103, 58, 183, 0.12);
  border: 2px solid rgba(103, 58, 183, 0.15);
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    box-shadow: 0 12px 48px rgba(103, 58, 183, 0.18);
    border-color: rgba(103, 58, 183, 0.25);
  }
}

.toc-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 20px;
  background: linear-gradient(
    135deg,
    rgba(227, 242, 253, 0.8) 0%,
    rgba(243, 229, 245, 0.8) 100%
  );
  border-bottom: 2px solid rgba(103, 58, 183, 0.15);

  .toc-icon {
    font-size: 24px;
    color: #5e35b1;
    animation: bounce 2s ease-in-out infinite;
  }

  .toc-title {
    margin: 0;
    font-size: 17px;
    font-weight: 700;
    color: #5e35b1;
    letter-spacing: 0.5px;
  }
}

.toc-content {
  padding: 12px 0;
  max-height: calc(100vh - 250px);
  overflow-y: auto;
  overflow-x: hidden;

  // 自定义滚动条
  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: rgba(179, 157, 219, 0.1);
    border-radius: 3px;
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(180deg, #a18cd1 0%, #fbc2eb 100%);
    border-radius: 3px;

    &:hover {
      background: linear-gradient(180deg, #fbc2eb 0%, #a18cd1 100%);
    }
  }
}

.toc-item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;

  &::before {
    content: "";
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 3px;
    height: 0;
    background: linear-gradient(180deg, #a18cd1 0%, #fbc2eb 100%);
    border-radius: 0 3px 3px 0;
    transition: height 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  &:hover {
    background: rgba(103, 58, 183, 0.05);
    transform: translateX(4px);

    &::before {
      height: 80%;
    }

    .toc-bullet {
      transform: scale(1.2) rotate(360deg);
    }
  }

  &.active {
    background: rgba(103, 58, 183, 0.1);

    &::before {
      height: 100%;
    }

    .toc-text {
      color: #5e35b1;
      font-weight: 600;
    }

    .toc-bullet {
      color: #5e35b1;
      transform: scale(1.3);
    }
  }

  .toc-bullet {
    flex-shrink: 0;
    margin-right: 10px;
    font-size: 14px;
    color: #9575cd;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .toc-text {
    flex: 1;
    font-size: 14px;
    color: #424242;
    line-height: 1.5;
    word-break: break-word;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  // 不同级别的缩进
  &.level-1 {
    padding-left: 20px;
    .toc-text {
      font-weight: 600;
      font-size: 15px;
    }
  }

  &.level-2 {
    padding-left: 35px;
  }

  &.level-3 {
    padding-left: 50px;
    .toc-text {
      font-size: 13px;
    }
  }

  &.level-4 {
    padding-left: 65px;
    .toc-text {
      font-size: 13px;
      color: #616161;
    }
  }

  &.level-5 {
    padding-left: 80px;
    .toc-text {
      font-size: 12px;
      color: #757575;
    }
  }

  &.level-6 {
    padding-left: 95px;
    .toc-text {
      font-size: 12px;
      color: #9e9e9e;
    }
  }
}

// 空状态样式
.toc-empty {
  padding: 40px 20px;
  text-align: center;
  color: #9e9e9e;

  .empty-icon {
    font-size: 48px;
    color: #d1c4e9;
    margin-bottom: 16px;
    animation: float 3s ease-in-out infinite;
  }

  .empty-text {
    margin: 0 0 8px 0;
    font-size: 15px;
    font-weight: 500;
    color: #757575;
  }

  .empty-hint {
    margin: 0;
    font-size: 13px;
    color: #bdbdbd;
  }
}

// 弹跳动画
@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

// 浮动动画
@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .toc-content {
    max-height: 400px;
  }
}

@media (max-width: 768px) {
  .toc-header {
    padding: 12px 16px;

    .toc-icon {
      font-size: 20px;
    }

    .toc-title {
      font-size: 16px;
    }
  }

  .toc-item {
    padding: 8px 16px;

    .toc-text {
      font-size: 13px;
    }

    // 移动端减少缩进
    &.level-1 {
      padding-left: 16px;
    }
    &.level-2 {
      padding-left: 28px;
    }
    &.level-3 {
      padding-left: 40px;
    }
    &.level-4 {
      padding-left: 52px;
    }
    &.level-5 {
      padding-left: 64px;
    }
    &.level-6 {
      padding-left: 76px;
    }
  }

  .toc-content {
    max-height: 300px;
  }
}
</style>

<style lang="scss">
// 标题高亮动画（全局样式）
.heading-highlight {
  animation: headingPulse 0.6s ease-out;
  background: linear-gradient(
    90deg,
    rgba(103, 58, 183, 0.15) 0%,
    transparent 100%
  );
  padding-left: 12px;
  margin-left: -12px;
  border-left: 3px solid #a18cd1;
}

@keyframes headingPulse {
  0% {
    background-color: rgba(103, 58, 183, 0.3);
  }
  100% {
    background-color: rgba(103, 58, 183, 0);
  }
}
</style>
