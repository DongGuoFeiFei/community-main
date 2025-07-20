<template>
  <div v-if="editor">
    <template v-for="(item, index) in items" :key="index">
      <div class="divider" v-if="item.type === 'divider'" />
      <MenuItem v-else v-bind="item" />
    </template>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import MenuItem from "@/views/pages/views/edit/components/MenuItem.vue";

const props = defineProps({
  editor: {
    type: Object,
    required: true
  },
  uploadImage: {
    type: Function,
    required: true
  }
})

const items = reactive([
  {
    icon: 'bold',
    title: '加粗',
    action: () => props.editor.chain().focus().toggleBold().run(),
    isActive: () => props.editor.isActive('bold')
  },
  {
    icon: 'italic',
    title: '斜体',
    action: () => props.editor.chain().focus().toggleItalic().run(),
    isActive: () => props.editor.isActive('italic')
  },
  {
    icon: 'strikethrough',
    title: '文本线',
    action: () => props.editor.chain().focus().toggleStrike().run(),
    isActive: () => props.editor.isActive('strike')
  },
  {
    icon: 'code-view',
    title: '代码',
    action: () => props.editor.chain().focus().toggleCode().run(),
    isActive: () => props.editor.isActive('code')
  },
  {
    icon: 'mark-pen-line',
    title: '高亮',
    action: () => props.editor.chain().focus().toggleHighlight().run(),
    isActive: () => props.editor.isActive('highlight')
  },
  {
    type: 'divider'
  },
  {
    icon: 'h-1',
    title: '标题1',
    action: () => props.editor.chain().focus().toggleHeading({ level: 1 }).run(),
    isActive: () => props.editor.isActive('heading', { level: 1 })
  },
  {
    icon: 'h-2',
    title: '标题2',
    action: () => props.editor.chain().focus().toggleHeading({ level: 2 }).run(),
    isActive: () => props.editor.isActive('heading', { level: 2 })
  },
  {
    icon: 'h-3',
    title: '标题3',
    action: () => props.editor.chain().focus().toggleHeading({ level: 3 }).run(),
    isActive: () => props.editor.isActive('heading', { level: 3 })
  },
  {
    icon: 'h-4',
    title: '标题4',
    action: () => props.editor.chain().focus().toggleHeading({ level: 4 }).run(),
    isActive: () => props.editor.isActive('heading', { level: 4 })
  },
  {
    icon: 'h-5',
    title: '标题5',
    action: () => props.editor.chain().focus().toggleHeading({ level: 5 }).run(),
    isActive: () => props.editor.isActive('heading', { level: 5 })
  },
  {
    icon: 'h-6',
    title: '标题6',
    action: () => props.editor.chain().focus().toggleHeading({ level: 6 }).run(),
    isActive: () => props.editor.isActive('heading', { level: 6 })
  },
  {
    icon: 'paragraph',
    title: '段落',
    action: () => props.editor.chain().focus().setParagraph().run(),
    isActive: () => props.editor.isActive('paragraph')
  },
  {
    icon: 'list-unordered',
    title: '无须列表',
    action: () => props.editor.chain().focus().toggleBulletList().run(),
    isActive: () => props.editor.isActive('bulletList')
  },
  {
    icon: 'list-ordered',
    title: '有须列表',
    action: () => props.editor.chain().focus().toggleOrderedList().run(),
    isActive: () => props.editor.isActive('orderedList')
  },
  {
    type: 'divider'
  },
  {
    icon: 'double-quotes-l',
    title: '块',
    action: () => props.editor.chain().focus().toggleBlockquote().run(),
    isActive: () => props.editor.isActive('blockquote')
  },
  {
    icon: 'separator',
    title: '横线',
    action: () => props.editor.chain().focus().setHorizontalRule().run()
  },
  {
    type: 'divider'
  },
  {
    icon: 'format-clear',
    title: '清除样式',
    action: () => props.editor.chain()
        .focus()
        .clearNodes()
        .unsetAllMarks()
        .run()
  },
  {
    type: 'divider'
  },
  {
    icon: 'image-line',
    title: '插入图片',
    action: () => {
      const input = document.createElement('input');
      input.type = 'file';
      input.accept = 'image/*';

      input.onchange = async (e) => {
        const file = e.target.files[0];
        if (file) {
          try {
            await props.uploadImage(file);
          } catch (error) {
            console.error('图片上传失败:', error);
            // 可以在这里添加错误提示
          }
        }
      };

      input.click();
    }
  },
  {
    type: 'divider'
  },
  {
    icon: 'arrow-go-back-line',
    title: '撤销',
    action: () => props.editor.chain().focus().undo().run()
  },
  {
    icon: 'arrow-go-forward-line',
    title: '取消撤销',
    action: () => props.editor.chain().focus().redo().run()
  }
])

</script>
<style lang="scss">
.divider {
  width: 2px;
  height: 1.25rem;
  background-color: rgba(#000, .1);
  margin-left: .5rem;
  margin-right: .75rem;
}
</style>
