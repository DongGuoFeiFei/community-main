<template>
  <canvas ref="liveCanvas" class="live2d-canvas"></canvas>

  <div class="control-panel">
    <el-tooltip disabled>
      <el-button @click="switchPhone" circle>
        <el-icon>
          <Iphone/>
        </el-icon>
      </el-button>
    </el-tooltip>
    <el-tooltip disabled>
      <el-button @click="switchHairOrnament" circle>
        <el-icon>
          <MagicStick/>
        </el-icon>
      </el-button>
    </el-tooltip>
    <el-tooltip disabled>
      <el-button @click="switchHair" circle>
        <el-icon>
          <Female/>
        </el-icon>
      </el-button>
    </el-tooltip>
    <el-tooltip disabled>
      <el-button @click="switchCloth" circle>
        <el-icon>
          <Present/>
        </el-icon>
      </el-button>
    </el-tooltip>
    <el-tooltip disabled>
      <el-button @click="switchLeg" circle>
        <el-icon>
          <SemiSelect/>
        </el-icon>
      </el-button>
    </el-tooltip>
    <el-tooltip disabled>
      <el-button @click="switchBackground" circle>
        <el-icon>
          <Picture/>
        </el-icon>
      </el-button>
    </el-tooltip>
  </div>
</template>

<script setup>
import {onBeforeUnmount, onMounted, ref} from 'vue';
import * as PIXI from 'pixi.js';
import {Live2DModel} from 'pixi-live2d-display/cubism4';
import {Female, Iphone, MagicStick, Picture, Present, SemiSelect} from '@element-plus/icons-vue';

const liveCanvas = ref(null);
let app = null;
let model = null;
// 切换状态记录
const switchStates = ref({
  phone: 0,
  hairOrnament: 0,
  hair: 0,
  cloth: 0,
  leg: 0,
  background: 0
});

const emit = defineEmits(['trigger-motion']);

const modelConfig = {
  path: '/live2d/Castorice_V2/Castorice_V2.model3.json',
  scale: 0.08,
  greeting: "午安，我是遐蝶。幽暗由我执笔，愿你成为下一行光。"
};

// 初始化Live2D
const initLive2D = async () => {
  try {
    window.PIXI = PIXI;

    // 设置画布大小
    const canvasWidth = 400;
    const canvasHeight = 200;

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

    // 计算并设置模型位置
    const xPos = canvasWidth - model.width * modelConfig.scale * 14;
    const yPos = canvasHeight - model.height * modelConfig.scale * 13;
    model.position.set(xPos, yPos);

    // 启用交互
    model.interactive = true;
    model.buttonMode = true;

    // 点击事件处理
    model.on('hit', (hitAreas) => {
      handleHitArea(hitAreas[0]);
    });

    // 初始随机闲置动画
    playRandomIdle();

    // 模型加载完成后显示招呼语
    emit('trigger-motion', modelConfig.greeting, modelConfig.greeting.length * 150 + 3000);

  } catch (error) {
    console.error('Live2D初始化错误:', error);
  }
};

// 处理点击区域
const handleHitArea = (hitArea) => {
  switch (hitArea) {
    case 'Body':
      triggerRandomMotion('TapBody');
      break;
    case 'Head':
      triggerRandomMotion('TapHead');
      break;
    case 'Bust':
      triggerRandomMotion('TapBust');
      break;
    case 'Leg':
      triggerRandomMotion('TapLeg');
      break;
    case 'Arm':
      triggerRandomMotion('TapArm');
      break;
    default:
      playRandomIdle(); // 默认播放随机闲置动画
  }
};

// 触发随机动作
const triggerRandomMotion = (motionGroup) => {
  const motions = model.internalModel.motionManager.definitions[motionGroup];
  if (motions?.length > 0) {
    const randomIndex = Math.floor(Math.random() * motions.length);
    model.motion(motionGroup, randomIndex);
    // todo 为每个点击事件添加文字
    // 显示文本（如果有）
    if (motions[randomIndex].Text) {
      emit('trigger-motion', motions[randomIndex].Text, motions[randomIndex].Text.length * 200 + 2000);
    }
  } else {
    playRandomIdle(); // 如果没有指定动作，返回闲置状态
  }
};


// 播放随机闲置动画
const playRandomIdle = () => {
  if (!model) return;

  const idleMotions = model.internalModel?.motionManager?.definitions['Idle'];
  if (idleMotions?.length > 0) {
    const randomIndex = Math.floor(Math.random() * idleMotions.length);
    model.motion('Idle', randomIndex);
  }
};

// 切换功能实现
const switchPhone = () => {
  if (!model) return;
  switchStates.value.phone = switchStates.value.phone === 0 ? 1 : 0;
  model.internalModel.coreModel.setParameterValueById('PARAM_PHONE_SWITCH', switchStates.value.phone);
};

const switchHairOrnament = () => {
  if (!model) return;
  switchStates.value.hairOrnament = (switchStates.value.hairOrnament + 1) % 6;
  model.internalModel.coreModel.setParameterValueById('PARAM_HAIRORNAMENT_SWITCH', switchStates.value.hairOrnament);
};

const switchHair = () => {
  if (!model) return;
  switchStates.value.hair = switchStates.value.hair === 0 ? 1 : 0;
  model.internalModel.coreModel.setParameterValueById('PARAM_HAIR_SWITCH', switchStates.value.hair);
};

const switchCloth = () => {
  if (!model) return;
  switchStates.value.cloth = switchStates.value.cloth === 0 ? 1 : 0;
  model.internalModel.coreModel.setParameterValueById('PARAM_CLOTH_SWITCH', switchStates.value.cloth);
};

const switchLeg = () => {
  if (!model) return;
  switchStates.value.leg = (switchStates.value.leg + 1) % 3;
  model.internalModel.coreModel.setParameterValueById('PARAM_LEG_SWITCH', switchStates.value.leg);
};

const switchBackground = () => {
  if (!model) return;
  switchStates.value.background = (switchStates.value.background + 1) % 3;
  model.internalModel.coreModel.setParameterValueById('PARAM_BACKGROUND', switchStates.value.background);
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

.control-panel {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 100;
  display: flex;
  gap: 8px;
  background: rgba(255, 255, 255, 0.7);
  padding: 8px;
  border-radius: 20px;

  .el-button {
    width: 36px;
    height: 36px;
    padding: 0;

    :deep(.el-icon) {
      font-size: 18px;
    }
  }
}
</style>
