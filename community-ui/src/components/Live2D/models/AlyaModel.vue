<template>
  <canvas ref="liveCanvas" class="live2d-canvas"></canvas>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue';
import * as PIXI from 'pixi.js';
import {Live2DModel} from 'pixi-live2d-display/cubism4';

const liveCanvas = ref(null);
let app = null;
let model = null;

const emit = defineEmits(['trigger-motion']);

const modelConfig = {
  path: '/live2d/alya/Alya.model3.json',
  scale: 0.07,
  greeting: "Дурак…哼！才、才不是关心你，只是路过提醒一下——我是艾莉，别忘啦！"
};

// 初始化Live2D
const initLive2D = async () => {
  try {
    window.PIXI = PIXI;

    // 设置画布大小
    const canvasWidth = 220;
    const canvasHeight = 280;

    app = new PIXI.Application({
      view: liveCanvas.value,
      width: canvasWidth,
      height: canvasHeight,
      backgroundAlpha: 0,
      antialias: true,
      autoDensity: true,
      resolution: window.devicePixelRatio || 1,
    });

    // 加载模型
    model = await Live2DModel.from(modelConfig.path);

    // 验证模型是否加载成功
    if (!model) {
      throw new Error('模型加载失败');
    }

    app.stage.addChild(model);

    // 设置模型缩放
    model.scale.set(modelConfig.scale);

    // 计算并设置模型位置
    const xPos = canvasWidth - model.width * modelConfig.scale * 14;
    const yPos = canvasHeight - model.height * modelConfig.scale * 15;
    model.position.set(xPos, yPos);

    // 模型加载完成后显示招呼语
    emit('trigger-motion', modelConfig.greeting, modelConfig.greeting.length * 150 + 3000);


  } catch (error) {
    console.error('Live2D初始化错误:', error);
  }
};

// 销毁资源
const destroyLive2D = () => {
  try {
    if (model) {
      model.destroy();
      model = null;
    }
    if (app) {
      app.destroy();
      app = null;
    }
  } catch (error) {
    console.error('资源销毁错误:', error);
  }
};

onMounted(() => {
  initLive2D();
});

onBeforeUnmount(() => {
  destroyLive2D();
});
</script>

<style scoped lang="scss">
.live2d-canvas {
  width: 100%;
  height: 100%;
  display: block;
}
</style>
