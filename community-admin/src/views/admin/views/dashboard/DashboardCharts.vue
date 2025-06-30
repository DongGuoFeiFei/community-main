<template>
  <div class="dashboard-charts">
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane label="用户增长" name="user">
        <div ref="userChart" class="chart-container"></div>
      </el-tab-pane>
      <el-tab-pane label="帖子增长" name="post">
        <div ref="postChart" class="chart-container"></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import * as echarts from 'echarts';

const props = defineProps({
  userGrowth: {
    type: Array,
    default: () => []
  },
  postGrowth: {
    type: Array,
    default: () => []
  }
});

const activeTab = ref('user');
const userChart = ref(null);
const postChart = ref(null);
let userChartInstance = null;
let postChartInstance = null;

const initChart = (chartRef, data, title) => {
  if (!chartRef.value) return;

  const chart = echarts.init(chartRef.value);

  const option = {
    title: {
      text: title,
      left: 'center',
      textStyle: {
        fontSize: 14,
        color: '#666'
      }
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.date)
    },
    yAxis: {
      type: 'value'
    },
    series: [{
      data: data.map(item => item.value),
      type: 'line',
      smooth: true,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(58, 77, 233, 0.8)' },
          { offset: 1, color: 'rgba(58, 77, 233, 0.1)' }
        ])
      },
      itemStyle: {
        color: '#3a4de9'
      }
    }],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    }
  };

  chart.setOption(option);
  return chart;
};

onMounted(() => {
  nextTick(() => {
    if (props.userGrowth.length > 0) {
      userChartInstance = initChart(userChart, props.userGrowth, '用户增长趋势');
    }

    if (props.postGrowth.length > 0) {
      postChartInstance = initChart(postChart, props.postGrowth, '帖子增长趋势');
    }
  });
});

watch(() => props.userGrowth, (newVal) => {
  if (newVal.length > 0 && userChartInstance) {
    userChartInstance.setOption({
      xAxis: {
        data: newVal.map(item => item.date)
      },
      series: [{
        data: newVal.map(item => item.value)
      }]
    });
  }
});

watch(() => props.postGrowth, (newVal) => {
  if (newVal.length > 0 && postChartInstance) {
    postChartInstance.setOption({
      xAxis: {
        data: newVal.map(item => item.date)
      },
      series: [{
        data: newVal.map(item => item.value)
      }]
    });
  }
});
</script>

<style lang="scss" scoped>
.dashboard-charts {
  background-color: var(--el-bg-color);
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: var(--el-box-shadow-light);

  .chart-container {
    width: 100%;
    height: 300px;
  }
}
</style>