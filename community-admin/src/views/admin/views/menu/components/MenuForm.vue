<template>
  <el-dialog
    :model-value="visible"
    :title="formData.menuId ? '编辑菜单' : '添加菜单'"
    width="600px"
    @update:modelValue="handleDialogVisibleChange"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="上级菜单" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="menuTree"
          :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
          node-key="menuId"
          check-strictly
          placeholder="选择上级菜单"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="菜单类型" prop="menuType">
        <el-radio-group v-model="formData.menuType">
          <el-radio-button v-for="item in menuTypeOptions" :key="item.value" :value="item.value">
            {{ item.label }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="formData.menuName" placeholder="请输入菜单名称"/>
      </el-form-item>

      <el-form-item label="显示顺序" prop="orderNum">
        <el-input-number v-model="formData.orderNum" :min="0" controls-position="right"/>
      </el-form-item>

      <el-form-item v-if="formData.menuType !== 'F'" label="路由地址" prop="path">
        <el-input v-model="formData.path" placeholder="请输入路由地址"/>
      </el-form-item>

      <el-form-item v-if="formData.menuType === 'C'" label="组件路径" prop="component">
        <el-input v-model="formData.component" placeholder="请输入组件路径"/>
      </el-form-item>

      <el-form-item v-if="formData.menuType !== 'M'" label="权限标识" required prop="perms">
        <el-input v-model="formData.perms" placeholder="请输入权限标识"/>
      </el-form-item>

      <el-form-item label="菜单图标"  prop="icon" required>
        <el-popover
          placement="bottom-start"
          width="450"
          trigger="click"
          @show="showIconSelect = true"
        >
          <template #reference>
            <el-input
              v-model="formData.icon"
              placeholder="点击选择图标"
              readonly
              style="cursor: pointer"
            >
              <template #prefix>
                <el-icon v-if="formData.icon && formData.icon !== '#'">
                  <component :is="formData.icon"/>
                </el-icon>
              </template>
            </el-input>
          </template>
          <icon-select v-if="showIconSelect" v-model="formData.icon"/>
        </el-popover>
      </el-form-item>

      <el-form-item label="显示状态" prop="visible">
        <el-radio-group v-model="formData.visible">
          <el-radio :value="1">显示</el-radio>
          <el-radio :value="0">隐藏</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item v-if="formData.menuType === 'L'" label="外部链接" prop="isExternal">
        <el-radio-group v-model="formData.isExternal">
          <el-radio :value="1">是</el-radio>
          <el-radio :value="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item v-if="formData.menuType === 'L' && formData.isExternal" label="打开方式"
                    prop="target">
        <el-radio-group v-model="formData.target">
          <el-radio value="_self">当前窗口</el-radio>
          <el-radio value="_blank">新窗口</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注"/>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="submitForm">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {computed, ref, watch} from 'vue'
import IconSelect from "@/views/admin/components/IconSelect.vue";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  },
  formData: {
    type: Object,
    required: true
  },
  menuTree: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['submit', 'cancel', "update:visible"])

const menuTree = computed(() => [
  {
    menuId: 0,
    menuName: '根菜单',
    isRoot: true // 添加标记便于识别
  },
  ...(Array.isArray(props.menuTree) ? props.menuTree : [])
])


// 处理对话框可见性变化
const handleDialogVisibleChange = (value) => {
  emit('update:visible', value)
}

const formRef = ref(null)
const showIconSelect = ref(false)

const menuTypeOptions = [
  {value: 'M', label: '目录'},
  {value: 'C', label: '菜单'},
  {value: 'F', label: '按钮'},
  {value: 'L', label: '链接'}
]

const rules = {
  menuName: [{required: true, message: '请输入菜单名称', trigger: 'blur'}],
  parentId: [{required: true, message: '请选择上级菜单', trigger: 'change'}],
  menuType: [{required: true, message: '请选择菜单类型', trigger: 'change'}],
  path: [{required: true, message: '请输入路由地址', trigger: 'blur'}],
  component: [
    {
      required: true,
      validator: (rule, value, callback) => {
        if (props.formData.menuType === 'C' && !value) {
          callback(new Error('请输入组件路径'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}


watch(() => props.visible, (val) => {
  if (!val) {
    showIconSelect.value = false
  }
})

const handleClose = () => {
  emit('cancel')
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    emit('submit', props.formData)
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}
</script>
