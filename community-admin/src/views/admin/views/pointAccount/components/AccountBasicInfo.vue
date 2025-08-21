<template>
  <div class="account-basic-info" v-if="processedAccount">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="账户ID">{{ processedAccount.accountId }}</el-descriptions-item>
      <el-descriptions-item label="用户ID">{{ processedAccount.userId }}</el-descriptions-item>
      <el-descriptions-item label="用户昵称">
        {{ processedAccount.user.nickname }}
      </el-descriptions-item>
      <el-descriptions-item label="用户名">
        {{ processedAccount.user.username }}
      </el-descriptions-item>
      <el-descriptions-item label="累计积分">
        <el-tag type="info">{{ processedAccount.totalPoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="可用积分">
        <el-tag type="success">{{ processedAccount.availablePoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="已消费积分">
        <el-tag>{{ processedAccount.consumedPoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="冻结积分">
        <el-tag type="warning">{{ processedAccount.frozenPoints }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="当前等级">
        <el-tag :type="getLevelTag(processedAccount.level.levelName)">
          {{ processedAccount.level.levelName }}
          <el-icon v-if="processedAccount.level.levelIcon" class="ml-1">
<!--            <img :src="processedAccount.level.levelIcon" class="h-4 w-4"/>-->
          </el-icon>
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="当前经验值">{{
          processedAccount.experience
        }}
      </el-descriptions-item>
      <el-descriptions-item label="升级所需经验">
        {{ processedAccount.experience }}/{{ processedAccount.level.maxExperience }}
      </el-descriptions-item>
      <el-descriptions-item label="最后更新时间">{{
          processedAccount.lastUpdateTime
        }}
      </el-descriptions-item>
    </el-descriptions>

    <div class="mt-6">
      <el-progress
        :percentage="calculateProgress(processedAccount.experience, processedAccount.level.maxExperience)"
        :color="getProgressColor(processedAccount.level.levelName)"
        :show-text="false"
        :stroke-width="12"
      />
      <div class="flex justify-between mt-2 text-sm text-gray-500">
        <span>{{ processedAccount.level.levelName }}</span>
        <span>{{ processedAccount.experience }}/{{ processedAccount.level.maxExperience }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed} from 'vue';

const props = defineProps({
  account: {
    type: Object,
    required: true,
  },
});

// 数据预处理
const processedAccount = computed(() => {
  if (!props.account) return null;

  return {
    ...props.account,
    // 统一字段命名
    level: props.account.pointsLevel || {
      levelName: '未知等级',
      maxExperience: 0,
      levelIcon: ''
    },
    user: props.account.user || {
      nickname: '',
      username: ''
    }
  };
});

// 获取下一等级名称
const getNextLevelName = (level) => {
  return level.nextLevel?.levelName || '最高等级';
};

// 其他方法保持不变...
const calculateProgress = (current, max) => {
  return Math.min(Math.round((current / max) * 100), 100);
};

const getLevelTag = (levelName) => {
  const map = {
    '初级': '',
    '中级': 'info',
    '高级': 'warning',
    '铂金': 'success',
    '钻石': 'danger',
  };
  return map[levelName] || '';
};

const getProgressColor = (levelName) => {
  const map = {
    '初级': '#cd7f32',
    '中级': '#c0c0c0',
    '高级': '#ffd700',
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

  .ml-1 {
    margin-left: 0.25rem;
  }

  .h-4 {
    height: 1rem;
  }

  .w-4 {
    width: 1rem;
  }
}
</style>
