<template>
  <div class="editor-container">
    <!-- 返回按钮和标题 -->
    <div class="editor-header">
      <button @click="goBack" class="back-button">← 返回</button>
      <input
          v-model="articleTitle"
          type="text"
          placeholder="输入文章标题"
          class="title-input"
      />
      <button @click="saveArticle" class="save-button">保存文章</button>
    </div>

    <!-- Vditor 编辑器 -->
    <div id="vditor" ref="vditorRef" v-show="editorLoaded"></div>

    <!-- 封面选择区域 -->
    <div class="cover-section">
      <h3>文章封面</h3>
      <div class="cover-actions">
        <button @click="openFileDialog" class="cover-button">
          {{ coverImageData.accessUrl ? '更换封面' : '选择封面' }}
        </button>
        <input
            type="file"
            ref="fileInput"
            @change="handleCoverUpload"
            accept="image/*"
            style="display: none;"
        >
        <button
            v-if="coverImageData.accessUrl"
            @click="removeCover"
            class="cover-button remove-button"
        >
          移除封面
        </button>
      </div>
      <div v-if="coverImageData.accessUrl" class="cover-preview">
        <img :src="coverImageData.accessUrl" alt="封面预览" class="cover-image">
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import Vditor from "vditor";
import "vditor/dist/index.css";
import {addArticle, delFileById, uploadFile} from "@/api/index.js";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
import {sessionStore} from "@/stores/sessionStores.js";


const userStore = sessionStore()
const baseUrl = userStore.baseURL;
const router = useRouter();
const vditorRef = ref(null);
const fileInput = ref(null);
const articleTitle = ref("");
const vditorInstance = ref(null);
const coverImageData = reactive({
  fileId: null,
  fileOriginalName: "",
  fileAutoName: "",
  storageUrl: null,
  accessUrl: "",
  uploadTime: ""
})

// 打开文件选择对话框
const openFileDialog = () => {
  fileInput.value.click();
};

// 处理封面上传
const handleCoverUpload = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 检查文件类型
  if (!file.type.startsWith("image/")) {
    ElMessage.error("请选择图片文件");
    return;
  }

  // 检查文件大小 (限制5MB)
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error("图片大小不能超过5MB");
    return;
  }
  // coverImageData.accessUrl不是”“，表示里面有着数据，还有之前的图片在，那么就删除再说
  if (coverImageData.accessUrl !== "") {
    const res = await delFileById(coverImageData.fileId)
    coverImageData.accessUrl = "";
  }


  try {
    const formData = new FormData();
    formData.append("file", file);

    // 将图片上传
    const res = await uploadFile(formData);
    // 保存返回的封面数据
    Object.assign(coverImageData, res.data)
    coverImageData.accessUrl = baseUrl + res.data.accessUrl;

    ElMessage.success("封面上传成功");
  } catch (error) {
    console.error("封面上传失败:", error);
    ElMessage.error("封面上传失败");
  } finally {
    // 重置input，允许重复选择同一文件
    event.target.value = "";
  }
};

// 移除封面
const removeCover = () => {
  ElMessageBox.confirm("确定要移除封面吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
        // 在数据库中将图片删掉，需要直接能展示出效果
        const res = delFileById(coverImageData.fileId)
        coverImageData.accessUrl = "";
        ElMessage.success("封面已移除");
      })
      .catch(() => {
        // 用户取消操作
      });
};

// 保存文章（补充封面数据）
const saveArticle = () => {
  if (!articleTitle.value.trim()) {
    ElMessage.warning("请输入文章标题")
    return;
  }

  const content = vditorInstance.value.getValue();
  if (!content.trim()) {
    ElMessage.warning("文章内容不能为空")
    return;
  }

  const articleData = {
    title: articleTitle.value,
    fileId: coverImageData.fileId,
    content: content
  };
  const res = addArticle(articleData)
  console.log(res)
  if (res) {
    console.log("保存的文章数据:", articleData);
    ElMessage.success("文章保存成功")
  } else {
    ElMessage.warning("文章保存失败，稍后重试。")
  }
};

