<template>
  <el-dialog
    v-model="visible"
    title="调整用户积分"
    width="500px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="120px"
      label-position="left"
    >
      <el-form-item label="调整类型" prop="operationType">
        <el-radio-group v-model="formData.operationType">
          <el-radio :label="1">增加积分</el-radio>
          <el-radio :label="2">减少积分</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="积分数量" prop="points">
        <el-input-number
          v-model="formData.points"
          :min="1"
          :max="100000"
          controls-position="right"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="调整原因" prop="reason">
        <el-input
          v-model="formData.reason"
          type="textarea"
          :rows="3"
          placeholder="请输入调整原因"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { adjustPoints } from '@/api/points';
import { ElMessage } from 'element-plus';

const props = defineProps({
  accountId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close', 'success']);

const visible = ref(true);
const formRef = ref(null);

const formData = reactive({
  operationType: 1,
  points: 10,
  reason: '',
});

const rules = {
  operationType: [
    { required: true, message: '请选择调整类型', trigger: 'change' },
  ],
  points: [
    { required: true, message: '请输入积分数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '积分必须大于0', trigger: 'blur' },
  ],
  reason: [
    { required: true, message: '请输入调整原因', trigger: 'blur' },
    { max: 200, message: '原因不能超过200个字符', trigger: 'blur' },
  ],
};

// 关闭弹窗
const handleClose = () => {
  emit('close');
};

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    const data = {
      ...formData,
      points: Number(formData.points),
    };
    await adjustPoints(props.accountId, data);
    ElMessage.success('调整成功');
    emit('success');
    handleClose();
  } catch (error) {
    if (error.fields) return;
    console.error('调整积分失败:', error);
    ElMessage.error('调整失败');
  }
};
</script>
