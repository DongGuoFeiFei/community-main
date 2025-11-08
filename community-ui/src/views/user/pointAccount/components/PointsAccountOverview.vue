<template>
  <el-card class="points-account-overview">
    <template #header>
      <div class="card-header">
        <span>积分账户概览</span>
      </div>
    </template>

    <div class="account-info">
      <div class="info-item">
        <span class="label">总积分:</span>
        <span class="value">{{ account.totalPoints }}</span>
      </div>
      <div class="info-item">
        <span class="label">可用积分:</span>
        <span class="value">{{ account.availablePoints }}</span>
      </div>
      <div class="info-item">
        <span class="label">已消费积分:</span>
        <span class="value">{{ account.consumedPoints }}</span>
      </div>
      <div class="info-item">
        <span class="label">冻结积分:</span>
        <span class="value">{{ account.frozenPoints }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import dayjs from 'dayjs';
import {getPointsAccountDetail} from "@/api/points.js";

const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
});

const account = ref({
  totalPoints: 0,
  availablePoints: 0,
  consumedPoints: 0,
  frozenPoints: 0,
  lastUpdateTime: null
});

const loadAccountInfo = async () => {
  try {
    const response = await getPointsAccountDetail(props.userId);
    account.value = response.data;
  } catch (error) {
    console.error('Failed to load points account:', error);
  }
};

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss');
};

onMounted(() => {
  loadAccountInfo();
});
</script>

<style scoped lang="scss">
.points-account-overview {
  margin-bottom: 20px;

  .card-header {
    font-weight: bold;
    font-size: 16px;
  }

  .account-info {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;

    .info-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      border-bottom: 1px solid #f0f0f0;

      .label {
        color: #666;
      }

      .value {
        font-weight: bold;
        color: #333;
      }
    }
  }
}
</style>
