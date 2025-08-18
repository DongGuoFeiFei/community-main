<template>
  <div class="category-management">
    <el-row :gutter="10">
      <el-col :span="16">
        <category-list/>
      </el-col>
      <el-col :span="8">
        <category-tree @edit="handleTreeEdit"/>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>

import CategoryList from "@/views/admin/views/category/components/CategoryList.vue";
import CategoryTree from "@/views/admin/views/category/components/CategoryTree.vue";

const handleTreeEdit = (category) => {
  console.log("category", category)
};
</script>

<style lang="scss" scoped>
.category-management {
  padding: 20px;
}
</style>
