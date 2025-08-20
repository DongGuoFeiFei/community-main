<template>
  <div class="points-management">
    <PointsAccountList />
  </div>
</template>

<script setup>
import PointsAccountList from "@/views/admin/views/pointAccount/components/PointsAccountList.vue";
</script>

<style lang="scss" scoped>
.points-management {
  padding: 20px;
}
</style>
