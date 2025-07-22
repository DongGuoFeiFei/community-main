<template>
  <div class="live2d-container">
    <canvas ref="liveCanvas"></canvas>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue'
import * as PIXI from 'pixi.js'
import {Live2DModel} from 'pixi-live2d-display/cubism4'

// 暴露可配置的props
const props = defineProps({
  modelPath: {
    type: String,
    default: '/live2d/Murasame/Murasame.model3.json'
  },
  scale: {
    type: Number,
    default: 0.2,
    validator: (v) => v > 0 && v <= 1
  },
  position: {
    type: Object,
    default: () => ({x: 0, y: 0})
  }
})

const liveCanvas = ref(null)
let app = null
let model = null

// 初始化Live2D
const initLive2D = async () => {
  // 确保PIXI全局可用（某些插件需要）
  window.PIXI = PIXI

  app = new PIXI.Application({
    view: liveCanvas.value,
    autoStart: true,
    backgroundAlpha: 0, // 透明背景
    resizeTo: window // 响应式尺寸
  })

  try {
    model = await Live2DModel.from(props.modelPath)
    model.scale.set(props.scale)
    model.position.set(props.position.x, props.position.y)
    app.stage.addChild(model)
  } catch (error) {
    console.error('Live2D加载失败:', error)
  }
}

// 销毁资源
const destroyLive2D = () => {
  model?.destroy()
  app?.destroy()
}

// 生命周期钩子
onMounted(initLive2D)
onBeforeUnmount(destroyLive2D)
</script>

<style scoped>
.live2d-container {
  position: fixed;
  right: 0;
  bottom: 0;
  z-index: 1000;
  pointer-events: none;
}
</style>