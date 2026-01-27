<template>
  <Live2DViewer />

  <!-- 回到顶部按钮 -->
  <BackToTop :z-index="600" />

  <el-container>
    <el-header>
      <HomeHeader />
    </el-header>
    <el-main>
      <div class="editor-container">
        <!-- 编辑器头部 -->
        <EditorHeader
          v-model:title="articleData.title"
          :is-edit-mode="isEditMode"
          @back="goBack"
          @save="saveArticle"
        />

        <!-- 编辑器主体区域 -->
        <div class="editor-main-area">
          <div class="editor-content-wrapper">
            <tiptap-editor v-model:html="articleData.content" />
          </div>
        </div>

        <div class="editor-settings-area">
          <!-- 封面预览卡片 -->
          <div class="settings-section cover-preview-card">
            <div class="section-header">
              <span class="section-icon">🖼️</span>
              <h3 class="section-title">文章封面</h3>
            </div>
            <CoverSection
              :cover-url="coverImageData.accessUrl"
              @select="openFileDialog"
              @remove="removeCover"
            />
          </div>

          <!-- 分类选择卡片 -->
          <div class="settings-section">
            <div class="section-header">
              <span class="section-icon">📂</span>
              <h3 class="section-title">文章分类</h3>
            </div>
            <CategorySelector
              v-model:selected="articleData.categoryIds"
              :initial-selected="articleData.categoryIds"
            />
          </div>

          <!-- 标签选择卡片 -->
          <div class="settings-section">
            <div class="section-header">
              <span class="section-icon">🏷️</span>
              <h3 class="section-title">文章标签</h3>
            </div>
            <TagSelector v-model="tags" :max-tags="5" />
          </div>
        </div>

        <!-- 隐藏的文件输入 -->
        <input
          type="file"
          ref="fileInput"
          @change="handleCoverUpload"
          accept="image/*"
          style="display: none"
        />
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElLoading, ElMessage, ElMessageBox } from "element-plus";
import {
  addArticle,
  delFileById,
  getArticleById,
  updateArticle,
  uploadFile,
} from "@/api/index.js";
import { localStores } from "@/stores/localStores.js";
import { sessionStores } from "@/stores/sessionStores.js";
import CoverSection from "@/views/pages/edit/CoverSection.vue";
import EditorHeader from "@/views/pages/edit/EditorHeader.vue";
import TagSelector from "@/views/pages/edit/TagSelector.vue";
import { getPostTags } from "@/api/article.js";
import TiptapEditor from "@/views/pages/edit/components/TiptapEditor.vue";
import { getArticleCategories } from "@/api/category.js";
import CategorySelector from "@/views/pages/edit/CategorySelector.vue";
import HomeHeader from "@/components/Header.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
import BackToTop from "@/components/common/BackToTop.vue";

const lStore = localStores();
const baseUrl = lStore.baseURL;
const router = useRouter();
const fileInput = ref(null);
const sStore = sessionStores();
const isEditMode = ref(sStore.isEditMode);
const route = useRoute();

// 文章数据
const articleData = reactive({
  title: "",
  fileId: null,
  content:
    '<div style="font-family: SimSun, 宋体, serif; font-size: 18px; text-align: center; margin-bottom: 10px;">\n' +
    '  涉江<span style="color: #8b4513; font-weight: bold;">采芙蓉</span>，兰泽多<span style="color: #8b4513; font-weight: bold;">芳草</span>。\n' +
    "</div>\n" +
    '<div style="font-family: SimSun, 宋体, serif; font-size: 18px; text-align: center; margin-bottom: 10px;">\n' +
    '  采之欲<span style="color: #8b4513; font-weight: bold;">遗谁</span>？所思在<span style="color: #8b4513; font-weight: bold;">远道</span>。\n' +
    "</div>",
  status: 0, // 0: 发布, 1: 草稿
  tagIds: computed(() => tags.value.map((tag) => tag.id)),
  categoryIds: [],
});

const content = ref("");

const tags = ref([]);

// 封面数据
const coverImageData = reactive({
  fileId: "",
  fileOriginalName: "",
  fileAutoName: "",
  storageUrl: "",
  accessUrl: "",
  uploadTime: "",
});

// 打开文件选择对话框
const openFileDialog = () => {
  fileInput.value.click();
};

// 处理封面上传
const handleCoverUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  if (!file.type.startsWith("image/")) {
    ElMessage.error("请选择图片文件");
    return;
  }

  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error("图片大小不能超过5MB");
    return;
  }

  if (
    coverImageData.accessUrl?.trim() !== "" &&
    Number(coverImageData.fileId) !== 1
  ) {
    await delFileById(coverImageData.fileId);
    coverImageData.accessUrl = "";
  }

  try {
    const formData = new FormData();
    formData.append("file", file);

    const res = await uploadFile(formData);
    Object.assign(coverImageData, res.data);
    coverImageData.accessUrl = baseUrl + res.data.accessUrl;
    articleData.fileId = res.data.fileId;

    ElMessage.success("封面上传成功");
  } catch (error) {
    console.error("封面上传失败:", error);
    ElMessage.error("封面上传失败");
  } finally {
    event.target.value = "";
  }
};

