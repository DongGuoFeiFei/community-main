<script setup>

import EditorView from "@/views/pages/views/edit/EditorView.vue";
import Header from "@/views/pages/components/Header.vue";
import Editor from "@/views/pages/views/edit/Editor.vue";
</script>

<template>
  <el-container>
    <el-header>
      <Header/>
    </el-header>
    <el-main>
      <EditorView/>
    </el-main>
  </el-container>
</template>