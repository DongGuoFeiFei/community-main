<template>
  <el-dialog
      v-model="visible"
      title="回复评论"
      width="600px"
      :before-close="handleClose"
  >
    <el-form :model="form" label-width="80px">
      <el-form-item label="回复内容">
        <el-input
            v-model="form.content"
            type="textarea"
            :rows="5"
            placeholder="请输入回复内容"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, watch} from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  commentId: {
    type: [Number, String],
    default: null
  }
});

const emit = defineEmits(['update:modelValue', 'submit']);

const visible = ref(false);
const form = ref({
  content: ''
});

watch(() => props.modelValue, (val) => {
  visible.value = val;
});

watch(visible, (val) => {
  emit('update:modelValue', val);
  if (!val) {
    form.value.content = '';
  }
});

const handleClose = () => {
  visible.value = false;
};

const handleSubmit = () => {
  if (!form.value.content.trim()) {
    ElMessage.warning('请输入回复内容');
    return;
  }
  emit('submit', form.value.content);
  handleClose();
};
</script>