// 移除封面
const removeCover = () => {
  ElMessageBox.confirm("确定要移除封面吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      if (Number(coverImageData.fileId) !== 1) {
        delFileById(coverImageData.fileId);
      }
      coverImageData.accessUrl = "";
      articleData.fileId = null;
      ElMessage.success("封面已移除");
    })
    .catch(() => {});
};

/**
 *  保存文章
 *  status：保存还是更新
 * @param status
 */
const saveArticle = (status) => {
  if (!articleData.title.trim()) {
    ElMessage.warning("请输入文章标题");
    return;
  }
  if (articleData.fileId === null) {
    ElMessage.warning("请添加封面");
    return;
  }
  if (!articleData.content.trim()) {
    ElMessage.warning("文章内容不能为空");
    return;
  }
  if (articleData.tagIds === []) {
    ElMessage.warning("请添加标签");
    return;
  }
  if (articleData.categoryIds === []) {
    ElMessage.warning("请添加分类");
    return;
  }
  articleData.status = status;

  const loading = ElLoading.service({
    lock: true,
    text: status === 0 ? "正在发布文章..." : "正在保存草稿...",
  });
  console.log(articleData);
  if (sStore.isEditMode) {
    updateArticle(sStore.editorArticleId, articleData)
      .then((res) => {
        ElMessage.success(status === 0 ? "文章重发布成功" : "草稿修改成功");
        sStore.isEditMode = false;
        router.back();
      })
      .catch((err) => {
        ElMessage.warning("操作失败，稍后重试。");
      })
      .finally(() => {
        loading.close();
      });
  } else {
    addArticle(articleData)
      .then((res) => {
        ElMessage.success(status === 0 ? "文章发布成功" : "草稿保存成功");
        sStore.isEditMode = false;
        router.back();
      })
      .catch((err) => {
        ElMessage.warning("操作失败，稍后重试。");
      })
      .finally(() => {
        loading.close();
      });
  }
};

// 设置是否是旧值
const isEditor = async () => {
  if (route.path === "/editor") {
    isEditMode.value = false;
    return;
  }

  if (route.path === "/editor-edit") {
    isEditMode.value = true;
  }

  if (isEditMode.value) {
    try {
      const [articleRes, tagsRes, categoriesRes] = await Promise.all([
        getArticleById(sStore.editorArticleId),
        getPostTags(sStore.editorArticleId),
        getArticleCategories(sStore.editorArticleId),
      ]);

      // 一次性赋值，减少响应式更新次数
      Object.assign(articleData, {
        title: articleRes.title,
        content: articleRes.content,
        fileId: articleRes.fileId,
        status: articleRes.status,
      });

      coverImageData.fileId = articleRes.fileId;
      coverImageData.accessUrl = sStore.baseURL + articleRes.coverUrl;
      tags.value = tagsRes.data;
      articleData.categoryIds = categoriesRes.data.map(
        (category) => category.categoryId,
      );
      console.log("articleData.categoryIds", articleData.categoryIds);
      console.log(articleData);
    } catch (error) {
      console.error("获取文章数据失败:", error);
      ElMessage.error("加载文章失败");
    }
  }
};

onMounted(() => {
  isEditor();
});

// 返回上一页
const goBack = async () => {
  try {
    await ElMessageBox.confirm("确定要离开当前页面吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    sStore.isEditMode = false;
    router.go(-1);
  } catch {
    console.log("用户取消了返回操作");
  }
};

// watch(() => articleData.categoryIds,
//     (newDate) => {
//       console.log(newDate)
//       console.log(articleData)
//     }
// )
</script>

<style scoped lang="scss">
// 动漫风格主容器
:deep(.el-main) {
  background: linear-gradient(180deg, #e3f2fd 0%, #f3e5f5 50%, #e8f5e9 100%);
  min-height: 100vh;
  position: relative;
  overflow: hidden;

  // 添加浮动云朵装饰
  &::before {
    content: "☁️";
    position: absolute;
    top: 10%;
    left: 5%;
    font-size: 60px;
    opacity: 0.3;
    animation: float 6s ease-in-out infinite;
    pointer-events: none;
  }

  &::after {
    content: "☁️";
    position: absolute;
    top: 20%;
    right: 10%;
    font-size: 80px;
    opacity: 0.2;
    animation: float 8s ease-in-out infinite reverse;
    pointer-events: none;
  }
}

.editor-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 20px;
  position: relative;

  // 添加浮动粒子效果
  &::before {
    content: "✨";
    position: absolute;
    top: 50px;
    right: -20px;
    font-size: 24px;
    opacity: 0.5;
    animation: sparkle 3s ease-in-out infinite;
    pointer-events: none;
  }

  &::after {
    content: "💫";
    position: absolute;
    bottom: 100px;
    left: -10px;
    font-size: 20px;
    opacity: 0.4;
    animation: sparkle 4s ease-in-out infinite reverse;
    pointer-events: none;
  }
}

// 顶部设置区域 - 三列布局
.editor-settings-area {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 24px;
  margin-bottom: 24px;
  animation: fadeIn 0.5s ease-out;
}

// 设置区域的每个卡片
.settings-section {
  animation: slideInUp 0.6s ease-out both;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(103, 58, 183, 0.12);
  border: 2px solid rgba(103, 58, 183, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;

  &:nth-child(1) {
    animation-delay: 0.1s;
  }

  &:nth-child(2) {
    animation-delay: 0.2s;
  }

  &:nth-child(3) {
    animation-delay: 0.3s;
  }

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 48px rgba(103, 58, 183, 0.18);
    border-color: rgba(103, 58, 183, 0.25);
  }

  // 内容区域滚动样式
  > :not(.section-header) {
    overflow-y: auto;
    overflow-x: hidden;
    padding: 16px;
    max-height: 420px;

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
  }
}

// 区块头部样式
.section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 20px;
  background: linear-gradient(
    135deg,
    rgba(227, 242, 253, 0.8) 0%,
    rgba(243, 229, 245, 0.8) 100%
  );
  border-bottom: 2px solid rgba(103, 58, 183, 0.15);
  flex-shrink: 0;

  .section-icon {
    font-size: 24px;
    animation: bounce 2s ease-in-out infinite;
    flex-shrink: 0;
  }

  .section-title {
    margin: 0;
    font-size: 17px;
    font-weight: 700;
    color: #5e35b1;
    letter-spacing: 0.5px;
    white-space: nowrap;
  }
}

