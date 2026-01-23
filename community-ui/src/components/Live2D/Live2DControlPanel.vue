<template>
  <div class="live2d-control-panel">
    <!-- 仅在显示状态时展示的其他控制按钮 -->
    <div v-if="isVisible">
      <!--  聊天按钮    -->
      <div>
        <el-popover placement="left" :width="600" trigger="click">
          <template #reference>
            <el-button
              class="control-btn"
              icon="ChatRound"
              circle
              @mouseenter="showTooltipText('要说些什么呢？')"
              @mouseleave="hideTooltipText"
            />
          </template>
          <AiChat
            ref="aiChatRef"
            height="500px"
            welcome-message="你好！我是 会话 助手，有什么可以帮您？"
            placeholder="要说些什么呢？"
            user-avatar="👤"
            assistant-avatar="🤖"
            user-name="用户"
            assistant-name="会话 助手"
            @message="handleAiMessage"
          />
        </el-popover>
      </div>

      <!-- 举报按钮 -->
      <div v-if="showReportButton">
        <el-tooltip effect="dark" disabled placement="left">
          <el-button
            class="control-btn"
            icon="Warning"
            circle
            @mouseenter="showTooltipText('发现违规内容，找我快速出警!')"
            @mouseleave="hideTooltipText"
            @click="openReportDialog"
          />
        </el-tooltip>
      </div>
      <Live2DReportDialog ref="reportDialog" />

      <!-- 模型切换按钮 -->
      <div>
        <el-popover placement="left" :width="200" disabled trigger="click">
          <template #reference>
            <el-button
              class="control-btn"
              icon="Refresh"
              circle
              @mouseenter="showTooltipText('(╯‵□′)╯︵┻━┻')"
              @mouseleave="hideTooltipText"
              @click="emit('switch-model')"
            />
          </template>
        </el-popover>
      </div>
    </div>

    <!-- 始终显示的主控制按钮 -->
    <div>
      <el-tooltip effect="dark" disabled placement="left">
        <el-button
          class="control-btn main-control"
          :icon="isVisible ? 'Hide' : 'View'"
          circle
          @mouseenter="showTooltipText(null)"
          @mouseleave="hideTooltipText"
          @click="toggleVisibility"
        />
      </el-tooltip>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from "vue";
import Live2DReportDialog from "@/components/Live2D/components/Live2DReportDialog.vue";
import AiChat from "@/components/common/AiChat.vue";
import { localStores } from "@/stores/localStores";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits([
  "update:modelValue",
  "update:text",
  "show-tooltip",
  "hide-tooltip",
  "switch-model",
]);

const isVisible = ref(props.modelValue);

const store = localStores();

const toggleVisibility = () => {
  isVisible.value = !isVisible.value;
  emit("update:modelValue", isVisible.value);
};

const showTooltipText = (text) => {
  if (isVisible) {
    emit("show-tooltip", text);
  }
};

const hideTooltipText = () => {
  emit("hide-tooltip");
};

/**
 * 显示举报按钮
 */
const reportDialog = ref(null);
const showReportButton = ref(false); // 新增状态

// 监听 reportDialog 的变化
watch(
  reportDialog,
  (newVal) => {
    if (newVal) {
      showReportButton.value = newVal.isShowButton;
    }
  },
  { immediate: true },
);

onMounted(() => {
  if (reportDialog.value) {
    showReportButton.value = reportDialog.value.isShowButton;
  }
});

const openReportDialog = () => {
  reportDialog.value.open();
};

/**
 * AI 聊天相关
 */
const aiChatRef = ref(null);

// 处理 AI 聊天消息
const handleAiMessage = (message) => {
  // 将 AI 的回复传递给 Live2D 显示
  emit("update:text", message);
};

// 获取 AI 对话的全部内容
const getAiChatMessages = () => {
  return aiChatRef.value?.messages || [];
};

// 获取对话历史的纯文本格式
const getAiChatHistory = () => {
  const messages = getAiChatMessages();
  return messages
    .map((msg) => {
      const role = msg.role === "user" ? "用户" : "AI助手";
      return `${role}: ${msg.content}`;
    })
    .join("\n\n");
};
watch(
  () => aiChatRef.value?.messages,
  (newValue) => {
    // todo 增加限制条件，存储数组数量不能太多，存在上限
    store.aiChatMessage = newValue;
  },
);

// 暴露方法供外部调用
defineExpose({
  getAiChatMessages,
  getAiChatHistory,
});
</script>

<style scoped lang="scss">
.live2d-control-panel {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 500;
  display: flex;
  flex-direction: column;
  gap: 10px;

  .control-btn {
    width: 36px;
    height: 36px;
    background-color: rgba(255, 255, 255, 0.8);
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s;

    &:hover {
      transform: scale(1.1);
      background-color: rgba(255, 255, 255, 0.95);
    }
  }

  .main-control {
    z-index: 1001;
  }

  .model-selector {
    display: flex;
    flex-direction: column;
    gap: 8px;
    padding: 8px;

    .model-option {
      width: 100%;
      padding: 8px;
      transition: all 0.2s;

      &:hover {
        background-color: #f5f7fa;
        transform: translateX(2px);
      }
    }
  }

  .chat-container {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .chat-actions {
      display: flex;
      justify-content: center;
      gap: 8px;
      padding: 8px;
      border-top: 1px solid #e4e7ed;
      background-color: #f5f7fa;
    }
  }
}
</style>
