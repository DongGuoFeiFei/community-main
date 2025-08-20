<template>
  <div class="points-management">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="积分规则" name="rules">
        积分规则
      </el-tab-pane>
      <el-tab-pane label="等级管理" name="levels">
        等级管理
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import {ref} from 'vue'

const activeTab = ref('rules')
</script>
