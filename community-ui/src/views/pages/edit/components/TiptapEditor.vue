<template>
  <div class="editor-wrapper">
    <!-- 左侧目录 -->
    <div v-if="showToc" class="editor-toc-sidebar">
      <TableOfContents :html="editorHtml" />
    </div>

    <!-- 编辑器主体 -->
    <div
      class="editor-container"
      :style="{ width: showToc ? 'calc(100% - 300px)' : width }"
    >
      <div class="editor" v-if="editor">
        <MenuBar
          v-if="editor"
          class="editor-header"
          :editor="editor"
          :upload-image="handleImageUpload"
          :show-toc="showToc"
          @toggle-toc="toggleToc"
        />
        <editor-content class="editor-content" :editor="editor" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { EditorContent, useEditor } from "@tiptap/vue-3";
import StarterKit from "@tiptap/starter-kit";
import { onBeforeUnmount, watch, onMounted, ref } from "vue";
import MenuBar from "./MenuBar.vue";
import TableOfContents from "./TableOfContents.vue";
import Highlight from "@tiptap/extension-highlight";
import Image from "@tiptap/extension-image";
import { Table } from "@tiptap/extension-table";
import { TableRow } from "@tiptap/extension-table-row";
import { TableCell } from "@tiptap/extension-table-cell";
import { TableHeader } from "@tiptap/extension-table-header";
import { uploadFile } from "@/api/files.js";
import env from "@/utils/env.js";

const props = defineProps({
  html: {
    type: String,
    default: "",
  },
  width: {
    type: String,
    default: "100%",
  },
  minHeight: {
    type: String,
    default: "100px",
  },
  // 是否显示目录
  showTableOfContents: {
    type: Boolean,
    default: true,
  },
});

// 目录显示状态
const showToc = ref(props.showTableOfContents);

// 编辑器HTML内容（用于传递给目录组件）
const editorHtml = ref(props.html);

const emit = defineEmits(["update:html"]);

const CustomTableCell = TableCell.extend({
  addAttributes() {
    return {
      ...this.parent?.(),
      backgroundColor: {
        default: null,
        parseHTML: (element) => element.getAttribute("data-background-color"),
        renderHTML: (attributes) => ({
          "data-background-color": attributes.backgroundColor,
          style: `background-color: ${attributes.backgroundColor}`,
        }),
      },
    };
  },
});

const editor = useEditor({
  content: props.html,
  extensions: [
    StarterKit,
    Image.configure({
      allowBase64: false,
    }),
    Highlight.configure({ multicolor: true }),
    Table.configure({
      resizable: true,
    }),
    TableRow,
    TableHeader,
    CustomTableCell,
  ],
  onUpdate: () => {
    const html = editor.value.getHTML();
    editorHtml.value = html;
    emit("update:html", html);
    adjustEditorHeight();
  },
});

/**
 * 切换目录显示状态
 */
const toggleToc = () => {
  showToc.value = !showToc.value;
};

/**
 * 调整编辑器高度
 */
const adjustEditorHeight = () => {
  const editorElement = document.querySelector(".editor-content");
  if (!editorElement) return;

  // 重置高度为auto以获取实际内容高度
  editorElement.style.height = "auto";

  // 获取内容实际高度
  const contentHeight = editorElement.scrollHeight;

  // 设置新的高度，确保不小于最小高度
  editorElement.style.height = `${Math.max(
    contentHeight,
    parseInt(props.minHeight)
  )}px`;
};

const handleImageUpload = async (file) => {
  try {
    const url = await uploadFile(file);
    editor.value
      .chain()
      .focus()
      .setImage({ src: env.apiBaseUrl + url })
      .run();
    // 图片加载完成后调整高度
    setTimeout(adjustEditorHeight, 300);
  } catch (error) {
    console.error("图片上传失败:", error);
  }
};

// 监听 showTableOfContents prop 的变化
watch(
  () => props.showTableOfContents,
  (newValue) => {
    showToc.value = newValue;
  }
);

watch(
  () => props.html,
  (newValue) => {
    editorHtml.value = newValue;
    if (editor.value && newValue !== editor.value.getHTML()) {
      editor.value.commands.setContent(newValue, false);
      // 内容更新后调整高度
      setTimeout(adjustEditorHeight, 100);
    }
  },
  { immediate: true }
);

