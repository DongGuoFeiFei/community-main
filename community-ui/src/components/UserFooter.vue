<script setup>
import { Icon } from "@iconify/vue";
</script>

<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="left-section">
        <span class="copyright">
          <Icon icon="mdi:copyright" width="16" />
          {{ new Date().getFullYear() }} 采芙蓉 · All rights reserved.
        </span>
      </div>
      <div class="right-section">
        <span class="tech">
          <Icon icon="mdi:vuejs" width="16" />
          技术支持：Vue 3 + Element Plus
        </span>
        <span class="github-link">
          <Icon icon="gis:layer-stack-o" width="16" height="16" />
          version: 1.0
        </span>
      </div>
    </div>
  </footer>
</template>

<style scoped lang="scss">
// 动漫风格配色
$sky-blue: #87ceeb;
$mint-blue: #b0e0e6;
$lavender: #e6e6fa;
$light-purple: #dda0dd;
$pink-accent: #ffb6c1;

.footer {
  // 气泡对话框式的半透明设计，与导航栏呼应
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.8) 0%,
    rgba(224, 247, 250, 0.6) 50%,
    rgba(255, 255, 255, 0.8) 100%
  );
  backdrop-filter: blur(15px) saturate(180%);
  height: 70px;
  display: flex;
  align-items: center;
  color: #64748b;
  width: 100%;
  box-shadow: 0 -4px 20px rgba(135, 206, 235, 0.2),
    0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  border-top: 2px solid rgba(135, 206, 235, 0.3);
  border-radius: 20px 20px 0 0;
  position: relative;
  z-index: 1;
  font-size: 0.875rem;
  transition: all 0.3s ease;

  // 底部装饰线
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba(135, 206, 235, 0.5) 20%,
      rgba(221, 160, 221, 0.5) 50%,
      rgba(152, 251, 152, 0.5) 80%,
      transparent 100%
    );
    border-radius: 20px 20px 0 0;
  }

  .footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1.5rem;
    height: 100%;

    .left-section,
    .right-section {
      display: flex;
      align-items: center;
      height: 100%;
    }

    .right-section {
      gap: 1.5rem;
    }

    > * {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      white-space: nowrap;
      transition: all 0.3s ease;
    }

    .copyright {
      color: #666;
      font-weight: 500;
      transition: all 0.3s ease;

      &:hover {
        color: #87ceeb;
        transform: translateY(-2px);
      }

      .iconify {
        color: #87ceeb;
        transition: all 0.3s ease;
      }

      &:hover .iconify {
        transform: rotate(360deg);
      }
    }

    .tech {
      color: #666;
      font-weight: 500;
      transition: all 0.3s ease;

      &:hover {
        color: #41b883;
        transform: translateY(-2px);
      }

      .iconify {
        color: #41b883;
        transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
      }

      &:hover .iconify {
        transform: scale(1.2) rotate(10deg);
      }
    }

    .github-link {
      color: #666;
      font-weight: 500;
      transition: all 0.3s ease;

      &:hover {
        color: #6366f1;
        transform: translateY(-2px);
      }

      .iconify {
        color: #6366f1;
        transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
      }

      &:hover .iconify {
        transform: scale(1.2) rotate(-10deg);
      }
    }
  }

  @media (max-width: 768px) {
    height: auto;
    padding: 0.75rem 0;

    .footer-content {
      flex-direction: column;
      gap: 0.75rem;
      padding: 0 1rem;

      .right-section {
        gap: 1rem;
      }
    }
  }

  @media (max-width: 480px) {
    font-size: 0.75rem;

    .footer-content {
      .right-section {
        flex-direction: column;
        gap: 0.5rem;
      }
    }
  }
}
</style>
