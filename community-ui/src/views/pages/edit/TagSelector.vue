<template>
  <div class="tag-selector">
    <!-- 标签输入框 -->
    <div class="tag-input-container">
      <el-input
        v-model="searchQuery"
        placeholder="搜索或添加标签"
        clearable
        @keyup.enter="handleAddTag"
        @clear="filterTags"
      >
        <template #prefix>
          <el-icon>
            <Search />
          </el-icon>
        </template>
      </el-input>
    </div>

    <!-- 标签展示区域 -->
    <div class="tag-display-area">
      <!-- 已选标签 -->
      <div class="selected-tags" v-if="selectedTags.length > 0">
        <div class="section-title">已选标签</div>
        <div class="tag-list">
          <el-tag
            v-for="tag in selectedTags"
            :key="tag.id"
            :color="tag.color"
            closable
            @close="removeTag(tag)"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
      </div>

      <!-- 热门标签 -->
      <div class="popular-tags" v-if="popularTags.length > 0">
        <div class="section-title">热门标签</div>
        <div class="tag-list">
          <el-tag
            v-for="tag in popularTags"
            :key="tag.id"
            :color="tag.color"
            @click="toggleTag(tag)"
            :effect="isTagSelected(tag) ? 'dark' : 'plain'"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div class="search-results" v-if="searchResults.length > 0">
        <div class="section-title">搜索结果</div>
        <div class="tag-list">
          <el-tag
            v-for="tag in searchResults"
            :key="tag.id"
            :color="tag.color"
            @click="toggleTag(tag)"
            :effect="isTagSelected(tag) ? 'dark' : 'plain'"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
      </div>

      <!-- 创建新标签 -->
      <div class="create-new-tag" v-if="showCreateNewTag">
        <el-button
          type="primary"
          size="small"
          @click="createNewTag"
          :loading="isCreatingTag"
        >
          创建新标签: "{{ searchQuery }}"
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { Search } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { createTag, getAllTags, getPopularTags } from "@/api/tag.js";

// 组件属性
const props = defineProps({
  modelValue: {
    type: Array,
    default: () => [],
  },
  maxTags: {
    type: Number,
    default: 5,
  },
});

// 组件事件
const emit = defineEmits(["update:modelValue"]);

// 状态管理
const searchQuery = ref("");
const allTags = ref([]);
const popularTags = ref([]);
const isCreatingTag = ref(false);

// 计算属性
const selectedTags = computed({
  get: () => props.modelValue,
  set: (value) => emit("update:modelValue", value),
});

