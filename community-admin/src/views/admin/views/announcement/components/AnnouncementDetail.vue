<script setup>
import { ref, watch, computed } from 'vue';
import { useAnnouncementStore } from '@/stores/announcement';
import { ElMessage } from 'element-plus';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['close']);

const store = useAnnouncementStore();
const visible = ref(true);
const loading = ref(false);
const error = ref(null);

// 使用store中的currentAnnouncement而不是单独的ref
const announcement = computed(() => store.currentAnnouncement);

// 计算属性格式化日期时间
const formattedPublishTime = computed(() => {
  if (!announcement.value?.publishTime) return '';
  return new Date(announcement.value.publishTime).toLocaleString();
});

const formattedTimeRange = computed(() => {
  if (!announcement.value) return '';

  const start = announcement.value.startTime
      ? new Date(announcement.value.startTime).toLocaleDateString()
      : '无';

  const end = announcement.value.endTime
      ? new Date(announcement.value.endTime).toLocaleDateString()
      : '长期有效';

  return `${start} 至 ${end}`;
});

// 获取公告详情
const fetchDetail = async () => {
  try {
    loading.value = true;
    error.value = null;
    await store.fetchAnnouncementDetail(props.id);
  } catch (err) {
    console.error('获取公告详情失败:', err);
    error.value = '获取公告详情失败，请稍后重试';
    ElMessage.error(error.value);
    visible.value = false;
  } finally {
    loading.value = false;
  }
};

// 监听id变化
watch(() => props.id, fetchDetail, { immediate: true });

// 监听visible变化
watch(visible, (newVal) => {
  if (!newVal) {
    emit('close');
  }
});
</script>

<template>
  <el-dialog
      title="公告详情"
      v-model="visible"
      width="800px"
      :close-on-click-modal="false"
      @close="emit('close')"
  >
    <div v-loading="loading" class="announcement-detail">
      <template v-if="announcement">
        <div class="header">
          <h2 class="title">{{ announcement.title }}</h2>
          <div class="priority">
            <el-tag type="warning">优先级: {{ announcement.priority }}</el-tag>
          </div>
        </div>

        <div class="meta">
          <div class="meta-item">
            <span class="label">发布人:</span>
            <span class="value">{{ announcement.publisher }}</span>
          </div>
          <div class="meta-item">
            <span class="label">发布时间:</span>
            <span class="value">{{ formattedPublishTime }}</span>
          </div>
          <div class="meta-item">
            <span class="label">状态:</span>
            <el-tag :type="announcement.status === 1 ? 'success' : 'info'">
              {{ announcement.status === 1 ? '上线' : '下线' }}
            </el-tag>
          </div>
        </div>

        <div class="time-range">
          <span class="label">生效时间:</span>
          <span class="value">{{ formattedTimeRange }}</span>
        </div>

        <div class="content">
          <h3 class="content-title">公告内容</h3>
          <div class="content-body">
            {{ announcement.content }}
          </div>
        </div>

        <div v-if="announcement.remark" class="footer">
          <div class="remark">
            <h3 class="remark-title">备注</h3>
            <p class="remark-content">{{ announcement.remark }}</p>
          </div>
        </div>
      </template>

      <div v-if="error" class="error-message">
        <el-alert :title="error" type="error" show-icon />
      </div>
    </div>

    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.announcement-detail {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .title {
      font-size: 20px;
      color: #333;
      margin: 0;
    }

    .priority {
      margin-left: 20px;
    }
  }

  .meta {
    display: flex;
    flex-wrap: wrap;
    gap: 15px;
    margin-bottom: 20px;
    color: #666;
    font-size: 14px;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 5px;

      .label {
        color: #999;
      }

      .value {
        color: #666;
      }
    }
  }

  .time-range {
    display: flex;
    align-items: center;
    gap: 5px;
    margin-bottom: 20px;
    font-size: 14px;

    .label {
      color: #999;
    }

    .value {
      color: #666;
    }
  }

  .content {
    margin-bottom: 20px;

    .content-title {
      font-size: 16px;
      color: #333;
      margin-bottom: 10px;
    }

    .content-body {
      padding: 15px;
      background-color: #f5f7fa;
      border-radius: 4px;
      line-height: 1.6;
      white-space: pre-wrap;
    }
  }

  .footer {
    .remark {
      padding: 15px;
      background-color: #f5f7fa;
      border-radius: 4px;

      .remark-title {
        font-size: 16px;
        color: #333;
        margin-bottom: 10px;
      }

      .remark-content {
        color: #666;
        line-height: 1.6;
        margin: 0;
      }
    }
  }

  .error-message {
    margin-top: 20px;
  }
}
</style>