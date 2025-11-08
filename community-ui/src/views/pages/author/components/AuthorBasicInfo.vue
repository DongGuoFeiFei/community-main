<template>
  <div class="user-basic-info">
    <div class="avatar-container">
      <el-avatar :size="120" :src="props.user.avatar"/>
    </div>

    <div class="info-container">
      <h2>{{ props.user.nickname }}</h2>
      <p class="bio">{{ props.user.bio || '暂无简介' }}</p>

      <div class="meta-info">
        <span><el-icon><Location/></el-icon> {{ props.user.location || '未知地区' }}</span>
        <span><el-icon><Calendar/></el-icon> 加入于 {{ formatJoinDate(props.user.createTime) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {Calendar, Location} from '@element-plus/icons-vue'
import {formatDate} from '@/utils/date.js'

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})
const formatJoinDate = (dateString) => {
  return formatDate(dateString, 'YYYY年MM月')
}
</script>

<style lang="scss" scoped>
.user-basic-info {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 20px;
  background-color: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .avatar-container {
    flex-shrink: 0;
  }

  .info-container {
    flex-grow: 1;

    h2 {
      margin: 0 0 10px 0;
      font-size: 24px;
    }

    .bio {
      margin: 0 0 15px 0;
      color: var(--el-text-color-secondary);
      line-height: 1.5;
    }

    .meta-info {
      display: flex;
      gap: 20px;
      color: var(--el-text-color-secondary);
      font-size: 14px;

      span {
        display: flex;
        align-items: center;
        gap: 5px;
      }
    }
  }
}
</style>
