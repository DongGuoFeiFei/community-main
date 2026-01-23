<template>
  <div class="user-basic-info">
    <div class="avatar-container">
      <el-avatar :size="120" :src="props.user.avatar"/>
    </div>

    <div class="info-container">
      <h2>{{ props.user.nickname }}</h2>
      <p class="bio">{{ props.user.bio || '暂无简介' }}</p>

      <div class="meta-info">
        <span><el-icon><Location/></el-icon> {{ props.user.location || '未知地区' }}</span>
        <span><el-icon><Calendar/></el-icon> 加入于 {{ formatJoinDate(props.user.createTime) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import {Calendar, Location} from '@element-plus/icons-vue'
import {formatDate} from '@/utils/date.js'

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})
const formatJoinDate = (dateString) => {
  return formatDate(dateString, 'YYYY年MM月')
}
</script>

<style lang="scss" scoped>
.user-basic-info {
  display: flex;
  align-items: center;
  gap: 30px;
  padding: 30px;
  background: linear-gradient(135deg, rgba(224, 242, 254, 0.8) 0%, rgba(243, 232, 255, 0.6) 100%);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(139, 92, 246, 0.15);
  border: 2px solid rgba(147, 197, 253, 0.3);
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 12px 40px rgba(139, 92, 246, 0.25);
  }

  // 装饰性背景元素
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200px;
    height: 200px;
    background: radial-gradient(circle, rgba(167, 139, 250, 0.2), transparent 70%);
    border-radius: 50%;
    animation: pulse-decoration 4s ease-in-out infinite;
  }

  &::after {
    content: '✦';
    position: absolute;
    bottom: 20px;
    right: 30px;
    font-size: 60px;
    color: rgba(192, 132, 252, 0.1);
    animation: rotate-decoration 8s linear infinite;
  }

  .avatar-container {
    flex-shrink: 0;
    position: relative;
    z-index: 1;

    // 头像光晕效果
    &::before {
      content: '';
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 140px;
      height: 140px;
      background: radial-gradient(circle, rgba(147, 197, 253, 0.4), transparent 70%);
      border-radius: 50%;
      animation: pulse-glow 2s ease-in-out infinite;
    }

    .el-avatar {
      position: relative;
      z-index: 1;
      border: 4px solid #fff;
      box-shadow: 0 8px 24px rgba(59, 130, 246, 0.3);
      transition: all 0.3s ease;
      cursor: pointer;

      &:hover {
        transform: scale(1.08) rotate(5deg);
        box-shadow: 0 12px 32px rgba(59, 130, 246, 0.5);
      }
    }
  }

  .info-container {
    flex-grow: 1;
    position: relative;
    z-index: 1;

    h2 {
      margin: 0 0 12px 0;
      font-size: 28px;
      font-weight: 700;
      background: linear-gradient(135deg, #6366f1, #a855f7);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      animation: text-shimmer 3s ease-in-out infinite;
    }

    .bio {
      margin: 0 0 18px 0;
      color: #64748b;
      line-height: 1.6;
      font-size: 15px;
      padding: 12px 16px;
      background: rgba(255, 255, 255, 0.6);
      border-radius: 12px;
      border-left: 3px solid #a78bfa;
      backdrop-filter: blur(10px);
    }

    .meta-info {
      display: flex;
      gap: 24px;
      color: #64748b;
      font-size: 14px;
      flex-wrap: wrap;

      span {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 8px 14px;
        background: rgba(255, 255, 255, 0.7);
        border-radius: 20px;
        transition: all 0.3s ease;

        &:hover {
          background: rgba(255, 255, 255, 0.9);
          transform: translateY(-2px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .el-icon {
          color: #a78bfa;
          font-size: 16px;
        }
      }
    }
  }
}

// 动画定义
@keyframes pulse-decoration {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.5;
  }
}

@keyframes rotate-decoration {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes pulse-glow {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.4;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.1);
    opacity: 0.7;
  }
}

@keyframes text-shimmer {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .user-basic-info {
    flex-direction: column;
    text-align: center;
    padding: 20px;

    .info-container {
      .meta-info {
        justify-content: center;
      }
    }
  }
}
</style>
