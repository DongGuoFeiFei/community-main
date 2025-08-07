<template>
  <div class="live2d-control-panel">
    <!-- 仅在显示状态时展示的其他控制按钮 -->
    <div v-if="isVisible">

      <!--  聊天按钮    -->
      <div>
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
                @mouseenter="showTooltipText('要与丛雨对话嘛？')"
                @mouseleave="hideTooltipText"
            />
          </template>
          <Live2DChatDialog @update:text="emit('update:text', $event)"/>
        </el-popover>
      </div>

      <!-- 举报按钮 -->
      <div v-if="isArticle">
        <el-tooltip
            effect="dark"
            disabled
            placement="left"
        >
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
    </div>

    <!-- 始终显示的主控制按钮 -->
    <div>
      <el-tooltip
          effect="dark"
          :content="isVisible ? '' : `显示丛雨`"
          :disabled="isVisible"
          placement="left"
      >
        <el-button
            class="control-btn main-control"
            :icon="isVisible ? 'Hide' : 'View'"
            circle
            @mouseenter="showTooltipText('要和丛雨说再见嘛？')"
            @mouseleave="hideTooltipText"
            @click="toggleVisibility"
        />
      </el-tooltip>
    </div>

    <Live2DReportDialog ref="reportDialog"/>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
import Live2DReportDialog from "@/components/Live2D/components/Live2DReportDialog.vue";
import Live2DChatDialog from "@/components/Live2D/components/Live2DChatDialog.vue";
import {useRoute} from "vue-router";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['update:modelValue', 'update:text', 'show-tooltip', 'hide-tooltip']);

const isVisible = ref(props.modelValue);
const isLoading = ref(false);
const reportDialog = ref(null);
const route = useRoute()
const isArticle = computed(() => {
  const articleRegex = /^\/(article|author)\/\d+$/;
  return articleRegex.test(route.path);
})

const toggleVisibility = () => {
  isVisible.value = !isVisible.value;
  emit('update:modelValue', isVisible.value);
};

const showTooltipText = (text) => {
  emit('show-tooltip', text);
};

const hideTooltipText = () => {
  emit('hide-tooltip');
};

const openReportDialog = () => {
  reportDialog.value.open();
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
</style>