// 封面预览卡片特殊样式
.cover-preview-card {
  :deep(.cover-section) {
    padding: 0;
    border: none;
    box-shadow: none;
    background: transparent;
  }
}

// 编辑器主体区域
.editor-main-area {
  width: 100%;
  animation: fadeIn 0.8s ease-out;

  .editor-content-wrapper {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 20px;
    padding: 32px;
    box-shadow: 0 8px 32px rgba(103, 58, 183, 0.12);
    border: 2px solid rgba(103, 58, 183, 0.15);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    min-height: 600px;

    &:hover {
      box-shadow: 0 12px 48px rgba(103, 58, 183, 0.18);
      border-color: rgba(103, 58, 183, 0.25);
    }
  }
}

// 浮动动画
@keyframes float {
  0%,
  100% {
    transform: translateY(0) translateX(0);
  }
  25% {
    transform: translateY(-20px) translateX(10px);
  }
  50% {
    transform: translateY(-10px) translateX(-10px);
  }
  75% {
    transform: translateY(-30px) translateX(5px);
  }
}

// 闪烁动画
@keyframes sparkle {
  0%,
  100% {
    opacity: 0.3;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2) rotate(180deg);
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

// 淡入动画
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

// 从左滑入
@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

// 从右滑入
@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

// 弹跳动画
@keyframes bounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .editor-settings-area {
    gap: 16px;
  }

  .settings-section > :not(.section-header) {
    max-height: 360px;
  }
}

@media (max-width: 992px) {
  .editor-settings-area {
    grid-template-columns: repeat(2, 1fr);
  }

  // 让第三个元素跨越两列居中
  .settings-section:nth-child(3) {
    grid-column: span 2;
    max-width: 600px;
    margin: 0 auto;
    width: 100%;
  }

  .settings-section > :not(.section-header) {
    max-height: 340px;
  }
}

@media (max-width: 768px) {
  .editor-container {
    padding: 20px 15px;
  }

  :deep(.el-main) {
    &::before,
    &::after {
      display: none; // 移动端隐藏装饰云朵
    }
  }

  .editor-settings-area {
    grid-template-columns: 1fr;
    gap: 16px;
    margin-bottom: 20px;
  }

  .settings-section:nth-child(3) {
    grid-column: span 1;
    max-width: 100%;
  }

  .section-header {
    padding: 12px 16px;

    .section-icon {
      font-size: 22px;
    }

    .section-title {
      font-size: 16px;
    }
  }

  .settings-section > :not(.section-header) {
    max-height: 300px;
    padding: 14px;
  }

  .editor-main-area .editor-content-wrapper {
    padding: 20px;
    min-height: 500px;
  }
}

@media (max-width: 576px) {
  .editor-container {
    padding: 15px 10px;

    &::before,
    &::after {
      display: none; // 移动端隐藏装饰粒子
    }
  }

  .editor-settings-area {
    gap: 12px;
    margin-top: 16px;
    margin-bottom: 16px;
  }

  .section-header {
    padding: 10px 14px;

    .section-icon {
      font-size: 20px;
    }

    .section-title {
      font-size: 15px;
    }
  }

  .settings-section > :not(.section-header) {
    max-height: 280px;
    padding: 12px;
  }

  .editor-main-area .editor-content-wrapper {
    padding: 16px;
    border-radius: 16px;
    min-height: 400px;
  }
}
</style>
