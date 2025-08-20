<template>
  <el-card class="points-level-info">
    <template #header>
      <div class="card-header">
        <span>等级信息</span>
      </div>
    </template>

    <div class="level-display" v-if="account && account.levelId">
      <div class="level-badge">
        <el-tag type="primary" size="large">Lv.{{ account.levelId }}</el-tag>
      </div>

      <div class="progress-container">
        <div class="progress-info">
          <span>当前经验: {{ account.experience || 0 }}</span>
          <span>下一等级: {{ nextLevelInfo ? nextLevelInfo.levelId : '最高等级' }}</span>
        </div>

        <el-progress
          :percentage="progressPercentage"
          :format="formatProgress"
          :stroke-width="16"
          :color="progressColor"
        />

        <div class="level-requirements" v-if="nextLevelInfo">
          <span>升级需要: {{ nextLevelInfo.requiredExperience - account.experience }} 经验</span>
        </div>
      </div>
    </div>

    <el-empty v-else description="暂无等级信息" />
  </el-card>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { getUserLevelInfo, getPointsLevels } from '@/api/points';

const props = defineProps({
  account: {
    type: Object,
    required: true,
    default: () => ({})
  }
});

const levels = ref([]);
const currentLevelInfo = ref({});
const loading = ref(false);

const loadLevels = async () => {
  try {
    loading.value = true;
    const [levelsResponse, currentLevelResponse] = await Promise.all([
      getPointsLevels(),
      getUserLevelInfo(props.account.levelId)
    ]);

    levels.value = levelsResponse.data;
    currentLevelInfo.value = currentLevelResponse.data;
  } catch (error) {
    console.error('Failed to load level info:', error);
  } finally {
    loading.value = false;
  }
};

const nextLevelInfo = computed(() => {
  if (!levels.value.length) return null;

  const nextLevel = levels.value.find(level => level.levelId > props.account.levelId);
  return nextLevel || null;
});

const progressPercentage = computed(() => {
  if (!nextLevelInfo.value || !props.account.experience) return 0;

  const currentExp = props.account.experience;
  const currentLevelExp = currentLevelInfo.value.requiredExperience || 0;
  const nextLevelExp = nextLevelInfo.value.requiredExperience;

  const range = nextLevelExp - currentLevelExp;
  const progress = ((currentExp - currentLevelExp) / range) * 100;

  return Math.min(100, Math.max(0, Math.round(progress)));
});

const progressColor = computed(() => {
  const percentage = progressPercentage.value;
  if (percentage < 30) return '#f56c6c';
  if (percentage < 70) return '#e6a23c';
  return '#67c23a';
});

const formatProgress = () => {
  return `${progressPercentage.value}%`;
};

watch(() => props.account.levelId, (newVal) => {
  if (newVal) {
    loadLevels();
  }
}, { immediate: true });
</script>

<style scoped lang="scss">
.points-level-info {
  margin-bottom: 20px;

  .card-header {
    font-weight: bold;
    font-size: 16px;
  }

  .level-display {
    display: flex;
    align-items: center;
    gap: 20px;

    .level-badge {
      .el-tag {
        font-size: 24px;
        padding: 0 16px;
        height: 50px;
        line-height: 50px;
      }
    }

    .progress-container {
      flex: 1;

      .progress-info {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
        font-size: 14px;
        color: #666;
      }

      .level-requirements {
        margin-top: 8px;
        font-size: 14px;
        color: #666;
        text-align: right;
      }
    }
  }
}
</style>
