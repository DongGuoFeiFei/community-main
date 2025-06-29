<script setup>
import { Icon } from '@iconify/vue'
</script>

<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="left-section">
        <span class="copyright">
          <Icon icon="mdi:copyright" width="16"/>
          {{ new Date().getFullYear() }} 采芙蓉 · All rights reserved.
        </span>
      </div>
      <div class="right-section">
        <span class="tech">
          <Icon icon="mdi:vuejs" width="16"/>
          技术支持：Vue 3 + Element Plus
        </span>
        <span class="github-link">
          <Icon icon="gis:layer-stack-o" width="16" height="16"/>
          version: 1.0
        </span>
      </div>
    </div>
  </footer>
</template>

<style scoped lang="less">
.footer {
  background: linear-gradient(90deg, #f8fafc 0%, #f1f5f9 100%);
  height: 60px;
  display: flex;
  align-items: center;
  color: #64748b;
  width: 100%;
  box-shadow: 0 -1px 3px rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 10;
  font-size: 0.875rem;

  .footer-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1.5rem;
    height: 100%;

    .left-section, .right-section {
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
      gap: 0.375rem;
      white-space: nowrap;
    }

    .copyright {
      color: #64748b;
      .iconify {
        color: #94a3b8;
      }
    }

    .tech {
      .iconify {
        color: #41b883;
      }
    }

    .github-link {
      .iconify {
        color: #6366f1;
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