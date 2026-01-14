<template>
  <el-dialog v-model="visible" title="活动详情" width="60%">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="活动名称">{{ activity.title }}</el-descriptions-item>
      <el-descriptions-item label="活动状态">
        <el-tag :type="activity.status === 1 ? 'success' : 'danger'">
          {{ activity.status === 1 ? '进行中' : '已结束' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="开始时间">{{ formatDate(activity.startTime) }}</el-descriptions-item>
      <el-descriptions-item label="结束时间">{{ formatDate(activity.endTime) }}</el-descriptions-item>
      <el-descriptions-item label="活动地点">{{ activity.location }}</el-descriptions-item>
      <el-descriptions-item label="参与人数">
        {{ activity.currentParticipants }}/{{ activity.maxParticipants }}
      </el-descriptions-item>
      <el-descriptions-item label="活动封面" :span="2">
        <el-image
            v-if="activity.coverImage"
            :src="activity.coverImage"
            style="max-width: 300px; max-height: 200px;"
        />
        <span v-else>无封面</span>
      </el-descriptions-item>
      <el-descriptions-item label="活动内容" :span="2">
        <div class="content" v-html="activity.content"></div>
      </el-descriptions-item>
    </el-descriptions>

    <template #footer>
      <el-button @click="visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import dayjs from 'dayjs';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  activity: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['update:modelValue']);

const visible = ref(false);

watch(() => props.modelValue, (val) => {
  visible.value = val;
});

watch(visible, (val) => {
  emit('update:modelValue', val);
});

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm');
};
</script>

<style lang="scss" scoped>
.content {
  padding: 10px;
  border: 1px solid #eee;
  border-radius: 4px;
  background-color: #f9f9f9;
}
</style>