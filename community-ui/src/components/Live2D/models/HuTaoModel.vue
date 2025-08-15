<template>
  <canvas ref="liveCanvas" class="live2d-canvas"></canvas>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue';
import * as PIXI from 'pixi.js';
import {Live2DModel} from 'pixi-live2d-display/cubism4';

const emit = defineEmits(['trigger-motion']);

const liveCanvas = ref(null);
let app = null;
let model = null;

const modelConfig = {
  path: '/live2d/Hu Tao/Hu Tao.model3.json',
  scale: 0.035,
  greeting: "往生堂第七十七代堂主胡桃，专司阴阳两界平衡之事"
};

// 初始化Live2D
const initLive2D = async () => {
  window.PIXI = PIXI;

  // 设置画布大小
  const canvasWidth = 200;
  const canvasHeight = 362;

  app = new PIXI.Application({
    view: liveCanvas.value,
    width: canvasWidth,
    height: canvasHeight,
    backgroundAlpha: 0,
    antialias: true,
    autoDensity: true,
    resolution: window.devicePixelRatio || 1,
  });

  try {
    model = await Live2DModel.from(modelConfig.path);
    app.stage.addChild(model);

    // 设置模型位置和大小
    model.scale.set(modelConfig.scale);

    // 将模型居中放置在画布中
    model.position.set(
        canvasWidth - model.width * modelConfig.scale * 32,
        canvasHeight - model.height * modelConfig.scale * 29
    );

    emit('trigger-motion', modelConfig.greeting, modelConfig.greeting.length * 150 + 3000);

  } catch (error) {
    console.error('Live2D加载失败:', error);
  }
};

// 销毁资源
const destroyLive2D = () => {
  model?.destroy();
  app?.destroy();
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
  //background-color: #58B19F;
}
</style>
