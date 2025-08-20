<template>
  <el-dialog
    :model-value="modelValue"
    :title="dialogType === 'add' ? '新增积分规则' : '编辑积分规则'"
    width="600px"
    @update:modelValue="$emit('update:modelValue', $event)"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="规则代码" prop="ruleCode">
        <el-input v-model="form.ruleCode" placeholder="请输入规则代码"/>
      </el-form-item>
      <el-form-item label="规则名称" prop="ruleName">
        <el-input v-model="form.ruleName" placeholder="请输入规则名称"/>
      </el-form-item>
      <el-form-item label="规则类型" prop="ruleType">
        <el-select v-model="form.ruleType" placeholder="请选择规则类型">
          <el-option
            v-for="item in ruleTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="积分值" prop="pointsValue">
        <el-input-number
          v-model="form.pointsValue"
          :min="1"
          :max="10000"
          controls-position="right"
        />
      </el-form-item>
      <el-form-item label="每日上限" prop="dailyLimit">
        <el-input-number
          v-model="form.dailyLimit"
          :min="0"
          :max="100000"
          controls-position="right"
        />
        <span class="tips">(0表示无限制)</span>
      </el-form-item>
      <el-form-item label="总上限" prop="totalLimit">
        <el-input-number
          v-model="form.totalLimit"
          :min="0"
          :max="1000000"
          controls-position="right"
        />
        <span class="tips">(0表示无限制)</span>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="规则描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入规则描述"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, watch} from 'vue'
import {ElMessage} from "element-plus";
import {addPointsRule, updatePointsRule} from "@/api/rules.js";

const props = defineProps({
  modelValue: Boolean,
  currentRule: Object,
  dialogType: String
})

const emit = defineEmits(['update:modelValue', 'refresh'])

const formRef = ref(null)
const form = ref({
  ruleCode: '',
  ruleName: '',
  ruleType: 1,
  pointsValue: 10,
  dailyLimit: 0,
  totalLimit: 0,
  status: 1,
  description: ''
})

const rules = ref({
  ruleCode: [
    {required: true, message: '请输入规则代码', trigger: 'blur'},
    {pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur'}
  ],
  ruleName: [
    {required: true, message: '请输入规则名称', trigger: 'blur'}
  ],
  ruleType: [
    {required: true, message: '请选择规则类型', trigger: 'change'}
  ],
  pointsValue: [
    {required: true, message: '请输入积分值', trigger: 'blur'}
  ],
  status: [
    {required: true, message: '请选择状态', trigger: 'change'}
  ]
})

const ruleTypeOptions = ref([
  {value: 1, label: '获取积分'},
  {value: 2, label: '消费积分'}
])

watch(() => props.currentRule, (val) => {
  if (val && Object.keys(val).length > 0) {
    form.value = {...val}
  }
}, {immediate: true})

const handleClose = () => {
  emit('update:modelValue', false)
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    if (props.dialogType === 'add') {
      await addPointsRule(form.value)
      ElMessage.success('新增成功')
    } else {
      await updatePointsRule(form.value)
      ElMessage.success('更新成功')
    }

    emit('refresh')
    handleClose()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
    }
  }
}
</script>

<style lang="scss" scoped>
.tips {
  margin-left: 10px;
  color: #999;
  font-size: 12px;
}
</style>
