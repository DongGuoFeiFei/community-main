<template>
  <div class="user-stats">
    <el-card shadow="hover">
      <div class="stat-item">
        <div class="stat-value">{{ stats.postCount || 0 }}</div>
        <div class="stat-label">帖子</div>
      </div>

      <div class="stat-item">
        <div class="stat-value">{{ stats.followingCount || 0 }}</div>
        <div class="stat-label">关注</div>
      </div>

      <div class="stat-item">
        <div class="stat-value">{{ stats.followerCount || 0 }}</div>
        <div class="stat-label">粉丝</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
const props = defineProps({
  stats: {
    type: Object,
    required: true
  }
})
</script>

<style lang="scss" scoped>
.user-stats {
  margin-bottom: 20px;

  .el-card {
    padding: 15px 0;
  }

  .stat-item {
    display: inline-block;
    width: 33.3%;
    text-align: center;

    .stat-value {
      font-size: 20px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 14px;
      color: var(--el-text-color-secondary);
    }
  }
}
</style>