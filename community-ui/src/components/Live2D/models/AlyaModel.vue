<template>
  <canvas ref="liveCanvas" class="live2d-canvas" @click="handleCanvasClick"></canvas>
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
  greeting: "Дурак…哼！才、才不是关心你，只是路过提醒一下——我是艾莉，别忘啦！",
  // 身体点击区域定义 (相对坐标和尺寸)
  hitAreas: [
    {name: 'body', x: 0.3, y: 0.4, width: 0.4, height: 0.5}
  ]
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

const getRandomExpression = () => {
  console.log("点击alya")
};

// 检查点击是否在身体区域
const isInBodyArea = (clickX, clickY) => {
  const modelRect = model.getBounds();
  const modelX = clickX - model.position.x;
  const modelY = clickY - model.position.y;

  return modelConfig.hitAreas.some(area => {
    const areaX = modelRect.width * area.x;
    const areaY = modelRect.height * area.y;
    const areaWidth = modelRect.width * area.width;
    const areaHeight = modelRect.height * area.height;

    return modelX >= areaX &&
        modelX <= areaX + areaWidth &&
        modelY >= areaY &&
        modelY <= areaY + areaHeight;
  });
};

// 处理画布点击
const handleCanvasClick = (event) => {
  if (!model || !app) return;

  const rect = liveCanvas.value.getBoundingClientRect();
  const clickX = event.clientX - rect.left;
  const clickY = event.clientY - rect.top;

  // 检查是否点击身体区域
  if (isInBodyArea(clickX, clickY)) {
    const expression = getRandomExpression();
    if (expression) {
      model.expression = expression.name;
      if (expression.hint) {
        emit('trigger-motion', expression.hint, 1500);
      }
    }
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
  cursor: pointer;

  &:hover {
    filter: drop-shadow(0 0 8px rgba(146, 230, 180, 0.6));
  }
}
</style>
