<script setup>
import { Icon } from '@iconify/vue'

</script>

<template>
  <footer class="footer">
    <div class="footer-divider"></div>
    <div class="footer-content">
      <span class="copyright">
        <Icon icon="mdi:copyright" width="16" />
        {{ new Date().getFullYear() }} 社区网 · All rights reserved.
      </span>

      <div class="footer-links">
        <span class="tech">
          <Icon icon="mdi:vuejs" width="16" />
          技术支持：Vue 3 + Element Plus
        </span>
        <div

            class="github-link"
        >
          <Icon icon="gis:layer-stack-o" width="16" height="16" />
          version: 1.0
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: linear-gradient(90deg, #fdfbfb 0%, #ebedee 100%);
  padding: 20px 0;
  font-size: 14px;
  color: #6b7280;
  margin-top: 40px;
  box-shadow: 0 -2px 6px rgba(0, 0, 0, 0.05);
}

.footer-divider {
  height: 1px;
  background: linear-gradient(90deg, transparent, #e2e8f0, transparent);
  margin-bottom: 20px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.copyright {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #94a3b8;
}

.footer-links {
  display: flex;
  align-items: center;
  gap: 20px;
}

.tech {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #a0aec0;
}

.github-link {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #94a3b8;
  text-decoration: none;
  transition: color 0.3s;
}

.github-link:hover {
  //color: #6366f1;
}

@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
    text-align: center;
  }

  .footer-links {
    flex-direction: column;
    gap: 8px;
  }
}

</style>