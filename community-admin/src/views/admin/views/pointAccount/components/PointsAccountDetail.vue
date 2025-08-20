<template>
  <el-dialog
    v-model="visible"
    title="积分账户详情"
    width="80%"
    top="5vh"
    @close="handleClose"
  >
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基本信息" name="basic">
        <AccountBasicInfo :account="accountDetail"/>
      </el-tab-pane>
      <el-tab-pane label="积分记录" name="logs">
        <PointsLogList :account-id="accountId"/>
      </el-tab-pane>
      <el-tab-pane label="消费记录" name="consumption">
        <PointsConsumptionList :account-id="accountId"/>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup>
import {ref, watch} from 'vue';
import {getPointsAccountDetail} from '@/api/points';
import AccountBasicInfo from './AccountBasicInfo.vue';
import PointsLogList from './PointsLogList.vue';
import PointsConsumptionList from './PointsConsumptionList.vue';

const props = defineProps({
  accountId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['close']);

const visible = ref(true);
const activeTab = ref('basic');
const accountDetail = ref({});

// 获取账户详情
const fetchAccountDetail = async () => {
  try {
    const res = await getPointsAccountDetail(props.accountId);
    accountDetail.value = res.data;
  } catch (error) {
    console.error('获取积分账户详情失败:', error);
  }
};

// 关闭弹窗
const handleClose = () => {
  emit('close');
};

watch(
  () => props.accountId,
  (newVal) => {
    if (newVal) {
      fetchAccountDetail();
    }
  },
  {immediate: true}
);
</script>
