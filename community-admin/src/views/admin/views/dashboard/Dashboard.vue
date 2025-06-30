<template>
  <div class="dashboard-container">
    <h1 class="dashboard-title">控制台</h1>

    <!-- 数据概览卡片 -->
    <div class="dashboard-cards">
      <DashboardCard
          v-for="card in cards"
          :key="card.title"
          :title="card.title"
          :value="card.value"
          :icon="card.icon"
          :color="card.color"
      />
    </div>

    <!-- 图表区域 -->
    <DashboardCharts
        v-if="showCharts"
        :user-growth="userGrowthData"
        :post-growth="postGrowthData"
    />

    <div class="dashboard-bottom">
      <!-- 最近活动 -->
      <RecentActivities
          class="dashboard-section"
          :activities="recentActivities"
      />


      <!-- 快速操作 -->
      <QuickActions
          class="dashboard-section"
          @action="handleQuickAction"
      />

      <!-- 系统状态 -->
      <SystemStatus
          class="dashboard-section"
          :cpu="systemStatus.cpu"
          :memory="systemStatus.memory"
          :disk="systemStatus.disk"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { sessionStores } from '@/stores/sessionStores';
import { getDashboardData } from '@/api/dashboard';
import RecentActivities from "@/views/admin/views/dashboard/RecentActivities.vue"
import SystemStatus from "@/views/admin/views/dashboard/SystemStatus.vue"
import DashboardCard from "@/views/admin/views/dashboard/DashboardCard.vue";
import DashboardCharts from "@/views/admin/views/dashboard/DashboardCharts.vue";
import QuickActions from "@/views/admin/views/dashboard/QuickActions.vue";

const sessionStore = sessionStores();
const showCharts = ref(true);

// 数据卡片
const cards = ref([
  { title: '总用户数', value: 0, icon: 'User', color: 'primary' },
  { title: '今日新增', value: 0, icon: 'UserFilled', color: 'success' },
  { title: '总帖子数', value: 0, icon: 'Document', color: 'warning' },
  { title: '今日发帖', value: 0, icon: 'EditPen', color: 'danger' },
  { title: '总评论数', value: 0, icon: 'ChatLineRound', color: 'info' },
  { title: '活跃用户', value: 0, icon: 'DataLine', color: '' }
]);

// 最近活动数据
const recentActivities = ref([]);

// 系统状态
const systemStatus = ref({
  cpu: 0,
  memory: 0,
  disk: 0
});

// 图表数据
const userGrowthData = ref([]);
const postGrowthData = ref([]);

// 加载数据
const loadDashboardData = async () => {
  try {
    const res = await getDashboardData();
    if (res.code === 200) {
      const data = res.data;

      // 更新卡片数据
      cards.value[0].value = data.totalUsers;
      cards.value[1].value = data.todayNewUsers;
      cards.value[2].value = data.totalPosts;
      cards.value[3].value = data.todayNewPosts;
      cards.value[4].value = data.totalComments;
      cards.value[5].value = data.activeUsers;

      // 更新最近活动
      recentActivities.value = data.recentActivities;

      // 更新系统状态
      systemStatus.value = data.systemStatus;

      // 更新图表数据
      userGrowthData.value = data.userGrowth;
      postGrowthData.value = data.postGrowth;
    }
  } catch (error) {
    console.error('加载控制台数据失败:', error);
  }
};

// 处理快速操作
const handleQuickAction = (action) => {
  switch (action) {
    case 'clearCache':
      sessionStore.showMessage('正在清除缓存...');
      break;
    case 'backup':
      sessionStore.showMessage('正在备份数据...');
      break;
    case 'refresh':
      loadDashboardData();
      sessionStore.showMessage('数据已刷新');
      break;
  }
};

onMounted(() => {
  loadDashboardData();
});
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .dashboard-title {
    margin-bottom: 20px;
    font-size: 24px;
    color: var(--el-text-color-primary);
  }

  .dashboard-cards {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 20px;
    margin-bottom: 20px;
  }

  .dashboard-bottom {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;

    @media (max-width: 1200px) {
      grid-template-columns: 1fr;
    }
  }

  .dashboard-section {
    background-color: var(--el-bg-color);
    border-radius: 8px;
    padding: 20px;
    box-shadow: var(--el-box-shadow-light);
  }
}
</style>