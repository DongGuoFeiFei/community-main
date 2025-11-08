<template>
  <div class="category-selector">
    <!-- 分类选择器头部 -->
    <div class="category-header">
      <el-input
        v-model="searchQuery"
        placeholder="搜索分类..."
        clearable
        @clear="handleSearchClear"
        @input="handleSearch"
      >
        <template #prefix>
          <el-icon>
            <Search />
          </el-icon>
        </template>
      </el-input>
    </div>

    <!-- 已选分类展示区 -->
    <div class="selected-categories" v-if="selectedCategories.length > 0">
      <div class="selected-title">
        已选分类 ({{ selectedCategories.length }}/3):
      </div>
      <div class="selected-tags">
        <el-tag
          v-for="category in selectedCategories"
          :key="category.id"
          closable
          @close="removeSelectedCategory(category.id)"
        >
          {{ category.categoryName }}
        </el-tag>
      </div>
    </div>

    <!-- 分类树 -->
    <div class="category-tree">
      <el-tree
        ref="treeRef"
        :data="filteredCategories"
        :props="treeProps"
        node-key="id"
        :accordion="true"
        :highlight-current="true"
        :expand-on-click-node="false"
        :filter-node-method="filterNode"
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <div class="tree-node">
            <span class="category-name">{{ node.label }}</span>
            <el-tag
              v-if="data.status === 0"
              size="small"
              type="danger"
              effect="plain"
            >
              禁用
            </el-tag>
            <el-tag
              v-if="data.description !== ''"
              size="small"
              type="info"
              effect="plain"
            >
              {{ data.description }}
            </el-tag>
          </div>
        </template>
      </el-tree>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { Search } from "@element-plus/icons-vue";
import { getCategories } from "@/api/category.js";
import { localStores } from "@/stores/localStores.js";
import { ElMessage } from "element-plus";

const lStore = localStores();
const baseUrl = lStore.baseURL;

// 树形结构配置
const treeProps = {
  label: "categoryName",
  children: "children",
};

// 分类数据
const categories = ref([]);
const searchQuery = ref("");
const treeRef = ref(null);
const selectedCategories = ref([]);

// 接收props
const props = defineProps({
  initialSelected: {
    type: Array,
    default: () => [],
  },
});

// 检查是否已选中
const isSelected = (id) => {
  return selectedCategories.value.some((c) => c.id === id);
};

// 过滤后的分类数据
const filteredCategories = computed(() => {
  if (!searchQuery.value) return categories.value;
  return filterTree(categories.value, searchQuery.value.toLowerCase());
});

// 获取分类数据
const fetchCategories = async () => {
  try {
    const res = await getCategories();
    categories.value = buildTree(res.data);
    // 初始化选中的分类
    initSelectedCategories();
  } catch (error) {
    console.error("获取分类失败:", error);
    ElMessage.error("获取分类失败");
  }
};

// 构建树形结构
const buildTree = (items, parentId = null) => {
  return items
    .filter((item) => item.parentId === parentId)
    .map((item) => ({
      ...item,
      children: buildTree(items, item.id),
    }));
};

// 初始化选中的分类
const initSelectedCategories = () => {
  if (props.initialSelected && props.initialSelected.length > 0) {
    // 扁平化树结构以便查找
    const flatCategories = flattenTree(categories.value);
    selectedCategories.value = props.initialSelected
      .map((id) => {
        const category = flatCategories.find((c) => c.id === id);
        return category
          ? { id: category.id, categoryName: category.categoryName }
          : null;
      })
      .filter(Boolean);
  }
};

// 扁平化树结构
const flattenTree = (tree) => {
  return tree.reduce((acc, node) => {
    acc.push(node);
    if (node.children && node.children.length > 0) {
      acc.push(...flattenTree(node.children));
    }
    return acc;
  }, []);
};

// 过滤树节点
const filterNode = (value, data) => {
  if (!value) return true;
  return data.categoryName.toLowerCase().includes(value.toLowerCase());
};

// 过滤整棵树
const filterTree = (tree, query) => {
  return tree
    .map((node) => ({ ...node }))
    .filter((node) => {
      const matches = filterNode(query, node);
      if (node.children) {
        node.children = filterTree(node.children, query);
        return matches || node.children.length > 0;
      }
      return matches;
    });
};

// 处理搜索
const handleSearch = () => {
  treeRef.value?.filter(searchQuery.value);
};

// 处理搜索清除
const handleSearchClear = () => {
  searchQuery.value = "";
  treeRef.value?.filter("");
};

// 移除已选分类
const removeSelectedCategory = (id) => {
  selectedCategories.value = selectedCategories.value.filter(
    (c) => c.id !== id
  );
  emit(
    "update:selected",
    selectedCategories.value.map((c) => c.id)
  );
};

// 处理节点点击
const emit = defineEmits(["select", "update:selected"]);
const handleNodeClick = (data) => {
  if (data.status !== 1) {
    ElMessage.warning("该分类已禁用，无法选择");
    return;
  }

  if (isSelected(data.id)) {
    removeSelectedCategory(data.id);
    return;
  }

  if (selectedCategories.value.length >= 3) {
    ElMessage.warning("最多只能选择3个分类");
    return;
  }

  selectedCategories.value.push({
    id: data.id,
    categoryName: data.categoryName,
  });

  emit("select", data.id);
  emit(
    "update:selected",
    selectedCategories.value.map((c) => c.id)
  );
};

// 监听初始选中的变化
watch(
  () => props.initialSelected,
  (newVal) => {
    if (categories.value.length > 0) {
      initSelectedCategories();
    }
  },
  { deep: true }
);