const searchResults = computed(() => {
  if (!searchQuery.value) return [];
  return allTags.value.filter(
    (tag) =>
      tag.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      tag.slug.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const showCreateNewTag = computed(() => {
  return (
    searchQuery.value &&
    !allTags.value.some(
      (tag) => tag.name.toLowerCase() === searchQuery.value.toLowerCase()
    ) &&
    !isTagSelected({ name: searchQuery.value })
  );
});

// 初始化加载标签
onMounted(async () => {
  await loadAllTags();
  await loadPopularTags();
});

// 加载所有标签
const loadAllTags = async () => {
  try {
    const response = await getAllTags();
    allTags.value = response.data;
  } catch (error) {
    ElMessage.error("加载标签失败");
    console.error("加载标签失败:", error);
  }
};

// 加载热门标签
const loadPopularTags = async () => {
  try {
    const response = await getPopularTags();
    popularTags.value = response.data;
  } catch (error) {
    ElMessage.error("加载热门标签失败");
    console.error("加载热门标签失败:", error);
  }
};

// 过滤标签
const filterTags = () => {
  // 搜索框清空时重置
  searchQuery.value = "";
};

// 添加标签
const handleAddTag = () => {
  if (!searchQuery.value) return;

  // 如果搜索到的标签存在，直接选中
  if (searchResults.value.length > 0) {
    toggleTag(searchResults.value[0]);
    return;
  }

  // 否则显示创建新标签按钮
};

// 创建新标签
const createNewTag = async () => {
  if (!searchQuery.value) return;
  if (selectedTags.value.length >= props.maxTags) {
    ElMessage.warning(`最多只能选择${props.maxTags}个标签`);
    return;
  }

  isCreatingTag.value = true;
  try {
    const response = await createTag({
      name: searchQuery.value,
      slug: generateSlug(searchQuery.value),
      color: getRandomColor(),
    });
    const newTag = response.data;

    allTags.value.push(newTag);
    toggleTag(newTag);
    searchQuery.value = "";
    ElMessage.success("创建标签提交审核");
  } catch (error) {
    ElMessage.error("创建标签失败");
    console.error("创建标签失败:", error);
  } finally {
    isCreatingTag.value = false;
  }
};

// 切换标签选择状态
const toggleTag = (tag) => {
  if (isTagSelected(tag)) {
    removeTag(tag);
  } else {
    addTag(tag);
  }
};

// 添加标签
const addTag = (tag) => {
  if (selectedTags.value.length >= props.maxTags) {
    ElMessage.warning(`最多只能选择${props.maxTags}个标签`);
    return;
  }

  if (!isTagSelected(tag)) {
    selectedTags.value = [...selectedTags.value, tag];
  }
};

// 移除标签
const removeTag = (tag) => {
  selectedTags.value = selectedTags.value.filter((t) => t.id !== tag.id);
};

// 检查标签是否已选
const isTagSelected = (tag) => {
  return selectedTags.value.some((t) => t.id === tag.id);
};

// 生成标签slug
const generateSlug = (name) => {
  return name
    .toLowerCase()
    .replace(/\s+/g, "-")
    .replace(/[^\w-]+/g, "");
};

// 获取随机颜色
const getRandomColor = () => {
  const colors = [
    "#409EFF",
    "#67C23A",
    "#E6A23C",
    "#F56C6C",
    "#909399",
    "#8E44AD",
    "#3498DB",
    "#E74C3C",
    "#1ABC9C",
  ];
  return colors[Math.floor(Math.random() * colors.length)];
};

// 监听搜索查询变化
watch(searchQuery, (newVal) => {
  if (!newVal) {
    filterTags();
  }
});
</script>

<style scoped lang="scss">
// 动漫风格标签选择器
.tag-selector {
  width: 100%;
  border: 2px solid rgba(103, 58, 183, 0.2);
  border-radius: 20px;
  padding: 20px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 6px 24px rgba(103, 58, 183, 0.12);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  animation: slideInUp 0.6s ease-out 0.3s both;

  // 装饰性标签图标
  &::before {
    content: "🏷️";
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

  .tag-input-container {
    margin-bottom: 16px;

    :deep(.el-input) {
      .el-input__wrapper {
        border-radius: 16px;
        padding: 8px 16px;
        background: linear-gradient(
          135deg,
          rgba(227, 242, 253, 0.5) 0%,
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

  .tag-display-area {
    max-height: 350px;
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
      background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
      border-radius: 3px;

      &:hover {
        background: linear-gradient(180deg, #764ba2 0%, #667eea 100%);
      }
    }

    .section-title {
      font-size: 15px;
      font-weight: 700;
      color: #5e35b1;
      margin-bottom: 12px;
      padding: 8px 12px;
      background: linear-gradient(
        135deg,
        rgba(227, 242, 253, 0.5) 0%,
        rgba(243, 229, 245, 0.5) 100%
      );
      border-radius: 12px;
      border-left: 4px solid #7e57c2;
      display: inline-block;
      letter-spacing: 0.5px;
    }

    .tag-list {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      margin-bottom: 20px;

      :deep(.el-tag) {
        cursor: pointer;
        user-select: none;
        border-radius: 12px;
        padding: 8px 16px;
        font-weight: 600;
        font-size: 14px;
        border: 2px solid transparent;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
        position: relative;
        overflow: hidden;
        color: #fcf3f3;

        // 波纹效果
        &::before {
          content: "";
          position: absolute;
          top: 50%;
          left: 50%;
          width: 0;
          height: 0;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.5);
          transform: translate(-50%, -50%);
          transition: width 0.6s, height 0.6s;
        }

        &:hover {
          transform: translateY(-3px) scale(1.05);
          box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
          border-color: rgba(255, 255, 255, 0.5);

          &::before {
            width: 200px;
            height: 200px;
          }
        }

        &:active {
          transform: translateY(-1px) scale(1.02);
        }

        // 已选中状态
        &.is-dark {
          font-weight: 700;
          animation: bounce 0.5s ease;
        }

        .el-tag__close {
          transition: all 0.3s;

          &:hover {
            background-color: rgba(255, 255, 255, 0.3);
            transform: rotate(90deg) scale(1.2);
          }
        }
      }
    }

    .create-new-tag {
      margin-top: 16px;
      text-align: center;

      :deep(.el-button) {
        border-radius: 14px;
        padding: 10px 24px;
        font-weight: 600;
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        border: none;
        box-shadow: 0 4px 12px rgba(79, 172, 254, 0.3);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

        &:hover {
          transform: translateY(-3px);
          box-shadow: 0 8px 20px rgba(79, 172, 254, 0.4);
        }

        &:active {
          transform: translateY(-1px);
        }
      }
    }
  }
}

// 弹跳动画
@keyframes bounce {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.15);
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
  .tag-selector {
    padding: 16px;

    .tag-display-area {
      max-height: 280px;

      .tag-list :deep(.el-tag) {
        font-size: 13px;
        padding: 6px 14px;
      }
    }
  }
}
</style>
