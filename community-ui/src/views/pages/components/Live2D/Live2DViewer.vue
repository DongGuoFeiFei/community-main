<template>
  <div class="live2d-wrapper">
    <canvas ref="liveCanvas" class="live2d-canvas"></canvas>
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
let app = null
let model = null
let textTimeout = null

// 初始化Live2D
const initLive2D = async () => {
  window.PIXI = PIXI

  // 设置画布大小略大于模型
  const canvasWidth = 250
  const canvasHeight = 450

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
        canvasWidth / 5 - model.width * props.scale / 2,
        canvasHeight / 2 - model.height * props.scale * 3.5
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
.live2d-wrapper {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 250px;
  height: 450px;
  z-index: 9999;

  .live2d-canvas {
    //background-color: blue;
    width: 100%;
    height: 100%;
    pointer-events: auto; // 启用点击事件
  }
}
</style>