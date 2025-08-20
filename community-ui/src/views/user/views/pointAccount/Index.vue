<template>
  <div class="user-points-management">

    <div class="management-container">
      <div class="left-section">
        <PointsAccountOverview
            :userId="userId"
        />
        <PointsLevelInfo
            :account="accountData"
        />
      </div>

      <div class="right-section">
        <PointsTransactions
            :userId="userId"
            :key="refreshKey"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {getUserInfo} from '@/api/user';
import {getPointsAccount} from "@/api/points.js";
import {localStores} from "@/stores/localStores.js";
import PointsAccountOverview from "@/views/user/views/pointAccount/components/PointsAccountOverview.vue";
import PointsLevelInfo from "@/views/user/views/pointAccount/components/PointsLevelInfo.vue";
import PointsTransactions from "@/views/user/views/pointAccount/components/PointsTransactions.vue";

const store = localStores()
const route = useRoute();
const router = useRouter();
const userId = ref(store.userInfo.userInfo.userId);
const userInfo = ref({});
const refreshKey = ref(0);

const loadUserInfo = async () => {
  try {
    const response = await getUserInfo(userId.value);
    userInfo.value = response.data;
    loadAccountInfo
  } catch (error) {
    console.error('Failed to load user info:', error);
  }
};

const accountData = ref({
  levelId: 0,
  experience: 0
});

const loadAccountInfo = async () => {
  try {
    const response = await getPointsAccount(props.userId);
    accountData.value = response.data;
  } catch (error) {
    console.error('Failed to load points account:', error);
  }
};

const handleUpdateSuccess = () => {
  refreshKey.value++;
};

onMounted(() => {
  loadUserInfo();
});
</script>

<style scoped lang="scss">
.user-points-management {

  .management-container {
    margin-top: 20px;
    display: flex;
    gap: 20px;

    .left-section {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }

    .right-section {
      flex: 2;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
  }
}

@media (max-width: 992px) {
  .management-container {
    flex-direction: column;

    .left-section, .right-section {
      flex: auto !important;
      width: 100%;
    }
  }
}
</style>
