<template>
  <div>
    <Live2DControlPanel
        v-model="isVisible"
        @update:text="showAiText"
    />
    <div class="live2d-wrapper" v-show="isVisible">
      <div v-if="showText" class="live2d-text-bubble">
        {{ currentText }}
      </div>
      <canvas ref="liveCanvas" class="live2d-canvas"></canvas>
    </div>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref, watch} from 'vue'
import * as PIXI from 'pixi.js'
import {Live2DModel} from 'pixi-live2d-display/cubism4'
import Live2DControlPanel from "@/views/pages/components/Live2D/Live2DControlPanel.vue";

const props = defineProps({
  modelPath: {
    type: String,
    default: '/live2d/Murasame/Murasame.model3.json'
  },
  scale: {
    type: Number,
    default: 0.13,
    validator: (v) => v > 0 && v <= 1
  },
})

const liveCanvas = ref(null)

const showText = ref(false)
const currentText = ref('')
let app = null
let model = null
let textTimeout = null

const isVisible = ref(true)

const showAiText = (text) => {
  currentText.value = text;
  showText.value = true;
  clearTimeout(textTimeout);
  textTimeout = setTimeout(() => {
    showText.value = false;
  }, text.length * 100);
};

// 初始化Live2D
const initLive2D = async () => {
  window.PIXI = PIXI

  // 设置画布大小
  const canvasWidth = 200
  const canvasHeight = 390

  app = new PIXI.Application({
    view: liveCanvas.value,
    width: canvasWidth,
    height: canvasHeight,
    backgroundAlpha: 0,
    antialias: true,
    autoDensity: true,
    resolution: window.devicePixelRatio || 1,
  })

  try {
    model = await Live2DModel.from(props.modelPath)
    app.stage.addChild(model)

    // 设置模型位置和大小
    model.scale.set(props.scale)

    // 将模型居中放置在画布中(在画布的大小进行设置)
    model.position.set(
        canvasWidth - model.width * props.scale * 8,
        canvasHeight - model.height * props.scale * 7.7
    )

    // 启用交互
    model.interactive = true
    model.buttonMode = true

    // 点击事件处理
    model.on('hit', (hitAreas) => {
      handleHitArea(hitAreas[0])
    })

    // 初始空闲动画
    model.motion('Idle')

  } catch (error) {
    console.error('Live2D加载失败:', error)
  }
}

// 处理点击区域
const handleHitArea = (hitArea) => {
  clearTimeout(textTimeout)

  switch (hitArea) {
    case 'face':
      triggerRandomMotion('Tapface')
      break
    case 'hair':
      triggerRandomMotion('Taphair')
      break
    case 'xiongbu':
      triggerRandomMotion('Tapxiongbu')
      break
    case 'qunzi':
      triggerRandomMotion('Tapqunzi')
      break
    case 'leg':
      triggerRandomMotion('Tapleg')
      break
    default:
      model.motion('Idle')
  }
}

// 触发随机动作
const triggerRandomMotion = (motionGroup) => {
  const motions = model.internalModel.motionManager.definitions[motionGroup]
  if (motions && motions.length > 0) {
    const randomIndex = Math.floor(Math.random() * motions.length)
    model.motion(motionGroup, randomIndex)

    // 显示文本
    if (motions[randomIndex].Text) {
      currentText.value = motions[randomIndex].Text
      showText.value = true
      textTimeout = setTimeout(() => {
        showText.value = false;
      }, motions[randomIndex].Text.length * 200 + 2000);
    }

    // 播放声音(语言报错，对应的文本也不显示)
    if (motions[randomIndex].Sound) {
      const audio = new Audio(motions[randomIndex].Sound)
      audio.play().catch(e => console.error('音频播放失败:', e))
    }
  }
}

// 销毁资源
const destroyLive2D = () => {
  if (textTimeout) clearTimeout(textTimeout)
  model?.destroy()
  app?.destroy()
}

watch(
    () => isVisible.value,
    (newData) => {
      if (newData) {
        initLive2D()
      }
    }
)

onMounted(() => {
  initLive2D();
})

onBeforeUnmount(() => {
  destroyLive2D();
})
</script>

<style scoped lang="scss">
.live2d-wrapper {
  position: fixed;
  right: 0;
  bottom: 0;
  width: 200px;
  height: 390px;
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

  .live2d-canvas {
    width: 100%;
    height: 100%;
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