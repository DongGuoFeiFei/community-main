<script setup>
import { ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { delFollowAuthor, getFollowingList } from "@/api/follow.js";

const props = defineProps({
  userId: {
    type: Number,
    required: true,
  },
  loading: {
    type: Boolean,
    default: false,
  },
});

const emits = defineEmits(["refresh"]);

const follows = ref([]);

const fetchFollows = async () => {
  try {
    const res = await getFollowingList(props.userId);
    follows.value = res.data || [];
    console.log("follows.value", follows.value);
  } catch (error) {
    ElMessage.error(error.message || "获取关注列表失败");
  }
};

const handleUnfollow = async (userIdToUnfollow) => {
  try {
    await ElMessageBox.confirm("确定要取消关注吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });

    await delFollowAuthor(userIdToUnfollow);
    ElMessage.success("取消关注成功");
    emits("refresh");
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error(error.message || "取消关注失败");
    }
  }
};

defineExpose({
  fetchFollows,
});
</script>

<template>
  <div class="list-column">
    <h3 class="list-title">关注列表</h3>
    <el-skeleton :loading="loading" animated>
      <template #template>
        <div class="skeleton-item" v-for="i in 5" :key="`follow-skeleton-${i}`">
          <el-skeleton-item variant="circle" class="avatar" />
          <el-skeleton-item variant="text" class="name" />
          <el-skeleton-item variant="button" class="action" />
        </div>
      </template>
      <template #default>
        <div class="empty-tip" v-if="follows.length === 0">暂无关注用户</div>
        <div
          class="user-item"
          v-for="user in follows"
          :key="`follow-${user.id}`"
        >
          <RouterLink :to="`/author/${user.id}`" target="_blank">
            <div class="user-info">
              <el-avatar :src="user.avatar" class="avatar" />
              <div class="user-details">
                <span class="username">{{ user.nickname }}</span>
                <span class="user-bio">{{
                  user.bio !== null ? user.bio : "暂无简介"
                }}</span>
              </div>
            </div>
          </RouterLink>
          <el-button
            type="danger"
            size="small"
            @click="handleUnfollow(user.id)"
          >
            取消关注
          </el-button>
        </div>
      </template>
    </el-skeleton>
  </div>
</template>

<style scoped lang="scss">
.list-column {
  flex: 1;
  min-width: 0;
  padding: 16px;
  background-color: var(--el-bg-color-page);
  border-radius: 6px;
  // 确保高度自适应，不产生内部滚动条
  height: auto;
  max-height: none;
  overflow: visible;

  .list-title {
    margin-bottom: 16px;
    font-size: 16px;
    color: var(--el-text-color-primary);
    text-align: center;
  }

  .skeleton-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid var(--el-border-color-light);

    .avatar {
      width: 40px;
      height: 40px;
      margin-right: 12px;
    }

    .name {
      flex: 1;
      height: 16px;
      margin-right: 12px;
    }

    .action {
      width: 80px;
      height: 32px;
    }
  }

  .empty-tip {
    padding: 20px;
    text-align: center;
    color: var(--el-text-color-secondary);
  }

  .user-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 0;
    border-bottom: 1px solid var(--el-border-color-light);

    .user-info {
      display: flex;
      align-items: center;
      flex: 1;
      min-width: 0;

      .avatar {
        margin-right: 12px;
      }

      .user-details {
        display: flex;
        flex-direction: column;
        flex: 1;
        min-width: 0;

        .username {
          font-weight: 500;
          margin-bottom: 4px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          color: var(--el-text-color-primary);
        }

        .user-bio {
          font-size: 12px;
          color: var(--el-text-color-secondary);
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
  }
}

// 确保 el-skeleton 组件不产生滚动条
:deep(.el-skeleton) {
  overflow: visible !important;
  height: auto !important;
  max-height: none !important;
}
</style>
