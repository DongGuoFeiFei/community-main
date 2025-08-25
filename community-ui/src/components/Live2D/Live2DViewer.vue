<template>
  <div>
    <Live2DControlPanel
        v-model="store.isVisibleLive2D"
        @update:text="showAiText"
        @show-tooltip="showTooltipText"
        @hide-tooltip="hideTooltipText"
        @switch-model="switchModel"
    />
    <div class="live2d-wrapper" v-show="store.isVisibleLive2D">
      <div v-if="showText" class="live2d-text-bubble">
        {{ currentText }}
      </div>

      <!-- 动态组件切换 -->
      <component
          :is="currentModelComponent"
          @trigger-motion="handleMotion"
      />
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, shallowRef, watch} from 'vue';
import Live2DControlPanel from './Live2DControlPanel.vue';
import {localStores} from '@/stores/localStores.js';

// 导入所有模型组件
import MurasameModel from './models/MurasameModel.vue';
import NahidaModel from './models/NahidaModel.vue';
import AlyaModel from './models/AlyaModel.vue';
import CastoriceModel from './models/CastoriceModel.vue';
import TutuModel from "@/components/Live2D/models/TutuModel.vue";

const store = localStores();

// 模型组件映射
const modelComponents = {
  murasame: MurasameModel,
  // huTao: HuTaoModel,
  nahida: NahidaModel,
  alya: AlyaModel,
  castorice: CastoriceModel,
  // takoM: TakoMModel,
  tutu: TutuModel,
};

// 当前模型组件
const currentModelComponent = shallowRef(null);
// 当前模型名称
const currentModelName = ref('murasame');

// 文本相关状态
const showText = ref(false);
const currentText = ref('');
let textTimeout = null;

// todo 优化初始化模型，使页面最后渲染模型初始化，同一时间仅初始化一个模型
// 初始化模型
const initModel = () => {
  currentModelComponent.value = modelComponents[currentModelName.value];
};

// 获取所有模型名称的数组
const modelNames = Object.keys(modelComponents);
// 切换模型
const switchModel = () => {
  // 获取所有模型名称
  const availableModels = [...modelNames];

  // 移除当前模型（如果有）
  const currentIndex = availableModels.indexOf(currentModelName.value);
  if (currentIndex > -1) {
    availableModels.splice(currentIndex, 1);
  }

  // 如果有可用的模型
  if (availableModels.length > 0) {
    const randomIndex = Math.floor(Math.random() * availableModels.length);
    const randomModelName = availableModels[randomIndex];

    currentModelName.value = randomModelName;
    currentModelComponent.value = modelComponents[randomModelName];
    console.log(randomModelName)
  } else {
    console.log('没有其他模型可切换');
  }
};

// 触发招呼语以及传递文本
const handleMotion = (text, duration) => {
  currentText.value = text;
  showText.value = true;
  clearTimeout(textTimeout);
  textTimeout = setTimeout(() => {
    showText.value = false;
  }, duration || text.length * 200 + 2000);
};

// 显示AI文本
const showAiText = (text) => {
  handleMotion(text, text.length * 100);
};

// 显示工具提示文本
const showTooltipText = (text) => {
  if (!text) return;
  currentText.value = text;
  showText.value = true;
};

// 隐藏工具提示文本
const hideTooltipText = () => {
  clearTimeout(textTimeout);
  showText.value = false;
};

// 初始化
onMounted(() => {
  initModel()
})


watch(
    () => store.isVisibleLive2D,
    (newVal) => {
      if (newVal) {
        initModel();
      }
    }
);
</script>

<style scoped lang="scss">
.live2d-wrapper {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 999;

  .live2d-text-bubble {
    position: absolute;
    bottom: 100%;
    left: 0;
    background-color: rgba(255, 255, 255, 0.9);
    padding: 8px 12px;
    border-radius: 12px;
    max-width: 200px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    font-size: 14px;
    color: #333;
    margin-bottom: 8px;
    animation: fadeIn 0.3s ease;

    &::after {
      content: '';
      position: absolute;
      top: 100%;
      left: 56px;
      border-width: 6px;
      border-style: solid;
      border-color: rgba(255, 255, 255, 0.9) transparent transparent transparent;
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
