<script setup lang="ts">
import { Icon } from '@iconify/vue'
import { computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>

<template>
  <footer class="footer">
    <!-- 装饰性云朵 -->
    <div class="cloud-decorations">
      <span class="cloud cloud-1">☁️</span>
      <span class="cloud cloud-2">☁️</span>
      <span class="cloud cloud-3">☁️</span>
    </div>

    <!-- 波浪装饰 -->
    <div class="wave-decoration">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
          d="M0,60 C150,100 350,0 600,60 C850,120 1050,60 1200,60 L1200,120 L0,120 Z"
          class="wave-path"
        />
      </svg>
    </div>

    <div class="footer-content">
      <div class="left-section">
        <div class="footer-logo">
          <span class="logo-emoji">🌸</span>
          <span class="logo-text">采芙蓉</span>
        </div>
        <span class="copyright">
          <Icon icon="mdi:copyright" width="16" />
          {{ currentYear }} · All rights reserved
        </span>
      </div>

      <div class="center-section">
        <div class="footer-motto">
          <span class="motto-icon">💫</span>
          <span class="motto-text">在这里，遇见美好的二次元世界</span>
          <span class="motto-icon">✨</span>
        </div>
      </div>

      <div class="right-section">
        <span class="tech-badge">
          <Icon icon="mdi:vuejs" width="18" />
          <span>Vue 3</span>
        </span>
        <span class="tech-badge">
          <Icon icon="mdi:language-typescript" width="18" />
          <span>TypeScript</span>
        </span>
        <span class="version-badge">
          <Icon icon="gis:layer-stack-o" width="18" />
          <span>v1.0</span>
        </span>
      </div>
    </div>

    <!-- 底部装饰心形 -->
    <div class="heart-decorations">
      <span class="heart">💖</span>
      <span class="heart">💝</span>
      <span class="heart">💕</span>
    </div>
  </footer>
</template>

<style scoped lang="scss">
.footer {
  position: relative;
  background: linear-gradient(
    135deg,
    rgba(224, 247, 250, 0.95) 0%,
    /* 薄荷蓝 */ rgba(230, 240, 255, 0.95) 50%,
    /* 淡紫 */ rgba(232, 245, 255, 0.95) 100%
      /* 天空蓝 */
  );
  backdrop-filter: blur(20px) saturate(180%);
  padding: 30px 0;
  width: 100%;
  box-shadow: 0 -8px 32px rgba(135, 206, 235, 0.2),
    inset 0 2px 10px rgba(255, 255, 255, 0.6);
  border-top: 3px solid rgba(255, 255, 255, 0.5);
  overflow: hidden;
  z-index: 10;

  // 云朵装饰
  .cloud-decorations {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 100%;
    pointer-events: none;

    .cloud {
      position: absolute;
      font-size: 30px;
      opacity: 0.4;
      animation: cloudFloat 20s infinite ease-in-out;

      &.cloud-1 {
        top: 10%;
        left: 10%;
        animation-delay: 0s;
      }

      &.cloud-2 {
        top: 60%;
        left: 50%;
        animation-delay: 7s;
      }

      &.cloud-3 {
        top: 30%;
        right: 15%;
        animation-delay: 14s;
      }
    }
  }

  // 波浪装饰
  .wave-decoration {
    position: absolute;
    top: -30px;
    left: 0;
    width: 100%;
    height: 40px;
    overflow: hidden;
    line-height: 0;

    svg {
      position: relative;
      display: block;
      width: calc(100% + 1.3px);
      height: 40px;

      .wave-path {
        fill: rgba(255, 255, 255, 0.4);
        animation: waveMove 8s ease-in-out infinite;
      }
    }
  }

  // 心形装饰
  .heart-decorations {
    position: absolute;
    bottom: 5px;
    left: 0;
    right: 0;
    display: flex;
    justify-content: center;
    gap: 150px;
    pointer-events: none;

    .heart {
      font-size: 16px;
      opacity: 0.5;
      animation: heartFloat 3s infinite ease-in-out;

      &:nth-child(2) {
        animation-delay: 1s;
      }

      &:nth-child(3) {
        animation-delay: 2s;
      }
    }
  }

  .footer-content {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 40px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 30px;
    position: relative;
    z-index: 2;

    .left-section {
      display: flex;
      flex-direction: column;
      gap: 10px;
      align-items: flex-start;

      .footer-logo {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px 16px;
        background: rgba(255, 255, 255, 0.6);
        border-radius: 20px;
        box-shadow: 0 4px 15px rgba(135, 206, 235, 0.2),
          inset 0 2px 5px rgba(255, 255, 255, 0.8);
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

        &:hover {
          transform: translateY(-3px) scale(1.05);
          box-shadow: 0 8px 20px rgba(135, 206, 235, 0.3);
        }

        .logo-emoji {
          font-size: 24px;
          animation: spin 10s linear infinite;
        }

        .logo-text {
          font-size: 18px;
          font-weight: 700;
          background: linear-gradient(135deg, #5B9BD5, #8E44AD);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }
      }

      .copyright {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;
        color: #5B9BD5;
        font-weight: 500;
        padding: 4px 12px;
        background: rgba(135, 206, 235, 0.15);
        border-radius: 15px;

        .iconify {
          color: #87CEEB;
        }
      }
    }

    .center-section {
      flex: 1;
      display: flex;
      justify-content: center;

      .footer-motto {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px 24px;
        background: rgba(255, 255, 255, 0.7);
        backdrop-filter: blur(10px);
        border-radius: 25px;
        border: 2px solid rgba(135, 206, 235, 0.3);
        box-shadow: 0 6px 20px rgba(135, 206, 235, 0.2),
          inset 0 2px 8px rgba(255, 255, 255, 0.6);
        animation: mottoGlow 3s infinite ease-in-out;

        .motto-icon {
          font-size: 18px;
          animation: twinkle 2s infinite ease-in-out;

          &:last-child {
            animation-delay: 1s;
          }
        }

        .motto-text {
          font-size: 14px;
          font-weight: 600;
          background: linear-gradient(135deg, #5B9BD5, #8E44AD, #84C7D0);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          letter-spacing: 0.5px;
        }
      }
    }

    .right-section {
      display: flex;
      gap: 12px;
      align-items: center;

      .tech-badge,
      .version-badge {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 8px 16px;
        background: rgba(255, 255, 255, 0.6);
        border-radius: 20px;
        border: 2px solid rgba(255, 255, 255, 0.8);
        box-shadow: 0 4px 15px rgba(135, 206, 235, 0.15),
          inset 0 2px 5px rgba(255, 255, 255, 0.6);
        font-size: 13px;
        font-weight: 600;
        color: #5B9BD5;
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
        cursor: pointer;

        &:hover {
          transform: translateY(-3px) scale(1.05);
          box-shadow: 0 8px 20px rgba(135, 206, 235, 0.3);
          background: rgba(255, 255, 255, 0.9);
        }

        .iconify {
          color: #5B9BD5;
        }
      }
    }
  }

  // 响应式设计
  @media (max-width: 1024px) {
    .footer-content {
      flex-direction: column;
      gap: 20px;
      padding: 0 30px;

      .left-section,
      .center-section,
      .right-section {
        width: 100%;
        justify-content: center;
        align-items: center;
      }

      .left-section {
        align-items: center;
      }
    }
  }

  @media (max-width: 768px) {
    padding: 20px 0;

    .footer-content {
      padding: 0 20px;

      .center-section .footer-motto {
        padding: 10px 20px;

        .motto-text {
          font-size: 12px;
        }
      }

      .right-section {
        flex-wrap: wrap;
        justify-content: center;
      }
    }
  }

  @media (max-width: 480px) {
    .footer-content {
      padding: 0 15px;

      .left-section .footer-logo {
        padding: 6px 12px;

        .logo-emoji {
          font-size: 20px;
        }

        .logo-text {
          font-size: 16px;
        }
      }

      .center-section .footer-motto {
        .motto-icon {
          font-size: 14px;
        }

        .motto-text {
          font-size: 11px;
        }
      }

      .right-section {
        .tech-badge,
        .version-badge {
          padding: 6px 12px;
          font-size: 11px;

          .iconify {
            width: 14px;
            height: 14px;
          }
        }
      }
    }
  }
}

// 动画关键帧
@keyframes cloudFloat {
  0%,
  100% {
    transform: translate(0, 0);
  }
  25% {
    transform: translate(30px, -10px);
  }
  50% {
    transform: translate(-20px, 10px);
  }
  75% {
    transform: translate(20px, 5px);
  }
}

@keyframes heartFloat {
  0%,
  100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-10px) scale(1.2);
  }
}

@keyframes waveMove {
  0% {
    transform: translateX(0) scaleY(1);
  }
  50% {
    transform: translateX(-25%) scaleY(1.1);
  }
  100% {
    transform: translateX(-50%) scaleY(1);
  }
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes twinkle {
  0%,
  100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.3);
  }
}

@keyframes mottoGlow {
  0%,
  100% {
    box-shadow: 0 6px 20px rgba(135, 206, 235, 0.2),
      inset 0 2px 8px rgba(255, 255, 255, 0.6);
  }
  50% {
    box-shadow: 0 8px 25px rgba(135, 206, 235, 0.35),
      inset 0 2px 8px rgba(255, 255, 255, 0.8);
  }
}
</style>