onMounted(() => {
  // 初始高度调整
  setTimeout(adjustEditorHeight, 300);

  // 添加resize观察器
  const resizeObserver = new ResizeObserver(() => {
    adjustEditorHeight();
  });

  const editorContent = document.querySelector(".editor-content");
  if (editorContent) {
    resizeObserver.observe(editorContent);
  }

  onBeforeUnmount(() => {
    resizeObserver.disconnect();
    if (editor.value) {
      editor.value.destroy();
    }
  });
});
</script>

<style lang="scss">
// 编辑器整体包装器
.editor-wrapper {
  display: flex;
  gap: 20px;
  width: 100%;
  animation: fadeIn 0.5s ease-out;
  position: relative;

  // 添加浮动装饰
  &::before {
    content: "✨";
    position: absolute;
    top: -15px;
    right: 10%;
    font-size: 20px;
    opacity: 0.4;
    animation: sparkle 3s ease-in-out infinite;
    pointer-events: none;
    z-index: 1;
  }
}

// 左侧目录栏
.editor-toc-sidebar {
  width: 280px;
  flex-shrink: 0;
  animation: slideInLeft 0.6s ease-out;
  align-self: flex-start;
  position: sticky;
  top: 20px;
  max-height: calc(100vh - 200px);
}

// 编辑器容器
.editor-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  min-width: 0;
  animation: slideInRight 0.6s ease-out;
}

.editor {
  display: flex;
  flex-direction: column;
  color: #2c3e50;
  background: linear-gradient(135deg, #ffffff 0%, #fafcff 100%);
  border: 3px solid transparent;
  background-clip: padding-box;
  border-radius: 20px;
  min-height: v-bind("props.minHeight");
  box-shadow: 0 8px 32px rgba(103, 58, 183, 0.15),
    0 2px 8px rgba(103, 58, 183, 0.1), inset 0 1px 0 rgba(255, 255, 255, 0.9);
  position: relative;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;

  // 渐变边框效果
  &::before {
    content: "";
    position: absolute;
    inset: 0;
    border-radius: 20px;
    padding: 3px;
    background: linear-gradient(
      135deg,
      #667eea 0%,
      #764ba2 25%,
      #f093fb 50%,
      #4facfe 75%,
      #00f2fe 100%
    );
    -webkit-mask: linear-gradient(#fff 0 0) content-box,
      linear-gradient(#fff 0 0);
    -webkit-mask-composite: xor;
    mask: linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0);
    mask-composite: exclude;
    pointer-events: none;
    opacity: 0.6;
  }

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 12px 48px rgba(103, 58, 183, 0.2),
      0 4px 16px rgba(103, 58, 183, 0.15),
      inset 0 1px 0 rgba(255, 255, 255, 0.9);

    &::before {
      opacity: 1;
      animation: borderRotate 3s linear infinite;
    }
  }

  &-header {
    display: flex;
    align-items: center;
    flex: 0 0 auto;
    flex-wrap: wrap;
    padding: 12px;
    background: linear-gradient(
      135deg,
      rgba(227, 242, 253, 0.5) 0%,
      rgba(243, 229, 245, 0.5) 100%
    );
    border-bottom: 2px solid rgba(103, 58, 183, 0.1);
    border-radius: 17px 17px 0 0;
    position: relative;
    z-index: 1;

    &::after {
      content: "";
      position: absolute;
      left: 20px;
      right: 20px;
      bottom: 0;
      height: 2px;
      background: linear-gradient(
        90deg,
        transparent 0%,
        #a18cd1 50%,
        transparent 100%
      );
      opacity: 0;
      transition: opacity 0.3s ease;
    }
  }

  &:hover &-header::after {
    opacity: 0.5;
  }

  &-content {
    padding: 24px 20px;
    flex: 1 1 auto;
    overflow-x: hidden;
    overflow-y: hidden;
    transition: height 0.15s ease-out;
    position: relative;
    z-index: 1;
  }
}

// 动画定义
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes sparkle {
  0%,
  100% {
    opacity: 0.3;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2) rotate(180deg);
  }
}

@keyframes borderRotate {
  0% {
    filter: hue-rotate(0deg);
  }
  100% {
    filter: hue-rotate(360deg);
  }
}

// 响应式设计
@media (max-width: 1400px) {
  .editor-toc-sidebar {
    width: 240px;
  }
}

@media (max-width: 1200px) {
  .editor-wrapper {
    flex-direction: column;
  }

  .editor-toc-sidebar {
    width: 100%;
    max-height: 400px;
    position: relative;
    top: 0;
  }
}

