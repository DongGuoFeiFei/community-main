<template>
  <div class="tag-management-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>文章标签管理</span>
          <div>
            <el-button type="primary" @click="handleCreate">
              <el-icon>
                <plus/>
              </el-icon>
              新增标签
            </el-button>
            <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
              <el-icon>
                <delete/>
              </el-icon>
              批量删除
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标签名称">
          <el-input v-model="searchForm.name" placeholder="请输入标签名称" clearable/>
        </el-form-item>
        <el-form-item label="标签名称">
          <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
              filterable
          >
            <el-option
                v-for="(label, value) in statusOptions"
                :key="value"
                :label="label"
                :value="Number(value)"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 标签表格 -->
      <el-table
          v-loading="loading"
          :data="tagList"
          border
          stripe
          @selection-change="handleSelectionChange"
          style="width: 100%; height: auto;"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="name" label="标签名称" width="120">
          <template #default="{ row }">
            <el-tag>
              {{ row.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="slug" label="标签别名" width="120">
          <template #default="{ row }">
            <el-tag
                :color="row.color"
            >
              {{ row.slug }}
            </el-tag>
          </template>
        </el-table-column>
        <!--        <el-table-column prop="color" label="标签颜色" width="100"/>-->
        <el-table-column prop="createAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateAt" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.updateAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="creatorId" label="创建者ID" width="100"/>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            {{ getStatusText(row.status) }}
          </template>
        </el-table-column>
        <el-table-column prop="createCount" label="使用次数" width="100"/>
        <el-table-column label="操作" width="300" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            <!--            <el-button size="small" type="danger" @click="handleApproval(row)">审批</el-button>-->
            <el-select
                placeholder="审批状态"
                clearable
                filterable
                style="width: 100px"
            >
              <el-option
                  v-for="(label, value) in statusOptions"
                  :key="value"
                  :label="label"
                  :value="Number(value)"
                  @click="handleApproval(row,value)"
              />
            </el-select>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="500px"
        :close-on-click-modal="false"
    >
      <el-form ref="tagForm" :model="tagForm" :rules="rules" label-width="80px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="tagForm.name" placeholder="请输入标签名称"/>
        </el-form-item>
        <el-form-item label="标签别名" prop="slug">
          <el-input v-model="tagForm.slug" placeholder="请输入标签别名"/>
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-select
              v-model="tagForm.color"
              placeholder="请选择标签颜色"
              style="width: 100%"
          >
            <el-option
                v-for="item in colorOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';
import {Delete, Plus} from '@element-plus/icons-vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import dayjs from 'dayjs';
import {approvalTag, batchDeleteTags, createTag, deleteTag, getTagList, updateTag} from '@/api/tag';
import {colorOptions, STATUS_MAP} from "@/utils/staticData.js";
// 数据
const loading = ref(false);
const tagList = ref([]);
const selectedIds = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const isEditMode = ref(false);
const currentId = ref(null);

const searchForm = reactive({
  name: '',
  status: null
});

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

const tagForm = reactive({
  name: '',
  slug: '',
  color: ''
});

const rules = reactive({
  name: [
    {required: true, message: '请输入标签名称', trigger: 'blur'},
    {max: 20, message: '长度不能超过20个字符', trigger: 'blur'}
  ],
  slug: [
    {required: true, message: '请输入标签别名', trigger: 'blur'},
    {max: 20, message: '长度不能超过20个字符', trigger: 'blur'}
  ]
});

const statusOptions = STATUS_MAP;

// 方法
const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss');
};

const fetchTagList = async () => {
  try {
    loading.value = true;
    const params = {
      name: searchForm.name,
      status: searchForm.status,
      page: pagination.current,
      size: pagination.size
    };
    console.log(params)
    const res = await getTagList(params);
    tagList.value = res.data.rows;
    console.log(tagList.value)
    pagination.total = res.data.total;
  } catch (error) {
    console.error('获取标签列表失败:', error);
    ElMessage.error('获取标签列表失败');
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  pagination.current = 1;
  fetchTagList();
};

const resetSearch = () => {
  searchForm.name = '';
  handleSearch();
};

const handleSizeChange = (size) => {
  pagination.size = size;
  fetchTagList();
};

const handleCurrentChange = (current) => {
  pagination.current = current;
  fetchTagList();
};

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id);
};

const handleCreate = () => {
  dialogTitle.value = '新增标签';
  isEditMode.value = false;
  resetForm();
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  dialogTitle.value = '编辑标签';
  isEditMode.value = true;
  currentId.value = row.id;
  console.log(row)
  Object.assign(tagForm, row);
  console.log(tagForm)
  dialogVisible.value = true;
};

const resetForm = () => {
  Object.keys(tagForm).forEach(key => {
    tagForm[key] = '';
  });
};

const getStatusText = (data) => {
  const map = {0: '待审核', 1: '已审核', 2: '已拒绝'};
  return map[data] || '未知'
}

const submitForm = async () => {
  try {
    if (isEditMode.value) {
      await updateTag(currentId.value, tagForm);
      ElMessage.success('更新标签成功');
    } else {
      await createTag(tagForm);
      ElMessage.success('创建标签成功');
    }
    dialogVisible.value = false;
    fetchTagList();
  } catch (error) {
    console.error('操作失败:', error);
    ElMessage.error(error.message || '操作失败');
  }
};

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除标签 "${row.name}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTag(row.id);
      ElMessage.success('删除成功');
      fetchTagList();
    } catch (error) {
      console.error('删除失败:', error);
      ElMessage.error('删除失败');
    }
  }).catch(() => {
  });
};
const handleApproval = (row, status) => {
  ElMessageBox.confirm(`确定要统一审批标签 "${row.name}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const data = ref({
        id: row.id,
        status: status
      })
      await approvalTag(data.value);
      ElMessage.success('审批成功');
      fetchTagList();
    } catch (error) {
      console.error('审批失败:', error);
      ElMessage.error('审批失败');
    }
  }).catch(() => {
  });
};

const handleBatchDelete = () => {
  if (!selectedIds.value.length) return;
  ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 个标签吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await batchDeleteTags(selectedIds.value);
      ElMessage.success('批量删除成功');
      selectedIds.value = [];
      await fetchTagList();
    } catch (error) {
      console.error('批量删除失败:', error);
      ElMessage.error('批量删除失败');
    }
  }).catch(() => {
  });
};

// 生命周期
onMounted(() => {
  fetchTagList();
});
</script>

<style lang="scss" scoped>

.el-tag {
  cursor: pointer;
  user-select: none;
  color: #303133;
}

.tag-management-container {
  padding: 20px;
  background-color: #f0f2f5;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .search-form {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: center;
  }

  // 在组件样式或全局样式中添加
  .el-table {
    .el-table__body-wrapper {
      // 禁用滚动条
      &::-webkit-scrollbar {
        display: none;
      }

      // 禁用滚动行为
      overflow: hidden !important;
    }
  }
}
</style>