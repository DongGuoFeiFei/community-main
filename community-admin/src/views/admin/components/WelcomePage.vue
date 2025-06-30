<template>
  <div class="welcome-container">
    <!-- 顶部欢迎区域 -->
    <el-card class="welcome-header">
      <div class="header-content">
        <el-avatar :size="80" :src="store.baseURL+userInfo.avatarUrl" class="user-avatar">
          <el-icon :size="40">
            <User/>
          </el-icon>
        </el-avatar>
        <div class="welcome-text">
          <h1>欢迎回来，{{ userInfo.userInfo.nickname || userInfo.userInfo.username }}！</h1>
          <p class="sub-title">今天是 {{ currentDate }}，祝您工作愉快！</p>
        </div>
      </div>
    </el-card>
  </div>

</template>

<script setup>
import {computed} from 'vue'
import {User} from '@element-plus/icons-vue'
import {localStores} from '@/stores/localStores'
import dayjs from 'dayjs'

// 从localStore获取用户信息
const store = localStores()
const userInfo = computed(() => store.userInfo)

// 当前日期
const currentDate = computed(() => dayjs().format('YYYY年MM月DD日 dddd'))

</script>

<style lang="scss" scoped>
.welcome-container {
  padding: 20px;

  .welcome-header {
    margin-bottom: 20px;

    .header-content {
      display: flex;
      align-items: center;
      padding: 20px;

      .user-avatar {
        margin-right: 20px;
        background-color: var(--el-color-primary-light-7);
      }

      .welcome-text {
        h1 {
          margin: 0;
          font-size: 24px;
          color: var(--el-text-color-primary);
        }

        .sub-title {
          margin: 8px 0 0;
          font-size: 14px;
          color: var(--el-text-color-secondary);
        }
      }
    }
  }
}
</style>