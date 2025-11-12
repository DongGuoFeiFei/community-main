<template>
  <div class="auth-brand">
    <div class="brand-badge">
      <svg viewBox="0 0 64 64" class="brand-icon" aria-hidden="true">
        <defs>
          <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
            <stop offset="0%" stop-color="#8bc6ff"></stop>
            <stop offset="100%" stop-color="#bca6ff"></stop>
          </linearGradient>
        </defs>
        <circle cx="32" cy="32" r="28" fill="url(#g)"></circle>
        <path d="M20 38c6 2 18 2 24 0" stroke="#fff" stroke-width="3" fill="none" stroke-linecap="round"></path>
        <circle cx="26" cy="28" r="3" fill="#fff"></circle>
        <circle cx="38" cy="28" r="3" fill="#fff"></circle>
      </svg>
    </div>
    <div class="brand-text">
      <h1 class="title">采芙蓉</h1>
      <p class="subtitle">清新二次元社区 · 快乐每一刻</p>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.auth-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  .brand-badge {
    width: 48px;
    height: 48px;
    border-radius: 14px;
    background: rgba(255, 255, 255, 0.75);
    backdrop-filter: blur(6px);
    box-shadow: 0 6px 20px rgba(123, 151, 255, 0.25);
    display: grid;
    place-items: center;
  }
  .brand-icon {
    width: 30px;
    height: 30px;
  }
  .brand-text {
    .title {
      margin: 0;
      font-weight: 700;
      letter-spacing: 2px;
      font-size: 20px;
      background: linear-gradient(90deg, #7cc0ff, #c3a7ff);
      -webkit-background-clip: text;
      background-clip: text;
      color: transparent;
    }
    .subtitle {
      margin: 2px 0 0;
      font-size: 12px;
      color: #8a94a6;
    }
  }
}
</style>
