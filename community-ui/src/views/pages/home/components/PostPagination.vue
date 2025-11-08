<script setup>
defineProps({
  currentPage: {
    type: Number,
    required: true,
  },
  pageSize: {
    type: Number,
    required: true,
  },
  total: {
    type: Number,
    required: true,
  },
  loading: {
    type: Boolean,
    required: true,
  },
});

const emit = defineEmits(["update:currentPage", "page-change"]);

const handlePageChange = (page) => {
  emit("update:currentPage", page);
  emit("page-change");
};
</script>

<template>
  <div class="post-pagination">
    <el-pagination
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      layout="prev, pager, next"
      background
      @current-change="handlePageChange"
      :disabled="loading"
    />
  </div>
</template>

<style scoped lang="scss">
.post-pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  position: relative;
  animation: fadeInUp 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);

  // 波浪形分割线装饰
  &::before {
    content: "";
    position: absolute;
    top: -20px;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(
      90deg,
      transparent 0%,
      #ffb6c1 20%,
      #c9a0dc 50%,
      #84c7d0 80%,
      transparent 100%
    );
    mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1200 6'%3E%3Cpath d='M0,3 Q30,0 60,3 T120,3 T180,3 T240,3 T300,3 T360,3 T420,3 T480,3 T540,3 T600,3 T660,3 T720,3 T780,3 T840,3 T900,3 T960,3 T1020,3 T1080,3 T1140,3 T1200,3' fill='none' stroke='black' stroke-width='6'/%3E%3C/svg%3E");
    mask-size: 100% 100%;
    mask-repeat: no-repeat;
  }

  :deep(.el-pagination) {
    display: flex;
    gap: 8px;
    padding: 15px 25px;
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(15px);
    border-radius: 25px;
    border: 2px solid rgba(255, 182, 193, 0.3);
    box-shadow: 0 10px 30px rgba(255, 182, 193, 0.2),
      inset 0 2px 8px rgba(255, 255, 255, 0.6);

    // 按钮通用样式
    .btn-prev,
    .btn-next,
    .el-pager li {
      min-width: 40px;
      height: 40px;
      line-height: 40px;
      border-radius: 20px !important; // 大圆角
      border: 2px solid transparent;
      background: rgba(255, 255, 255, 0.8);
      color: #5b9bd5;
      font-weight: 600;
      font-size: 14px;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      box-shadow: 0 4px 10px rgba(135, 206, 235, 0.15),
        inset 0 1px 3px rgba(255, 255, 255, 0.8);
      margin: 0 4px;

      &:hover:not(.disabled):not(.is-active) {
        background: linear-gradient(135deg, #e0f7fa, #e8f5ff);
        transform: translateY(-4px) scale(1.1); // 弹跳效果
        box-shadow: 0 8px 20px rgba(135, 206, 235, 0.3),
          0 0 20px rgba(135, 206, 235, 0.4); // 外发光
        border-color: rgba(135, 206, 235, 0.5);
        color: #5b9bd5;
      }

      &.is-active {
        background: linear-gradient(135deg, #87ceeb, #9370db) !important;
        color: #f0f8ff !important;
        border-color: #87ceeb;
        transform: scale(1.15);
        box-shadow: 0 8px 20px rgba(91, 155, 213, 0.4),
          0 0 30px rgba(135, 206, 235, 0.5),
          inset 0 2px 8px rgba(255, 255, 255, 0.3);
        animation: activePageBounce 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);

        &::after {
          content: "✨";
          position: absolute;
          top: -25px;
          left: 50%;
          transform: translateX(-50%);
          font-size: 16px;
          animation: sparkle 1s infinite;
        }
      }

      &.disabled {
        opacity: 0.4;
        cursor: not-allowed;
        background: rgba(240, 240, 240, 0.5);
        color: #ccc;
      }
    }

    // 省略号样式
    .el-pager .more {
      color: #c9a0dc;
      font-weight: bold;

      &::before {
        content: "⋯";
        font-size: 20px;
      }
    }
  }

  // 添加小装饰
  &::after {
    content: "🌟";
    position: absolute;
    right: -40px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 22px;
    animation: rotate360 4s linear infinite;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes activePageBounce {
  0% {
    transform: scale(0.8);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1.15);
  }
}

@keyframes sparkle {
  0%,
  100% {
    opacity: 0.5;
    transform: translateX(-50%) translateY(0);
  }
  50% {
    opacity: 1;
    transform: translateX(-50%) translateY(-5px);
  }
}

@keyframes rotate360 {
  from {
    transform: translateY(-50%) rotate(0deg);
  }
  to {
    transform: translateY(-50%) rotate(360deg);
  }
}
</style>
