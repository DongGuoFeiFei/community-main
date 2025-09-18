<template>
  <div class="chat-view">
    <div class="chat-container">
      <div class="sidebar">
        <ChatList
            :active-session-id="Number(activeSessionId)"
            @session-change="handleSessionChange"
        />
      </div>

      <div class="main-content">
        <div v-if="activeSessionId&&currentSessionDetail" class="chat-room-wrapper">
          <ChatRoom
              :session-id="Number(activeSessionId)"
              :session-detail="currentSessionDetail"
          />
        </div>

        <div v-else class="empty-state">
          <el-empty description="请选择一个会话或创建新会话"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import ChatList from './ChatList.vue';
import ChatRoom from './ChatRoom.vue';
import {getSessionDetail} from '@/api/session';

const activeSessionId = ref(null);
const currentSessionDetail = ref();
const loadingSession = ref(false);
// 处理会话切换
const handleSessionChange = async (sessionId) => {
  activeSessionId.value = sessionId;
  loadingSession.value = true;
  console.log(sessionId)
  try {
    const res = await getSessionDetail(sessionId);
    currentSessionDetail.value = res.data;
    console.log(res.data)
    console.log(currentSessionDetail.value)
  } catch (error) {
    console.error('获取会话详情失败:', error);
  }
};
</script>

<style lang="scss" scoped>
.chat-view {
  height: 100vh;
  display: flex;

  .chat-container {
    flex: 1;
    display: flex;
    height: 100%;

    .sidebar {
      width: 280px;
      border-right: 1px solid #eee;
      height: 100%;
    }

    .main-content {
      flex: 1;
      height: 100%;

      .chat-room-wrapper {
        height: 100%;
      }

      .empty-state {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
      }
    }
  }
}
</style>
