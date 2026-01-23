<template>
  <div class="chat-detail-view">
    <el-page-header @back="goBack" title="返回"/>

    <div class="detail-container">
      <div class="user-info">
        <el-avatar :src="targetUser.avatar" :size="80"/>
        <div class="info-text">
          <h3>{{ targetUser.nickname }}</h3>
          <p>{{ targetUser.bio || '暂无简介' }}</p>
        </div>
      </div>

      <div class="chat-room-container">
        <ChatRoom
            :session-id="sessionId"
            :session-detail="sessionDetail"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getUserInfo } from '@/api/user.js';
import { getSessionDetail } from '@/api/session';
import { localStores } from '@/stores/localStores.js';
import ChatRoom from './ChatRoom.vue';

const route = useRoute();
const router = useRouter();
const store = localStores();

const sessionId = ref(parseInt(route.params.sessionId));
const targetUser = ref({});
const sessionDetail = ref({});

// 获取当前用户ID
const currentUserId = computed(() => {
  const id = store.userInfo.userInfo?.userId;
  if (id === undefined || id === null) {
    return null;
  }
  const numericId = Number(id);
  return Number.isNaN(numericId) ? null : numericId;
});

// 获取会话详情
const fetchSessionDetail = async () => {
  try {
    const res = await getSessionDetail(sessionId.value);
    sessionDetail.value = res.data;

    // 获取对方用户信息
    const userId = sessionDetail.value.members?.find(
        m => m.userId !== currentUserId.value
    )?.userId;

    if (userId) {
      const userRes = await getUserInfo(userId);
      targetUser.value = userRes.data;
    }
  } catch (error) {
    console.error('获取会话详情失败:', error);
  }
};

// 返回上一页
const goBack = () => {
  router.go(-1);
};

onMounted(() => {
  fetchSessionDetail();
});
</script>

<style lang="scss" scoped>
.chat-detail-view {
  padding: 20px;

  .detail-container {
    margin-top: 20px;

    .user-info {
      display: flex;
      align-items: center;
      padding: 20px;
      border-bottom: 1px solid #eee;

      .info-text {
        margin-left: 20px;

        h3 {
          margin: 0;
          font-size: 24px;
        }

        p {
          margin: 8px 0 0;
          color: #666;
        }
      }
    }

    .chat-room-container {
      margin-top: 20px;
      height: calc(100vh - 200px);
    }
  }
}
</style>