@media (max-width: 768px) {
  .editor-wrapper {
    gap: 16px;
  }

  .editor-toc-sidebar {
    max-height: 300px;
  }
}

@media (max-width: 576px) {
  .editor-wrapper {
    gap: 12px;
  }

  .editor-toc-sidebar {
    max-height: 250px;
  }
}

/* 基本编辑器样式 - 动漫风格美化 */
.ProseMirror {
  outline: none;
  min-height: calc(v-bind("props.minHeight") - 1.4rem);
  line-height: 1.8;
  color: #2c3e50;

  &:focus {
    outline: none;
  }

  // 段落样式
  p {
    margin: 0.75em 0;
    transition: all 0.2s ease;

    &:first-child {
      margin-top: 0;
    }

    &:last-child {
      margin-bottom: 0;
    }
  }

  // 标题样式 - 渐变色 + 装饰
  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    line-height: 1.3;
    font-weight: 700;
    margin: 1.5em 0 0.75em;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    position: relative;
    padding-left: 16px;
    transition: all 0.3s ease;

    &::before {
      content: "";
      position: absolute;
      left: 0;
      top: 50%;
      transform: translateY(-50%);
      width: 6px;
      height: 60%;
      border-radius: 3px;
      background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
      opacity: 0.6;
      transition: all 0.3s ease;
    }

    &:hover::before {
      width: 8px;
      opacity: 1;
    }
  }

  h1 {
    font-size: 2em;
    &::before {
      height: 70%;
    }
  }

  h2 {
    font-size: 1.75em;
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    &::before {
      background: linear-gradient(180deg, #f093fb 0%, #f5576c 100%);
    }
  }

  h3 {
    font-size: 1.5em;
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    &::before {
      background: linear-gradient(180deg, #4facfe 0%, #00f2fe 100%);
    }
  }

  h4 {
    font-size: 1.25em;
  }

  h5 {
    font-size: 1.1em;
  }

  h6 {
    font-size: 1em;
  }

  // 列表样式
  ul,
  ol {
    padding: 0 0 0 1.5rem;
    margin: 1em 0;

    li {
      margin: 0.5em 0;
      transition: all 0.2s ease;

      &::marker {
        color: #a18cd1;
        font-weight: bold;
      }

      p {
        margin: 0.25em 0;
      }
    }
  }

  ul {
    li {
      position: relative;
      list-style: none;
      padding-left: 1.5em;

      &::before {
        content: "◆";
        position: absolute;
        left: 0;
        color: #a18cd1;
        font-size: 0.8em;
        top: 0.4em;
      }
    }
  }

  // 行内代码
  code {
    background: linear-gradient(
      135deg,
      rgba(102, 126, 234, 0.1) 0%,
      rgba(118, 75, 162, 0.1) 100%
    );
    color: #764ba2;
    padding: 0.2em 0.4em;
    border-radius: 6px;
    font-family: "Consolas", "Monaco", monospace;
    font-size: 0.9em;
    border: 1px solid rgba(118, 75, 162, 0.2);
    transition: all 0.2s ease;

    &:hover {
      background: linear-gradient(
        135deg,
        rgba(102, 126, 234, 0.15) 0%,
        rgba(118, 75, 162, 0.15) 100%
      );
      border-color: rgba(118, 75, 162, 0.3);
    }
  }

  // 代码块
  pre {
    background: linear-gradient(135deg, #2d3561 0%, #1e2440 100%);
    color: #e1e4e8;
    font-family: "Consolas", "Monaco", monospace;
    padding: 1.25rem 1.5rem;
    border-radius: 12px;
    margin: 1.5em 0;
    overflow-x: auto;
    box-shadow: 0 8px 24px rgba(45, 53, 97, 0.3);
    border: 2px solid rgba(102, 126, 234, 0.2);
    position: relative;

    &::before {
      content: "";
      position: absolute;
      top: 12px;
      left: 12px;
      display: flex;
      gap: 6px;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: #ff5f56;
      box-shadow: 20px 0 0 #ffbd2e, 40px 0 0 #27c93f;
    }

    code {
      color: inherit;
      padding: 0;
      background: none;
      font-size: 0.9em;
      border: none;
      display: block;
      margin-top: 1em;
    }

    // 自定义滚动条
    &::-webkit-scrollbar {
      height: 8px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(255, 255, 255, 0.05);
      border-radius: 4px;
    }

    &::-webkit-scrollbar-thumb {
      background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
      border-radius: 4px;

      &:hover {
        background: linear-gradient(90deg, #764ba2 0%, #667eea 100%);
      }
    }
  }

  // 高亮文本
  mark {
    background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
    color: #2c3e50;
    padding: 0.1em 0.3em;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(253, 203, 110, 0.3);
    transition: all 0.2s ease;

    &:hover {
      background: linear-gradient(135deg, #fdcb6e 0%, #ffeaa7 100%);
      box-shadow: 0 4px 8px rgba(253, 203, 110, 0.4);
    }
  }

  // 图片样式
  img {
    max-width: 100%;
    height: auto;
    border-radius: 12px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    cursor: pointer;
    margin: 1em 0;

    &:hover {
      transform: scale(1.02);
      box-shadow: 0 12px 32px rgba(103, 58, 183, 0.2);
    }
  }

  // 引用块
  blockquote {
    position: relative;
    padding: 1.25rem 1.5rem 1.25rem 2rem;
    margin: 1.5em 0;
    background: linear-gradient(
      135deg,
      rgba(227, 242, 253, 0.5) 0%,
      rgba(243, 229, 245, 0.5) 100%
    );
    border-left: 4px solid transparent;
    border-image: linear-gradient(180deg, #667eea 0%, #764ba2 100%) 1;
    border-radius: 0 12px 12px 0;
    font-style: italic;
    color: #5e35b1;
    box-shadow: 0 4px 16px rgba(103, 58, 183, 0.1);
    transition: all 0.3s ease;

    &::before {
      content: '"';
      position: absolute;
      top: 10px;
      left: 10px;
      font-size: 2em;
      color: #a18cd1;
      opacity: 0.3;
      font-family: Georgia, serif;
    }

    &:hover {
      background: linear-gradient(
        135deg,
        rgba(227, 242, 253, 0.7) 0%,
        rgba(243, 229, 245, 0.7) 100%
      );
      transform: translateX(4px);
    }

    p {
      margin: 0.5em 0;
    }
  }

  // 分隔线
  hr {
    border: none;
    height: 3px;
    background: linear-gradient(
      90deg,
      transparent 0%,
      #a18cd1 20%,
      #fbc2eb 50%,
      #a18cd1 80%,
      transparent 100%
    );
    margin: 2rem 0;
    border-radius: 2px;
    position: relative;
    overflow: visible;

    &::before {
      content: "✦";
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      background: white;
      padding: 0 10px;
      color: #a18cd1;
      font-size: 1.2em;
    }
  }

  // 任务列表
  ul[data-type="taskList"] {
    list-style: none;
    padding: 0;

    li {
      display: flex;
      align-items: flex-start;
      padding-left: 0;

      &::before {
        display: none;
      }

      > label {
        flex: 0 0 auto;
        margin-right: 0.75rem;
        user-select: none;
        margin-top: 0.2em;

        input[type="checkbox"] {
          width: 18px;
          height: 18px;
          cursor: pointer;
          border-radius: 4px;
          border: 2px solid #a18cd1;
          transition: all 0.2s ease;

          &:checked {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-color: #764ba2;
          }

          &:hover {
            transform: scale(1.1);
            border-color: #764ba2;
          }
        }
      }

      > div {
        flex: 1 1 auto;
      }
    }
  }

  // 表格样式
  table {
    border-collapse: collapse;
    margin: 1.5em 0;
    width: 100%;
    overflow: hidden;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(103, 58, 183, 0.1);

    td,
    th {
      border: 2px solid rgba(103, 58, 183, 0.15);
      padding: 0.75rem 1rem;
      text-align: left;
      transition: all 0.2s ease;
    }

    th {
      background: linear-gradient(
        135deg,
        rgba(227, 242, 253, 0.8) 0%,
        rgba(243, 229, 245, 0.8) 100%
      );
      font-weight: 700;
      color: #5e35b1;
    }

    tr {
      &:hover {
        background: rgba(227, 242, 253, 0.3);
      }
    }
  }

  // 强调样式
  strong {
    font-weight: 700;
    color: #5e35b1;
  }

  em {
    font-style: italic;
    color: #7b68ee;
  }

  // 删除线
  s {
    text-decoration: line-through;
    opacity: 0.7;
  }
}
</style>
