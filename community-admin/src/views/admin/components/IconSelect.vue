<template>
  <div class="icon-select-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Element Plus" name="element">
        <div class="icon-list">
          <div
            v-for="icon in elementIcons"
            :key="icon"
            class="icon-item"
            :class="{ active: selectedIcon === icon }"
            @click="handleSelect(icon)"
          >
            <el-icon :size="20">
              <component :is="icon" />
            </el-icon>
            <span class="icon-name">{{ icon }}</span>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import * as ElementPlusIcons from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const activeTab = ref('element')
const selectedIcon = ref(props.modelValue)

const elementIcons = Object.keys(ElementPlusIcons)

const handleSelect = (icon) => {
  selectedIcon.value = icon
  emit('update:modelValue', icon)
}
</script>

<style lang="scss" scoped>
.icon-select-container {
  .icon-list {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 10px;
    max-height: 300px;
    overflow-y: auto;

    .icon-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 10px;
      cursor: pointer;
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background-color: #f5f7fa;
      }

      &.active {
        background-color: var(--el-color-primary-light-9);
        color: var(--el-color-primary);
      }

      .icon-name {
        margin-top: 5px;
        font-size: 12px;
        text-align: center;
        word-break: break-all;
      }
    }
  }
}
</style>
