<script setup>

import Header from "@/views/pages/components/Header.vue";
import EditorView from "@/views/pages/views/edit/EditorView.vue";
</script>

<template>
  <el-container>
    <el-header>
      <Header/>
    </el-header>
    <el-main>
      <editor-view/>
    </el-main>
  </el-container>
</template>