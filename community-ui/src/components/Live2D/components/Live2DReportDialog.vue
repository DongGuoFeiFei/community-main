<!-- components/Live2DReportDialog.vue -->
<template>
  <el-dialog
      v-model="visible"
      title="举报内容"
      width="400px"
      :close-on-click-modal="false"
  >
    <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        label-width="80px"
        label-position="top"
    >
      <el-form-item label="举报类型" prop="type">
        <el-select
            v-model="form.type"
            placeholder="请选择举报类型"
            style="width: 100%"
        >
          <el-option
              v-for="item in reportTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="举报原因" prop="reason">
        <el-input
            v-model="form.reason"
            type="textarea"
            :rows="3"
            placeholder="请详细描述举报原因..."
            maxlength="200"
            show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="close">取消</el-button>
      <el-button
          type="primary"
          @click="submit"
          :loading="isSubmitting"
      >
        提交举报
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { useRoute } from 'vue-router';
import { submitReport } from '@/api/report.js';

// 枚举常量（企业级推荐）
const ContentType = {
  ARTICLE: 1,
  AUTHOR: 2,
  COMMENT: 3,
  REPLY: 4
};

const emit = defineEmits(['submitted']);

const visible = ref(false);
const isSubmitting = ref(false);
const formRef = ref(null);

const route = useRoute();

// 判断是否显示举报按钮（匹配 /article/:id 或 /author/:id 格式的路由）
const isShowButton = computed(() => {
  const pathRegex = /^\/(article|author)\/\d+$/;
  return pathRegex.test(route.path);
});

// 举报类型选项
const reportTypes = [
  { value: 'spam', label: '垃圾广告' },
  { value: 'porn', label: '色情内容' },
  { value: 'violence', label: '暴力内容' },
  { value: 'hate', label: '仇恨言论' },
  { value: 'misinformation', label: '虚假信息' },
  { value: 'harassment', label: '骚扰行为' },
  { value: 'other', label: '其他问题' },
];

// 表单数据
const form = ref({
  contentId: '',
  contentType: '',
  type: '',
  reason: '',
});

// 表单验证规则
const rules = {
  type: [{ required: true, message: '请选择举报类型', trigger: 'change' }],
  reason: [
    { required: true, message: '请输入举报原因', trigger: 'blur' },
    { min: 10, message: '至少输入10个字符', trigger: 'blur' },
  ],
};

// 监听路由变化，自动填充内容ID和类型
watch(
    () => route.path,
    (newPath) => {
      const match = newPath.match(/^\/(article|author)\/(\d+)$/);
      if (match) {
        form.value.contentId = match[2];
        form.value.contentType = match[1] === 'article'
            ? ContentType.ARTICLE
            : ContentType.AUTHOR;
      }
    },
    { immediate: true }
);

// 打开弹窗
const open = () => {
  visible.value = true;
};

// 关闭弹窗
const close = () => {
  visible.value = false;
};

// 提交举报
const submit = async () => {
  try {
    await formRef.value.validate();
    isSubmitting.value = true;

    // 确保内容ID和类型已正确填充
    if (!form.value.contentId || !form.value.contentType) {
      throw new Error('无法获取举报内容信息，请刷新页面后重试');
    }

    await submitReport(form.value);
    ElMessage.success('举报已提交，我们会尽快处理');
    emit('submitted');
    close();
    formRef.value.resetFields();
  } catch (error) {
    if (error instanceof Error) {
      ElMessage.error(error.message || '提交举报失败');
    }
  } finally {
    isSubmitting.value = false;
  }
};

defineExpose({
  open,
  isShowButton,
});
</script>

<style scoped lang="scss">
.el-form-item {
  margin-bottom: 18px;
}
</style>
