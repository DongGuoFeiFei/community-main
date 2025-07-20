<template>
  <div class="editor" v-if="editor" :style="{ width }">
    <MenuBar v-if="editor" class="editor-header" :editor="editor" :upload-image="handleImageUpload"/>
    <editor-content class="editor-content" :editor="editor"/>
  </div>
</template>

<script setup>
import {EditorContent, useEditor} from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import {onBeforeUnmount, watch} from 'vue'
import MenuBar from './MenuBar.vue'
import Highlight from '@tiptap/extension-highlight'
import Image from '@tiptap/extension-image'
import {Table} from '@tiptap/extension-table'
import {TableRow} from '@tiptap/extension-table-row'
import {TableCell} from '@tiptap/extension-table-cell'
import {TableHeader} from '@tiptap/extension-table-header'
import {uploadFile} from '@/api/files'
import env from "@/utils/env.js";

const props = defineProps({
  html: {
    type: String,
    default: ''
  },
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '300px'
  }
})

const emit = defineEmits(['update:html'])

const CustomTableCell = TableCell.extend({
  addAttributes() {
    return {
      ...this.parent?.(),
      backgroundColor: {
        default: null,
        parseHTML: (element) => element.getAttribute('data-background-color'),
        renderHTML: (attributes) => ({
          'data-background-color': attributes.backgroundColor,
          style: `background-color: ${attributes.backgroundColor}`
        })
      }
    }
  }
})

const editor = useEditor({
  content: props.html,
  extensions: [
    StarterKit,
    Image.configure({
      allowBase64: false,
    }),
    Highlight.configure({multicolor: true}),
    Table.configure({
      resizable: true
    }),
    TableRow,
    TableHeader,
    CustomTableCell
  ],
  onUpdate: () => {
    console.log(editor.value.getHTML())
    emit('update:html', editor.value.getHTML())
  }
})

const handleImageUpload = async (file) => {
  try {
    const url = await uploadFile(file);
    editor.value.chain().focus().setImage({src: env.apiBaseUrl + url}).run();
  } catch (error) {
    console.error('图片上传失败:', error);
    // 可以在这里添加错误提示
  }
}

watch(() => props.html,
    (newValue) => {
      if (editor.value && newValue !== editor.value.getHTML()) {
        editor.value.commands.setContent(newValue, false)
      }
    },
    {immediate: true}
)

onBeforeUnmount(() => {
  if (editor.value) {
    editor.value.destroy()
  }
})
</script>

<style lang="scss">
.editor {
  display: flex;
  flex-direction: column;
  max-height: 26rem;
  color: #0d0d0d;
  background-color: #fff;
  border: 3px solid #0d0d0d;
  border-radius: .75rem;

  &-header {
    display: flex;
    align-items: center;
    flex: 0 0 auto;
    flex-wrap: wrap;
    padding: .25rem;
    border-bottom: 3px solid #0d0d0d;
  }

  &-content {
    padding: .7rem .5rem;
    flex: 1 1 auto;
    overflow-x: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }
}

/* 基本编辑器样式 */
.ProseMirror {
  height: 100%;

  &:focus {
    outline: none;
  }

  ul,
  ol {
    padding: 0 1rem;
  }

  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    line-height: 1.1;
  }

  code {
    background-color: rgba(#616161, .1);
    color: #616161;
  }

  pre {
    background: #0d0d0d;
    color: #fff;
    font-family: 'JetBrainsMono', monospace;
    padding: .75rem 1rem;
    border-radius: .5rem;

    code {
      color: inherit;
      padding: 0;
      background: none;
      font-size: .8rem;
    }
  }

  mark {
    background-color: #faf594;
  }

  img {
    max-width: 100%;
    height: auto;
  }

  hr {
    margin: 1rem 0;
  }

  blockquote {
    padding-left: 1rem;
    border-left: 2px solid rgba(#0d0d0d, .1);
  }

  hr {
    border: none;
    border-top: 2px solid rgba(#0d0d0d, .1);
    margin: 2rem 0;
  }

  ul[data-type="taskList"] {
    list-style: none;
    padding: 0;

    li {
      display: flex;
      align-items: center;

      > label {
        flex: 0 0 auto;
        margin-right: .5rem;
        user-select: none;
      }

      > div {
        flex: 1 1 auto;
      }
    }
  }
}
</style>