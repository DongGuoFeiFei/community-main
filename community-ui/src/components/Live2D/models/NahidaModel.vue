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
  path: '/live2d/草神/草神.model3.json',
  scale: 0.07,
  greeting: "叮——小草神上线！纳西妲向你发出一起观察世界的邀请，接受吗？",
  // 定义可触发的表情及其权重
  expressions: [
    {name: '发光', weight: 1.2, hint: "闪闪发光~"},
    {name: '叶子', weight: 1.0, hint: "叶子沙沙响..."},
    {name: '姿势1', weight: 0.8},
    {name: '姿势2', weight: 0.8},
    {name: '姿势3', weight: 0.8},
    {name: '星星', weight: 1.1, hint: "★~"},
    {name: '泪', weight: 0.5, hint: "呜..."},
    {name: '生气', weight: 0.3, hint: "哼！"},
    {name: '脸红', weight: 1.0, hint: "///"},
    {name: '黑', weight: 0.2}
  ],
  // 身体点击区域定义 (相对坐标和尺寸)
  hitAreas: [
    {name: 'body', x: 0.3, y: 0.4, width: 0.4, height: 0.5}
  ]
};

// 初始化Live2D
const initLive2D = async () => {
  try {
    window.PIXI = PIXI;

    const canvasWidth = 220;
    const canvasHeight = 335;

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

    if (!model) {
      throw new Error('模型加载失败');
    }

    app.stage.addChild(model);
    model.scale.set(modelConfig.scale);

    const xPos = canvasWidth - model.width * modelConfig.scale * 14;
    const yPos = canvasHeight - model.height * modelConfig.scale * 14;
    model.position.set(xPos, yPos);

    // 显示招呼语
    emit('trigger-motion', modelConfig.greeting, modelConfig.greeting.length * 150 + 3000);

  } catch (error) {
    console.error('Live2D初始化错误:', error);
  }
};


// 获取加权随机表情
const getRandomExpression = () => {
  console.log("点击纳西妲")
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
