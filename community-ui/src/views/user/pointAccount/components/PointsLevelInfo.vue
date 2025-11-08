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
        <div class="level-name">{{ account.pointsLevel?.levelName }}</div>
      </div>

      <div class="progress-container">
        <div class="progress-info">
          <span>当前经验: {{ account.experience || 0 }}</span>
          <span>当前等级范围: {{ account.pointsLevel?.minExperience }} - {{ account.pointsLevel?.maxExperience }}</span>
        </div>

        <el-progress
            :percentage="progressPercentage"
            :format="formatProgress"
            :stroke-width="16"
            :color="progressColor"
        />

        <div class="level-requirements" v-if="!isMaxLevel">
          <span>升级需要: {{ nextLevelRequiredExp }} 经验</span>
        </div>
        <div class="level-requirements" v-else>
          <span>已达到最高等级</span>
        </div>
      </div>
    </div>

    <el-empty v-else description="暂无等级信息"/>
  </el-card>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import {getPointsLevels} from '@/api/points.js';

const props = defineProps({
  account: {
    type: Object,
    required: true,
    default: () => ({})
  }
});

const levels = ref([]);
const loading = ref(false);

const loadLevels = async () => {
  try {
    loading.value = true;
    const response = await getPointsLevels();
    levels.value = response.data;
  } catch (error) {
    console.error('Failed to load levels:', error);
  } finally {
    loading.value = false;
  }
};

const isMaxLevel = computed(() => {
  if (!levels.value.length) return false;
  const maxLevel = Math.max(...levels.value.map(l => l.levelId));
  return props.account.levelId === maxLevel;
});

const nextLevelRequiredExp = computed(() => {
  if (!levels.value.length || isMaxLevel.value) return 0;

  const nextLevel = levels.value.find(level => level.levelId > props.account.levelId);
  if (!nextLevel) return 0;

  return nextLevel.minExperience - props.account.experience;
});

const progressPercentage = computed(() => {
  if (!props.account.pointsLevel || !props.account.experience) return 0;

  const currentExp = props.account.experience;
  const minExp = props.account.pointsLevel.minExperience;
  const maxExp = props.account.pointsLevel.maxExperience;

  const range = maxExp - minExp;
  const progress = ((currentExp - minExp) / range) * 100;

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

onMounted(() => {
  loadLevels();
});
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
      display: flex;
      flex-direction: column;
      align-items: center;
      min-width: 100px;

      .el-tag {
        font-size: 24px;
        padding: 0 16px;
        height: 50px;
        line-height: 50px;
        margin-bottom: 8px;
      }

      .level-name {
        font-size: 14px;
        color: #666;
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
