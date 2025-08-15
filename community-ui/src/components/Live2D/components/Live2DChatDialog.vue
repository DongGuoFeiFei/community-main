<template>
  <div class="compact-chat-input">
    <el-input
        v-model="inputMessage"
        placeholder="要说些什么呢？"
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
      <el-icon>
        <Promotion/>
      </el-icon>
    </el-button>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {generateText} from '@/api/deepseek.js';
import {ElMessage} from 'element-plus';
import {Promotion} from '@element-plus/icons-vue';

const emit = defineEmits(['update:text']);

const inputMessage = ref('');
const isLoading = ref(false);

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
