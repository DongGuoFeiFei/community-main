<template>
  <div class="user-search">
    <el-input
        v-model="searchQuery"
        placeholder="搜索用户"
        clearable
        @input="handleSearch"
    >
      <template #prefix>
        <el-icon><search /></el-icon>
      </template>
    </el-input>

    <div class="search-results">
      <div
          v-for="user in searchResults"
          :key="user.id"
          class="user-item"
          @click="handleUserSelect(user)"
      >
        <el-avatar :src="user.avatar" :size="36" />
        <div class="user-info">
          <div class="user-name">{{ user.nickname }}</div>
          <div class="user-desc">{{ user.bio || '暂无简介' }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { searchUsers } from '@/api/user';

const emit = defineEmits(['user-selected']);

const searchQuery = ref('');
const searchResults = ref([]);
const loading = ref(false);

// 搜索用户
const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    searchResults.value = [];
    return;
  }

  try {
    loading.value = true;
    const res = await searchUsers(searchQuery.value);
    searchResults.value = res.data;
  } catch (error) {
    console.error('搜索用户失败:', error);
  } finally {
    loading.value = false;
  }
};

// 选择用户
const handleUserSelect = (user) => {
  emit('user-selected', user);
  searchQuery.value = '';
  searchResults.value = [];
};
</script>

<style lang="scss" scoped>
.user-search {
  .search-results {
    margin-top: 12px;
    max-height: 300px;
    overflow-y: auto;

    .user-item {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f5f5f5;
      }

      .user-info {
        margin-left: 12px;

        .user-name {
          font-weight: 500;
        }

        .user-desc {
          font-size: 12px;
          color: #999;
        }
      }
    }
  }
}
</style>
