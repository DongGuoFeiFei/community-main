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
import {getPointsAccountDetail} from "@/api/points.js";
import {localStores} from "@/stores/localStores.js";
import PointsAccountOverview from "@/views/user/pointAccount/components/PointsAccountOverview.vue";
import PointsLevelInfo from "@/views/user/pointAccount/components/PointsLevelInfo.vue";
import PointsTransactions from "@/views/user/pointAccount/components/PointsTransactions.vue";

const store = localStores()
const route = useRoute();
const router = useRouter();
const userId = ref(store.userInfo.userInfo.userId);
const refreshKey = ref(0);

const accountData = ref();

const loadAccountInfo = async () => {
  try {
    const response = await getPointsAccountDetail(userId.value);
    accountData.value = response.data;
    console.log(accountData.value)
  } catch (error) {
    console.error('Failed to load points account:', error);
  }
};

onMounted(() => {
  loadAccountInfo();
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
