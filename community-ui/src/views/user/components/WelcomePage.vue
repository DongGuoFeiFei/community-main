<template>
  <div class="user-welcome-page">
    <!-- 用户欢迎卡片 -->
    <el-card class="welcome-card">
      <div class="welcome-header">
        <el-avatar :size="80" :src="userInfo.avatar || defaultAvatar"/>
        <div class="welcome-text">
          <h2 class="welcome-title">欢迎回来, {{ userInfo.nickname || userInfo.username }}!</h2>
          <p class="welcome-subtitle">今天是 {{ currentDate }}，祝您度过愉快的一天</p>
        </div>
      </div>

      <!-- 用户数据概览 -->
      <div class="user-stats">
        <div class="stat-item">
          <div class="stat-value">{{ userStats.postCount || 0 }}</div>
          <div class="stat-label">发布的帖子</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ userStats.followers || 0 }}</div>
          <div class="stat-label">粉丝</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ userStats.following || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ userStats.likes || 0 }}</div>
          <div class="stat-label">获赞</div>
        </div>
      </div>
    </el-card>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <h3 class="section-title">快速开始</h3>
      <div class="actions-grid">
        <el-card
            shadow="hover"
            class="action-card"
            @click="navigateTo('create-post')"
        >
          <el-icon :size="36">
            <Edit/>
          </el-icon>
          <span>发布新内容</span>
        </el-card>
        <el-card
            shadow="hover"
            class="action-card"
            @click="navigateTo('messages')"
        >
          <el-icon :size="36">
            <Message/>
          </el-icon>
          <span>查看消息</span>
        </el-card>
        <el-card
            shadow="hover"
            class="action-card"
            @click="navigateTo('settings')"
        >
          <el-icon :size="36">
            <Setting/>
          </el-icon>
          <span>账户设置</span>
        </el-card>
        <el-card
            shadow="hover"
            class="action-card"
            @click="navigateTo('bookmarks')"
        >
          <el-icon :size="36">
            <Star/>
          </el-icon>
          <span>收藏内容</span>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import {useRouter} from 'vue-router'
import {Edit, Message, Setting, Star} from '@element-plus/icons-vue'
import {localStores} from '@/stores/localStores.js'
import {getUserStats} from '@/api/user'

// 默认头像
const defaultAvatar = ref('/images/default-avatar.png')

// 获取用户信息和状态
const store = localStores()
const userInfo = computed(() => store.userInfo)

// 用户统计数据
const userStats = ref({
  postCount: 0,
  followers: 0,
  following: 0,
  likes: 0
})

// 最近活动
const recentActivities = ref([
  {time: '2023-05-15 14:30', content: '您发布了新帖子《Vue3最佳实践》'},
  {time: '2023-05-14 09:15', content: '您的评论获得了3个赞'},
  {time: '2023-05-13 18:45', content: '您关注了用户"前端开发者"'}
])

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})

const router = useRouter()

// 导航方法
const navigateTo = (path) => {
  router.push({name: path})
}

// 加载用户数据
onMounted(async () => {
  try {
    const response = await getUserStats(userInfo.value.id)
    if (response.code === 200) {
      userStats.value = response.data
    }
  } catch (error) {
    console.error('获取用户统计数据失败:', error)
  }
})
</script>

<style lang="scss" scoped>
.user-welcome-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .welcome-card {
    margin-bottom: 24px;
    border-radius: 12px;

    .welcome-header {
      display: flex;
      align-items: center;
      padding: 20px;
      gap: 24px;

      .welcome-text {
        .welcome-title {
          margin: 0;
          font-size: 24px;
          color: var(--el-text-color-primary);
        }

        .welcome-subtitle {
          margin: 8px 0 0;
          font-size: 14px;
          color: var(--el-text-color-secondary);
        }
      }
    }

    .user-stats {
      display: flex;
      justify-content: space-around;
      padding: 20px 0;
      border-top: 1px solid var(--el-border-color-light);

      .stat-item {
        text-align: center;
        padding: 0 15px;

        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: var(--el-color-primary);
        }

        .stat-label {
          font-size: 14px;
          color: var(--el-text-color-secondary);
          margin-top: 8px;
        }
      }
    }
  }

  .quick-actions {
    margin-bottom: 24px;

    .section-title {
      font-size: 18px;
      margin-bottom: 16px;
      color: var(--el-text-color-primary);
    }

    .actions-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 16px;

      .action-card {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 120px;
        cursor: pointer;
        transition: all 0.3s ease;
        border-radius: 8px;

        &:hover {
          transform: translateY(-5px);
          box-shadow: var(--el-box-shadow-dark);
        }

        .el-icon {
          margin-bottom: 12px;
          color: var(--el-color-primary);
        }

        span {
          font-size: 16px;
        }
      }
    }
  }
}
</style>