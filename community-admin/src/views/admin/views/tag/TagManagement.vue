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
          style="width: 100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="name" label="标签名称"/>
        <el-table-column prop="slug" label="标签别名"/>
        <el-table-column prop="description" label="描述"/>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">审批</el-button>
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
        <el-form-item label="描述" prop="description">
          <el-input
              v-model="tagForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入标签描述"
          />
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
import {batchDeleteTags, createTag, deleteTag, getTagList, updateTag} from '@/api/tag';

// 数据
const loading = ref(false);
const tagList = ref([]);
const selectedIds = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const isEditMode = ref(false);
const currentId = ref(null);

const searchForm = reactive({
  name: ''
});

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

const tagForm = reactive({
  name: '',
  slug: '',
  description: ''
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

// 方法
const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss');
};

const fetchTagList = async () => {
  try {
    loading.value = true;
    const params = {
      ...searchForm,
      page: pagination.current,
      size: pagination.size
    };
    const res = await getTagList(params);
    tagList.value = res.data.list;
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
  Object.assign(tagForm, row);
  dialogVisible.value = true;
};

const resetForm = () => {
  Object.keys(tagForm).forEach(key => {
    tagForm[key] = '';
  });
};

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
}
</style>