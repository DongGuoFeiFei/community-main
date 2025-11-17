<template>
  <div class="notification-type-nav">
    <div class="nav-header">
      <h3>通知中心</h3>
    </div>

    <div class="nav-list">
      <div
        v-for="type in notificationTypes"
        :key="type.value"
        class="nav-item"
        :class="{ active: activeType === type.value }"
        @click="emit('type-change', type.value)"
      >
        <div class="type-icon" :style="{ backgroundColor: type.color }"></div>
        <span class="type-label">{{ type.label }}</span>
        <span v-if="unreadCounts[type.value]" class="unread-badge">
          {{ unreadCounts[type.value] }}
        </span>
      </div>
    </div>

    <div class="nav-footer">
      <el-button link @click="emit('mark-all-read')">
        <el-icon>
          <CircleCheck />
        </el-icon>
        <span>全部标记为已读</span>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { CircleCheck } from "@element-plus/icons-vue";

const props = defineProps({
  activeType: {
    type: String,
    required: true,
  },
  unreadCounts: {
    type: Object,
    default: () => ({
      like: 0,
      comment: 0,
      article: 0,
      follow: 0,
      system: 0,
      favorite: 0,
      reply: 0,
      favoriteArticle: 0,
      privateMessages: 0,
    }),
  },
});

const emit = defineEmits(["type-change", "mark-all-read"]);

const notificationTypes = [
  { value: "like", label: "点赞", color: "#FF4757" },
  { value: "comment", label: "评论", color: "#2ED573" },
  { value: "reply", label: "回复", color: "#FFD700" },
  { value: "follow", label: "关注", color: "#9C51B6" },
  // {value: 'system', label: '系统', color: '#FF7F50'},
  // {value: 'favorite', label: '收藏', color: '#25CCF7'},
  // {value: 'favorite', label: '私信', color: '#25CCF7'},
  { value: "favoriteArticle", label: "文章收藏", color: "#58B19F" },
];
</script>

<style lang="scss" scoped>
.notification-type-nav {
  display: flex;
  flex-direction: column;
  height: 100%;

  .nav-header {
    padding: 16px 20px;
    border-bottom: 1px solid #e6e6e6;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }
  }

  .nav-list {
    flex-grow: 1;
    overflow-y: visible; // 禁止内部滚动，使用浏览器滚动
    padding: 8px 0;
  }

  .nav-item {
    display: flex;
    align-items: center;
    padding: 12px 20px;
    cursor: pointer;
    transition: all 0.2s;
    position: relative;

    &:hover {
      background-color: #f0f2f5;
    }

    &.active {
      background-color: #e6f7ff;
      border-right: 3px solid #1890ff;

      .type-label {
        color: #1890ff;
        font-weight: 500;
      }
    }

    .type-icon {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-right: 12px;
    }

    .type-label {
      font-size: 14px;
      color: #333;
      flex-grow: 1;
    }

    .unread-badge {
      background-color: #f5222d;
      color: white;
      border-radius: 10px;
      padding: 0 6px;
      font-size: 12px;
      line-height: 18px;
      min-width: 18px;
      text-align: center;
    }
  }

  .nav-footer {
    padding: 12px 20px;
    border-top: 1px solid #e6e6e6;

    .el-button {
      width: 100%;
      justify-content: flex-start;
      color: #666;

      &:hover {
        color: #1890ff;
      }

      .el-icon {
        margin-right: 8px;
      }
    }
  }
}
</style>