// 返回上一页
const goBack = async () => {
  try {
    await ElMessageBox.confirm('确定要离开当前页面吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    router.go(-1)
  } catch {
    // 用户取消操作
    console.log('用户取消了返回操作')
  }
}

const loading = ElLoading.service({
  lock: true,
  text: '正在加载编辑器...',
  background: 'rgba(255, 255, 255, 0.8)',
});
const editorLoaded = ref(false);

// 生命周期钩子函数
onMounted(() => {
  vditorInstance.value = new Vditor("vditor", {
    mode: "wysiwyg",
    height: 450,
    toolbarConfig: {
      pin: true,
    },
    toolbar: [
      "emoji", "headings", "bold", "italic", "strike", "link", "|",
      "list", "ordered-list", "check", "outdent", "indent", "|",
      "quote", "line", "|", "upload", "|",
      "undo", "redo", "|", "outline", "fullscreen",
    ],
    upload: {
      url: "/files/upload",
      accept: "image/*",
      max: 10 * 1024 * 1024,
      fieldName: "file",
      headers: {
        'token': localStorage.getItem('token')
      },
      format: (files, responseText) => {
        const res = JSON.parse(responseText);
        if (res.code === 200) {

          const imageUrl = baseUrl + res.data.accessUrl;
          return JSON.stringify({
            msg: "上传成功",
            code: 0,
            data: {
              errFiles: [],
              succMap: {
                [files[0].name]: imageUrl
              }
            }
          });
        } else {
          throw new Error(res.message || "上传失败");
        }
      },
      success: (editor, msg) => {
        console.log("上传成功:", msg);
      },
      error: (msg) => {
        alert(`上传失败: ${msg}`);
      },
      handler: (files) => {
        return new Promise((resolve, reject) => {
          const formData = new FormData();
          formData.append('file', files[0]);

          uploadFile(formData)
              .then(res => {
                const imageUrl = baseUrl + res.data.accessUrl;
                resolve(imageUrl);
                const markdownImage = `![${files[0].name.replace(/\.[^/.]+$/, "")}](${imageUrl})`;
                vditorInstance.value.insertValue(markdownImage);
              })
              .catch(err => {
                reject(err);
              });
        });
      }
    },
    counter: {
      enable: true,
    },
    cache: {
      enable: true,
    },
    after: () => {
      vditorInstance.value.setValue("朋友，你的游戏，由我来分享🎉️！");
      editorLoaded.value = true;
      loading.close();
    },
  });
});

</script>
<style scoped>
.editor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.editor-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 15px;
}

.title-input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: all 0.3s;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.05);
}

.title-input:focus {
  border-color: #1890ff;
  outline: none;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1), 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.back-button,
.save-button {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.back-button {
  background-color: #f0f0f0;
  color: #333;
}

.back-button:hover {
  background-color: #e0e0e0;
}

.save-button {
  background-color: #1890ff;
  color: white;
}

.save-button:hover {
  background-color: #40a9ff;
  box-shadow: 0 2px 8px rgba(24, 144, 255, 0.3);
}

/* 封面区域样式 - 美化版 */
.cover-section {
  margin-top: 30px;
  padding: 25px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
}

.cover-section h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cover-section h3::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 16px;
  background-color: #1890ff;
  border-radius: 2px;
}

.cover-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.cover-button {
  padding: 8px 16px;
  background-color: #f5f5f5;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.cover-button:hover {
  background-color: #f0f0f0;
  border-color: #c0c0c0;
  transform: translateY(-1px);
}

.cover-button:active {
  transform: translateY(0);
}

.remove-button {
  background-color: #fff2f0;
  border-color: #ffccc7;
  color: #ff4d4f;
}

.remove-button:hover {
  background-color: #ffebe9;
  border-color: #ffa39e;
}

.cover-preview {
  margin-top: 15px;
  position: relative;
  transition: all 0.3s;
}

.cover-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: block;
  transition: all 0.3s;
}

.cover-image:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

/* 上传按钮美化 */
.cover-button::before {
  content: "🖼️";
  font-size: 16px;
}

.remove-button::before {
  content: "🗑️";
}

/* 响应式调整 */
@media (max-width: 768px) {
  .editor-header {
    flex-wrap: wrap;
  }

  .title-input {
    order: 1;
    width: 100%;
    margin-top: 10px;
  }

  .cover-actions {
    flex-wrap: wrap;
  }
}
</style>