onMounted(() => {
  fetchCategories();
});
</script>

<style scoped lang="scss">
// 动漫风格分类选择器
.category-selector {
  border: 2px solid rgba(103, 58, 183, 0.2);
  border-radius: 20px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 6px 24px rgba(103, 58, 183, 0.12);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  animation: slideInUp 0.6s ease-out 0.2s both;

  // 装饰性分类图标
  &::before {
    content: "📂";
    position: absolute;
    top: 15px;
    right: 20px;
    font-size: 24px;
    opacity: 0.3;
  }

  &:hover {
    border-color: rgba(103, 58, 183, 0.3);
    box-shadow: 0 8px 32px rgba(103, 58, 183, 0.18);
    transform: translateY(-2px);
  }

  .category-header {
    margin-bottom: 16px;

    :deep(.el-input) {
      .el-input__wrapper {
        border-radius: 16px;
        padding: 8px 16px;
        background: linear-gradient(
          135deg,
          rgba(232, 245, 233, 0.5) 0%,
          rgba(243, 229, 245, 0.5) 100%
        );
        border: 2px solid rgba(103, 58, 183, 0.15);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        box-shadow: 0 2px 8px rgba(103, 58, 183, 0.08);

        &:hover {
          border-color: rgba(103, 58, 183, 0.25);
          box-shadow: 0 4px 12px rgba(103, 58, 183, 0.12);
        }

        &.is-focus {
          border-color: #7e57c2;
          box-shadow: 0 4px 16px rgba(126, 87, 194, 0.2);
          background: rgba(255, 255, 255, 0.95);
        }
      }

      .el-input__inner {
        color: #5e35b1;
        font-weight: 500;

        &::placeholder {
          color: #b39ddb;
        }
      }
    }
  }

  .selected-categories {
    margin-bottom: 20px;
    padding: 14px;
    background: linear-gradient(
      135deg,
      rgba(227, 242, 253, 0.4) 0%,
      rgba(243, 229, 245, 0.4) 100%
    );
    border-radius: 16px;
    border: 2px solid rgba(103, 58, 183, 0.15);
    box-shadow: 0 2px 12px rgba(103, 58, 183, 0.08);

    .selected-title {
      font-size: 15px;
      font-weight: 700;
      color: #5e35b1;
      margin-bottom: 12px;
      display: flex;
      align-items: center;
      gap: 8px;

      &::before {
        content: "✨";
        font-size: 18px;
      }
    }

    .selected-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;

      :deep(.el-tag) {
        border-radius: 12px;
        padding: 8px 16px;
        font-weight: 600;
        font-size: 14px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: #ffffff;
        border: none;
        box-shadow: 0 3px 10px rgba(102, 126, 234, 0.3);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        cursor: pointer;
        position: relative;
        overflow: hidden;

        &::before {
          content: "";
          position: absolute;
          top: 50%;
          left: 50%;
          width: 0;
          height: 0;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.3);
          transform: translate(-50%, -50%);
          transition: width 0.6s, height 0.6s;
        }

        &:hover {
          transform: translateY(-3px) scale(1.05);
          box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);

          &::before {
            width: 200px;
            height: 200px;
          }
        }

        .el-tag__close {
          color: #ffffff;
          transition: all 0.3s;

          &:hover {
            background-color: rgba(255, 255, 255, 0.3);
            transform: rotate(90deg) scale(1.2);
          }
        }
      }
    }
  }

  .category-tree {
    max-height: 450px;
    overflow-y: auto;
    padding-right: 8px;

    // 自定义滚动条样式
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(179, 157, 219, 0.1);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: linear-gradient(180deg, #a18cd1 0%, #fbc2eb 100%);
      border-radius: 3px;

      &:hover {
        background: linear-gradient(180deg, #fbc2eb 0%, #a18cd1 100%);
      }
    }

    .tree-node {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 4px 0;
      width: 100%;

      .category-name {
        flex: 1;
        font-weight: 500;
        color: #5e35b1;
        transition: all 0.3s;
      }

      :deep(.el-tag) {
        border-radius: 8px;
        font-size: 12px;
        font-weight: 600;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.08);
      }
    }

    :deep(.el-tree) {
      background: transparent;
      color: #5e35b1;

      .el-tree-node {
        margin-bottom: 4px;

        &__content {
          height: 40px;
          border-radius: 12px;
          padding: 0 12px;
          transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
          background: rgba(255, 255, 255, 0.5);

          &:hover {
            background: linear-gradient(
              135deg,
              rgba(227, 242, 253, 0.7) 0%,
              rgba(243, 229, 245, 0.7) 100%
            );
            transform: translateX(4px);
            box-shadow: 0 3px 12px rgba(103, 58, 183, 0.15);
          }
        }

        &.is-current > .el-tree-node__content {
          background: linear-gradient(
            135deg,
            rgba(103, 58, 183, 0.15) 0%,
            rgba(156, 39, 176, 0.15) 100%
          );
          font-weight: 700;
          box-shadow: 0 3px 12px rgba(103, 58, 183, 0.2);
        }

        &__expand-icon {
          color: #7e57c2;
          font-size: 14px;
          transition: all 0.3s;

          &.is-leaf {
            color: transparent;
          }
        }
      }
    }
  }
}

// 滑入动画
@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .category-selector {
    padding: 16px;

    .selected-categories {
      padding: 12px;

      .selected-tags :deep(.el-tag) {
        font-size: 13px;
        padding: 6px 14px;
      }
    }

    .category-tree {
      max-height: 350px;

      :deep(.el-tree-node__content) {
        height: 36px;
      }
    }
  }
}
</style>
