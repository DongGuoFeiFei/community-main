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
                @mouseenter="showTooltipText('要说些什么呢？')"
                @mouseleave="hideTooltipText"
            />
          </template>
          <Live2DChatDialog @update:text="emit('update:text', $event)"/>
        </el-popover>
      </div>

      <!-- 模型切换按钮 -->
      <div>
        <el-popover
            placement="left"
            :width="200"
            disabled
            trigger="click"
        >
          <template #reference>
            <el-button
                class="control-btn"
                icon="Refresh"
                circle
                @mouseenter="showTooltipText('又要找其他姐姐玩耍了嘛(╯‵□′)╯︵┻━┻')"
                @mouseleave="hideTooltipText"
                @click="emit('switch-model')"
            />
          </template>
        </el-popover>
      </div>

      <!-- 举报按钮 -->
      <div v-if="showReportButton">
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
      <Live2DReportDialog ref="reportDialog"/>
    </div>

    <!-- 始终显示的主控制按钮 -->
    <div>
      <el-tooltip
          effect="dark"
          disabled
          placement="left"
      >
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
import {onMounted, ref, watch} from 'vue';
import Live2DReportDialog from "@/components/Live2D/components/Live2DReportDialog.vue";
import Live2DChatDialog from "@/components/Live2D/components/Live2DChatDialog.vue";

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['update:modelValue', 'update:text', 'show-tooltip', 'hide-tooltip', 'switch-model']);

const isVisible = ref(props.modelValue);

const toggleVisibility = () => {
  isVisible.value = !isVisible.value;
  emit('update:modelValue', isVisible.value);
};

const showTooltipText = (text) => {
  if (isVisible) {
    emit('show-tooltip', text);
  }
};

const hideTooltipText = () => {
  emit('hide-tooltip');
};


/**
 * 显示举报按钮
 */
const reportDialog = ref(null);
const showReportButton = ref(false); // 新增状态

// 监听 reportDialog 的变化
watch(reportDialog, (newVal) => {
  if (newVal) {
    showReportButton.value = newVal.isShowButton;
  }
}, {immediate: true});

onMounted(() => {
  if (reportDialog.value) {
    showReportButton.value = reportDialog.value.isShowButton;
  }
});

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
}
</style>
