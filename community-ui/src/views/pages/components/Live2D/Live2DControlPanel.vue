<template>
  <div class="live2d-control-panel">

    <!-- 仅在显示状态时展示的其他控制按钮 -->
    <template v-if="isVisible">
      <el-tooltip effect="dark" content="与看板娘对话" placement="left">
        <el-popover
            placement="left"
            :width="260"
            trigger="click"
        >
          <template #reference>
            <el-button
                class="control-btn"
                icon="ChatRound"
                circle
            />
          </template>
          <div class="compact-chat-input">
            <el-input
                v-model="inputMessage"
                placeholder="和看板娘聊天..."
                @keyup.enter="sendMessage"
                clearable
                class="chat-input-field"
            />
            <el-button
                type="primary"
                @click="sendMessage"
                :loading="isLoading"
                class="chat-send-btn"
            >
              <el-icon><Promotion /></el-icon>
            </el-button>
          </div>
        </el-popover>
      </el-tooltip>
    </template>

    <!-- 始终显示的主控制按钮 -->
    <el-tooltip effect="dark" :content="isVisible?`隐藏看板娘`:`显示看板娘`" placement="left">
      <el-button
          class="control-btn main-control"
          :icon="isVisible ? 'Hide' : 'View'"
          circle
          @click="toggleVisibility"
      />
    </el-tooltip>

  </div>
</template>

<script setup>
import { ref } from 'vue';
import { generateText } from '@/api/deepseek';
import { ElMessage } from 'element-plus';
import { Promotion } from '@element-plus/icons-vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['update:modelValue', 'update:text']);

const isVisible = ref(props.modelValue);
const inputMessage = ref('');
const isLoading = ref(false);

const toggleVisibility = () => {
  isVisible.value = !isVisible.value;
  emit('update:modelValue', isVisible.value);
};

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value) return;

  isLoading.value = true;

  try {
    const response = await generateText(inputMessage.value);
    emit('update:text', response.data);
  } catch (error) {
    ElMessage.error('对话请求失败');
    emit('update:text', '抱歉，我暂时无法回答这个问题...');
  } finally {
    isLoading.value = false;
    inputMessage.value = '';
  }
};
</script>

<style scoped lang="scss">
.live2d-control-panel {
  position: fixed;
  right: 20px;
  bottom: 20px;
  z-index: 1000;
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
    // 主控制按钮可以有不同的样式
    z-index: 1001; // 确保总是在最上层
  }
}

.compact-chat-input {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;

  .chat-input-field {
    flex: 1;

    :deep(.el-input__wrapper) {
      border-radius: 18px;
      padding: 0 15px;
      height: 36px;
    }
  }

  .chat-send-btn {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;

    &:hover {
      transform: scale(1.05);
    }
  }
}
</style>