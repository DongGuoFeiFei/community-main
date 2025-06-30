<template>
  <div class="recent-activities">
    <h3 class="section-title">最近活动</h3>

    <el-timeline>
      <el-timeline-item
          v-for="(activity, index) in activities"
          :key="index"
          :timestamp="activity.time"
          :type="getActivityType(activity.type)"
          placement="top"
      >
        <el-card shadow="hover">
          <div class="activity-content">
            <div class="activity-message">{{ activity.message }}</div>
            <div class="activity-user">{{ activity.user }}</div>
          </div>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <div class="view-all" @click="viewAllActivities">
      <el-button link>查看全部</el-button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  activities: {
    type: Array,
    default: () => []
  }
});

const router = useRouter();

const getActivityType = (type) => {
  const types = {
    'login': 'primary',
    'post': 'success',
    'comment': 'warning',
    'delete': 'danger',
    'update': 'info'
  };
  return types[type] || '';
};

const viewAllActivities = () => {
  router.push('/activities');
};
</script>

<style lang="scss" scoped>
.recent-activities {
  .section-title {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 18px;
    color: var(--el-text-color-primary);
  }

  .activity-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .activity-message {
      font-size: 14px;
    }

    .activity-user {
      font-size: 12px;
      color: var(--el-text-color-secondary);
    }
  }

  .view-all {
    text-align: center;
    margin-top: 10px;
  }
}
</style>