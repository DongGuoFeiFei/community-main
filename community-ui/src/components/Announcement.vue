<script setup lang="js">
import { ref, onMounted } from 'vue';
import { getLatestAnnouncement } from '@/api/announcement.js';
import { ElIcon } from 'element-plus';
import {Close, InfoFilled} from '@element-plus/icons-vue';

// 公告数据
const announcement = ref(null);
// 是否显示公告
const showAnnouncement = ref(false);

// 从localStorage中获取已关闭的公告ID
const getDismissedAnnouncements = () => {
  const dismissed = sessionStorage.getItem('dismissedAnnouncements');
  return dismissed ? JSON.parse(dismissed) : [];
};

// 检查公告是否已被关闭
const isAnnouncementDismissed = (id) => {
  const dismissed = getDismissedAnnouncements();
  return dismissed.includes(id);
};

// 关闭公告
const dismissAnnouncement = () => {
  if (announcement.value) {
    const dismissed = getDismissedAnnouncements();
    dismissed.push(announcement.value.id);
    sessionStorage.setItem('dismissedAnnouncements', JSON.stringify(dismissed));
    showAnnouncement.value = false;
  }
};

// 获取最新公告
const fetchLatestAnnouncement = async () => {
  try {
    const data = await getLatestAnnouncement();
    if (data && !isAnnouncementDismissed(data.id)) {
      announcement.value = data;
      showAnnouncement.value = true;
    }
  } catch (error) {
    console.error('获取公告失败:', error);
  }
};

onMounted(() => {
  fetchLatestAnnouncement();
});
</script>

<template>
  <div v-if="showAnnouncement && announcement" class="announcement-container">
    <div class="announcement-content">
      <div class="announcement-icon">
        <el-icon :size="16"><InfoFilled /></el-icon>
      </div>
      <div class="announcement-text">
        <span class="announcement-title">{{ announcement.title }}：</span>
        <span class="announcement-message">{{ announcement.content }}</span>
      </div>
      <div class="announcement-close" @click="dismissAnnouncement">
        <el-icon><Close /></el-icon>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.announcement-container {
  margin-bottom: 16px;
  margin-top: 20px;
  background-color: #e7e0c2;
  border-radius: 4px;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  position: relative;

  .announcement-content {
    display: flex;
    align-items: center;
    width: 100%;
  }

  .announcement-icon {
    margin-right: 12px;
    color: #000;
  }

  .announcement-text {
    flex: 1;
    font-size: 14px;
    line-height: 1.5;
    color: #333;

    .announcement-title {
      font-weight: bold;
    }

    .announcement-message {
      margin-left: 4px;
    }
  }

  .announcement-close {
    margin-left: 12px;
    cursor: pointer;
    color: #999;
    transition: color 0.2s;

    &:hover {
      color: #666;
    }
  }
}
</style>