<template>
  <div className="live2d-container">
    <canvas ref="liveCanvas" className="live2d"></canvas>
    <div v-if="showText" className="live2d-text-bubble">
      {{ currentText }}
    </div>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import * as PIXI from 'pixi.js'
import {Live2DModel} from 'pixi-live2d-display/cubism4'

const props = defineProps({
  modelPath: {
    type: String,
    default: '/live2d/Murasame/Murasame.model3.json'
  },
  scale: {
    type: Number,
    default: 0.13,
    validator: (v) => v > 0 && v <= 1
  }
})

const liveCanvas = ref(null)
const showText = ref(false)
const currentText = ref('')
let app = null
let model = null
let textTimeout = null

// 初始化Live2D
const initLive2D = async () => {
  window.PIXI = PIXI

  app = new PIXI.Application({
    view: liveCanvas.value,
    autoStart: true,
    backgroundAlpha: 0,
    resizeTo: window
  })

  try {
    model = await Live2DModel.from(props.modelPath)
    app.stage.addChild(model)

    // 设置初始大小和位置
    model.position.set(app.screen.width / 35, app.screen.height - app.screen.height / 2)
    model.scale.set(props.scale)

    // 点击事件处理
    model.on('hit', (hitAreas) => {
      handleHitArea(hitAreas[0]) // 取第一个点击区域
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

  // 根据点击区域触发不同的动作
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
    const motion = motions[randomIndex]

    // 播放动作
    model.motion(motionGroup, randomIndex)

    // 显示文本（如果有）
    if (motion.Text) {
      currentText.value = motion.Text
      showText.value = true
      textTimeout = setTimeout(() => {
        showText.value = false
      }, 3000) // 3秒后隐藏文本
    }

    // 播放声音（如果有）
    if (motion.Sound) {
      const audio = new Audio(motion.Sound)
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

// 生命周期钩子
onMounted(() => {
  initLive2D()
})

onBeforeUnmount(() => {
  destroyLive2D()
})
</script>

<style scoped lang="scss">
.live2d-container {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 1000;
  pointer-events: auto; // 允许点击

  .live2d {
    cursor: pointer;
    transition: transform 0.2s;

    &:active {
      transform: scale(0.98);
    }
  }

  .live2d-text-bubble {
    position: absolute;
    bottom: 100%;
    right: 0;
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
      right: 20px;
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