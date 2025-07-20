<script setup>

import Header from "@/views/pages/components/Header.vue";
import {onBeforeUnmount, onMounted, ref, watch} from "vue";
import EditorView from "@/views/pages/views/edit/EditorView.vue";
import TiptapEditor from "@/views/pages/views/edit/components/TiptapEditor.vue";

const editorHtml = ref('<p>tiptap editor demo.🎉</p>')

// let updateTimer = null

// 初始化定时器
// const initUpdateTimer = () => {
//   updateTimer = setInterval(() => {
//     const now = new Date()
//     editorHtml.value = `
//       <div>
//         <h3>自动更新内容 (${now.toLocaleTimeString()})</h3>
//         <p>这是自动更新的编辑器内容</p>
//         <ul>
//           <li>随机值: ${Math.random().toFixed(4)}</li>
//           <li>时间戳: ${now.getTime()}</li>
//         </ul>
//       </div>
//     `
//   }, 1000) // 10秒更新一次
// }
//
// onMounted(() => {
//   initUpdateTimer()
// })
//
// onBeforeUnmount(() => {
//   // 组件卸载时清除定时器
//   if (updateTimer) {
//     clearInterval(updateTimer)
//     updateTimer = null
//   }
// })

watch(
    () => editorHtml.value,
    (newHtml) => {
      console.log(newHtml)
    }
)
</script>

<template>
  <el-container>
    <el-header>
      <Header/>
    </el-header>
    <el-main>
<!--            <tiptap-editor v-model:html="editorHtml"/>-->
      <editor-view/>
    </el-main>
  </el-container>
</template>