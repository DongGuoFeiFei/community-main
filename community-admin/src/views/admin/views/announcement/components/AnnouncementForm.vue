<script setup>
import { ref, watch } from 'vue';
import { useAnnouncementStore } from '@/stores/announcement';
import { ElMessage } from 'element-plus';

const props = defineProps({
  type: {
    type: String,
    default: 'add'
  },
  id: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['close']);

const store = useAnnouncementStore();
const visible = ref(true);
const loading = ref(false);
const formRef = ref(null);

// 初始化表单数据结构
const form = ref({
  title: '',
  content: '',
  publisher: '',
  publisherId: null,
  status: 1,
  priority: 0,
  startTime: null,
  endTime: null,
  remark: ''
});

// 表单验证规则
const rules = ref({
  title: [
    { required: true, message: '请输入公告标题', trigger: 'blur' },
    { max: 100, message: '标题长度不能超过100个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入公告内容', trigger: 'blur' }
  ],
  publisher: [
    { required: true, message: '请输入发布人', trigger: 'blur' },
    { max: 50, message: '发布人长度不能超过50个字符', trigger: 'blur' }
  ],
  priority: [
    { type: 'number', message: '优先级必须为数字', trigger: 'blur' }
  ]
});

// 监听visible变化
watch(visible, (newVal) => {
  if (!newVal) {
    emit('close');
  }
});

// 监听id变化，获取公告详情
watch(() => props.id, async (newVal) => {
  if (newVal && props.type === 'edit') {
    try {
      loading.value = true;
      await store.fetchAnnouncementDetail(newVal);

      // 转换后端数据为表单所需格式
      const detail = store.currentAnnouncement;
      form.value = {
        title: detail.title,
        content: detail.content,
        publisher: detail.publisher,
        publisherId: detail.publisherId,
        status: detail.status,
        priority: detail.priority,
        startTime: detail.startTime || null,
        endTime: detail.endTime || null,
        remark: detail.remark || ''
      };
    } catch (error) {
      console.error('获取公告详情失败:', error);
      ElMessage.error('获取公告详情失败');
      visible.value = false;
    } finally {
      loading.value = false;
    }
  }
}, { immediate: true });

// 提交表单
const handleSubmit = async () => {
  try {
    // 验证表单
    await formRef.value.validate();

    loading.value = true;
    const data = {
      ...form.value,
      startTime: form.value.startTime ? formatDateTime(form.value.startTime) : null,
      endTime: form.value.endTime ? formatDateTime(form.value.endTime) : null
    };

    if (props.type === 'add') {
      await store.addAnnouncement(data);
      ElMessage.success('新增公告成功');
    } else {
      data.id = props.id;
      await store.updateAnnouncement(data);
      ElMessage.success('更新公告成功');
    }

    visible.value = false;
    emit('close', true); // 传递成功标志
  } catch (error) {
    if (error?.errors) return; // 表单验证错误不显示额外提示
    console.error('操作失败:', error);
    ElMessage.error(error.message || '操作失败');
  } finally {
    loading.value = false;
  }
};

// 格式化日期时间
const formatDateTime = (date) => {
  if (!date) return null;
  if (typeof date === 'string') return date;
  return date.toISOString().replace('T', ' ').slice(0, 19);
};
</script>

<template>
  <el-dialog
      :title="type === 'add' ? '新增公告' : '编辑公告'"
      v-model="visible"
      width="800px"
      :close-on-click-modal="false"
      @close="emit('close')"
  >
    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        v-loading="loading"
    >
      <el-form-item label="公告标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入公告标题" />
      </el-form-item>

      <el-form-item label="公告内容" prop="content">
        <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
            placeholder="请输入公告内容"
        />
      </el-form-item>

      <el-form-item label="发布人" prop="publisher">
        <el-input v-model="form.publisher" placeholder="请输入发布人" />
      </el-form-item>

      <el-form-item label="优先级" prop="priority">
        <el-input-number v-model="form.priority" :min="0" />
      </el-form-item>

      <el-form-item label="状态">
        <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="上线"
            inactive-text="下线"
        />
      </el-form-item>

      <el-form-item label="生效时间">
        <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="开始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
        <span style="margin: 0 10px;">至</span>
        <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="备注">
        <el-input
            v-model="form.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
            maxlength="500"
            show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button :disabled="loading" @click="visible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        {{ type === 'add' ? '新增' : '更新' }}
      </el-button>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.el-date-editor {
  width: 100%;
}

.el-input-number {
  width: 100%;
}
</style>