<template>
  <div class="account-basic-info">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="账户ID">{{ account.accountId }}</el-descriptions-item>
      <el-descriptions-item label="用户ID">{{ account.userId }}</el-descriptions-item>
      <el-descriptions-item label="用户昵称">{{ account.user.nickname }}</el-descriptions-item>
      <el-descriptions-item label="用户名">{{ account.user.username }}</el-descriptions-item>
      <el-descriptions-item label="累计积分">
        <el-tag type="info">{{ account.totalPoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="可用积分">
        <el-tag type="success">{{ account.availablePoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="已消费积分">
        <el-tag>{{ account.consumedPoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="冻结积分">
        <el-tag type="warning">{{ account.frozenPoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="当前等级">
        <el-tag :type="getLevelTag(account.level.levelName)">
          {{ account.level.levelName }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="当前经验值">{{ account.experience }}</el-descriptions-item>
      <el-descriptions-item label="升级所需经验">
        {{ account.experience }}/{{ account.level.maxExperience }}
      </el-descriptions-item>
      <el-descriptions-item label="最后更新时间">{{ account.lastUpdateTime }}</el-descriptions-item>
    </el-descriptions>

    <div class="mt-6">
      <el-progress
        :percentage="calculateProgress(account.experience, account.level.maxExperience)"
        :color="getProgressColor(account.level.levelName)"
        :show-text="false"
        :stroke-width="12"
      />
      <div class="flex justify-between mt-2 text-sm text-gray-500">
        <span>{{ account.level.levelName }}</span>
        <span>{{ account.experience }}/{{ account.level.maxExperience }}</span>
        <span>{{ account.level.nextLevel ? account.level.nextLevel.levelName : '最高等级' }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  account: {
    type: Object,
    required: true,
  },
});

// 计算进度百分比
const calculateProgress = (current, max) => {
  return Math.min(Math.round((current / max) * 100), 100);
};

// todo 数据后台存储
// 获取等级标签样式
const getLevelTag = (levelName) => {
  const map = {
    '青铜': '',
    '白银': 'info',
    '黄金': 'warning',
    '铂金': 'success',
    '钻石': 'danger',
  };
  return map[levelName] || '';
};

// 获取进度条颜色
const getProgressColor = (levelName) => {
  const map = {
    '青铜': '#cd7f32',
    '白银': '#c0c0c0',
    '黄金': '#ffd700',
    '铂金': '#e5e4e2',
    '钻石': '#b9f2ff',
  };
  return map[levelName] || '#409eff';
};
</script>

<style lang="scss" scoped>
.account-basic-info {
  :deep(.el-descriptions__body) {
    background-color: #fafafa;
  }

  :deep(.el-descriptions__label) {
    width: 120px;
    font-weight: bold;
  }
}
</style>
