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

// 模型配置
const modelConfig = {
  path: '/live2d/Murasame/Murasame.model3.json',
  scale: 0.13,
  greeting: "锵锵～丛雨登场！主人抱抱，今天也要黏在一起喔～！"
};

// 初始化Live2D
const initLive2D = async () => {
  window.PIXI = PIXI;

  // 设置画布大小
  const canvasWidth = 200;
  const canvasHeight = 390;

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
        canvasWidth - model.width * modelConfig.scale * 8,
        canvasHeight - model.height * modelConfig.scale * 7.7
    );

    // 启用交互
    model.interactive = true;
    model.buttonMode = true;

    // 点击事件处理
    model.on('hit', (hitAreas) => {
      handleHitArea(hitAreas[0]);
    });

    // 初始空闲动画
    model.motion('Idle');

    // 模型加载完成后显示招呼语
    emit('trigger-motion', modelConfig.greeting, modelConfig.greeting.length * 150 + 3000);

  } catch (error) {
    console.error('Live2D加载失败:', error);
  }
};

// 处理点击区域
const handleHitArea = (hitArea) => {
  switch (hitArea) {
    case 'face':
      triggerRandomMotion('Tapface');
      break;
    case 'hair':
      triggerRandomMotion('Taphair');
      break;
    case 'xiongbu':
      triggerRandomMotion('Tapxiongbu');
      break;
    case 'qunzi':
      triggerRandomMotion('Tapqunzi');
      break;
    case 'leg':
      triggerRandomMotion('Tapleg');
      break;
    default:
      model.motion('Idle');
  }
};

// 触发随机动作
const triggerRandomMotion = (motionGroup) => {
  const motions = model.internalModel.motionManager.definitions[motionGroup];
  if (motions && motions.length > 0) {
    const randomIndex = Math.floor(Math.random() * motions.length);
    model.motion(motionGroup, randomIndex);

    // 显示文本
    if (motions[randomIndex].Text) {
      emit('trigger-motion', motions[randomIndex].Text, motions[randomIndex].Text.length * 200 + 2000);
    }

    // 播放声音
    if (motions[randomIndex].Sound) {
      const audio = new Audio(motions[randomIndex].Sound);
      audio.play().catch(e => console.error('音频播放失败:', e));
    }
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
}
</style>
