<!-- /components/editor/MarkdownEditor.vue -->
<template>
  <div id="vditor" ref="vditorRef"></div>
</template>

<script setup lang="ts">
import {onMounted, onUnmounted, ref, watch} from "vue";
import Vditor from "vditor";
import "vditor/dist/index.css";
import config from "@/config.js";
import {ElLoading} from "element-plus";
import request from "@/utils/request.js";
import {useDebounceFn} from '@vueuse/core';

const props = defineProps({
  modelValue: {
    type: String,
    default: '朋友，有趣的故事，你来分享🎉️！',
    required: false
  },
  baseUrl: {
    type: String,
    default: config.apiBaseUrl,
    required: false
  },
  height: {
    type: Number,
    default: 450,
    required: false
  },
  mode: {
    type: String,
    default: 'ir',
    required: false
  }
});

const emit = defineEmits(['update:modelValue']);

// 编辑器文本内容
const vditorRef = ref(null);
// 编辑器实例
const vditorInstance = ref(null);

// 图片上传方法
const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);

  try {
    const res = await request({
      url: '/files/upload',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
        'token': localStorage.getItem('token')
      }
    });

    if (res.code === 200) {
      return `${props.baseUrl}${res.data.accessUrl}`;
    }
    throw new Error(res.msg || '文件上传失败');
  } catch (error) {
    console.error('上传失败:', error);
    throw error;
  }
};

// 初始化编辑器
const initEditor = () => {
  const loading = ElLoading.service({
    lock: true,
    text: '正在加载编辑器...',
    background: 'rgba(255, 255, 255, 0.8)',
  });

  const emitUpdate = useDebounceFn((value) => {
    emit('update:modelValue', value);
  }, 300); // 300ms防抖


  vditorInstance.value = new Vditor("vditor", {
    mode: props.mode,
    height: props.height,
    value: props.modelValue,
    toolbarConfig: {pin: true},
    toolbar: [
      "emoji", "headings", "bold", "italic", "strike", "link", "|",
      "list", "ordered-list", "check", "outdent", "indent", "|",
      "quote", "line", "|", "upload", "|",
      "undo", "redo", "|", "outline", "fullscreen",
    ],
    upload: {
      accept: "image/*",
      max: 10 * 1024 * 1024,
      multiple: false,
      handler: async (files) => {
        try {
          const url = await uploadFile(files[0]);
          const markdown = `![${files[0].name.replace(/\.[^/.]+$/, "")}](${url})`;
          vditorInstance.value.insertValue(markdown);
          return markdown;
        } catch (error) {
          console.error('上传失败:', error);
          return '';
        }
      }
    },
    counter: {enable: true},
    cache: {enable: true},
    input: (value) => emitUpdate(value),
    after: () => {
      loading.close();
    },
  });
};

defineExpose({
  vditorInstance
});

onUnmounted(() => {
  if (vditorInstance.value) {
    vditorInstance.value.destroy();
    vditorInstance.value = null;
  }
});

// 监听props变化更新编辑器内容
watch(() => props.modelValue, (newValue) => {
  if (vditorInstance.value && newValue !== vditorInstance.value.getValue()) {
    vditorInstance.value.setValue(newValue);
  }
});
// 编辑器初始化
onMounted(() => {
  initEditor();
});
</script>