<script setup>
import {onMounted, ref} from 'vue'
import {localStores} from '@/stores/localStores'
import FollowList from "@/views/user/views/follow&fans/components/FollowList.vue";
import FanList from "@/views/user/views/follow&fans/components/FanList.vue";

const store = localStores()
const userId = ref(store.userInfo.userInfo.userId || '0')
const loading = ref({
  follows: false,
  fans: false
})

const followListRef = ref(null)
const fanListRef = ref(null)

const refreshAll = () => {
  loading.value.follows = true
  loading.value.fans = true

  Promise.all([
    followListRef.value?.fetchFollows(),
    fanListRef.value?.fetchFans()
  ]).finally(() => {
    loading.value.follows = false
    loading.value.fans = false
  })
}

onMounted(() => {
  refreshAll()
})
</script>

<template>
  <div class="follow-fans-container">
    <h2 class="title">我的关系</h2>

    <div class="lists-container">
      <FollowList
          ref="followListRef"
          :userId="Number(userId)"
          :loading="loading.follows"
          @refresh="refreshAll"
      />
      <FanList
          ref="fanListRef"
          :userId="userId"
          :loading="loading.fans"
          @refresh="refreshAll"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.follow-fans-container {
  padding: 20px;
  background-color: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .title {
    margin-bottom: 24px;
    font-size: 20px;
    color: var(--el-text-color-primary);
    text-align: center;
  }

  .lists-container {
    display: flex;
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .lists-container {
    flex-direction: column;
  }
}
</style>