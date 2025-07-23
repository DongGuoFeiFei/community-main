<template>
  <div class="live2d-control-panel">
    <el-tooltip effect="dark" :content="isVisible?`隐藏看板娘`:`显示看板娘`" placement="left">
      <el-button
          class="control-btn"
          :icon="isVisible ? 'Hide' : 'View'"
          circle
          @click="toggleVisibility"
      />
    </el-tooltip>
    <!-- 可以在这里添加更多控制按钮 -->
  </div>
</template>

<script setup>
import {ref, watch} from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:modelValue'])

const isVisible = ref(props.modelValue)

const toggleVisibility = () => {
  isVisible.value = !isVisible.value
  emit('update:modelValue', isVisible.value)
}

watch(() => props.modelValue, (newVal) => {
  isVisible.value = newVal
})
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
}
</style>