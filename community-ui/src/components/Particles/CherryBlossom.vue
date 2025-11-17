<template>
  <div class="cherry-blossom-container" ref="containerRef">
    <div
      v-for="(petal, index) in petals"
      :key="index"
      class="cherry-petal"
      :style="{
        left: petal.x + 'px',
        top: petal.y + 'px',
        animationDelay: petal.delay + 's',
        animationDuration: petal.duration + 's',
        fontSize: petal.size + 'px',
        opacity: petal.opacity,
        transform: `rotate(${petal.rotation}deg)`,
      }"
    >
      {{ petal.emoji }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const containerRef = ref(null);
const petals = ref([]);
let animationFrameId = null;

// 樱花相关的 emoji
const cherryEmojis = ["🌸", "🌺", "💮", "🌼"];

// 创建樱花花瓣
const createPetal = () => {
  return {
    x: Math.random() * (containerRef.value?.clientWidth || window.innerWidth),
    y: -30, // 从顶部开始飘落
    delay: Math.random() * 3,
    duration: 8 + Math.random() * 7, // 8-15秒飘落时间
    size: 18 + Math.random() * 14, // 18-32px
    opacity: 0.4 + Math.random() * 0.5, // 0.4-0.9
    rotation: Math.random() * 360, // 初始旋转角度
    rotationSpeed: (Math.random() - 0.5) * 2, // 旋转速度
    emoji: cherryEmojis[Math.floor(Math.random() * cherryEmojis.length)],
    horizontalSpeed: (Math.random() - 0.5) * 1.5, // 水平飘动速度
  };
};

// 初始化樱花花瓣
const initPetals = () => {
  const count = 12; // 同时存在的花瓣数量
  petals.value = Array.from({ length: count }, () => createPetal());
};

// 动画循环
const animate = () => {
  if (!containerRef.value) return;

  const containerHeight = containerRef.value.clientHeight || window.innerHeight;
  const containerWidth = containerRef.value.clientWidth || window.innerWidth;

  petals.value.forEach((petal, index) => {
    // 更新位置 - 向下飘落
    petal.y += 0.5 + Math.random() * 0.3;

    // 水平摆动效果（模拟微风）
    petal.x +=
      Math.sin((petal.y * 0.01 + petal.delay) * Math.PI) * 0.5 +
      petal.horizontalSpeed;

    // 旋转效果
    petal.rotation += petal.rotationSpeed;

    // 如果花瓣飘出屏幕底部，重新从顶部生成
    if (petal.y > containerHeight + 50) {
      petals.value[index] = createPetal();
    }

    // 如果花瓣飘出屏幕左右边界，调整位置
    if (petal.x < -50) {
      petal.x = containerWidth + 50;
    } else if (petal.x > containerWidth + 50) {
      petal.x = -50;
    }
  });

  animationFrameId = requestAnimationFrame(animate);
};

onMounted(() => {
  initPetals();
  animate();
});

onUnmounted(() => {
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId);
  }
});
</script>

<style scoped lang="scss">
.cherry-blossom-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1; // 与 FloatingHearts 保持一致
  overflow: hidden;
}

.cherry-petal {
  position: absolute;
  animation: cherryFloat linear infinite;
  will-change: transform, opacity;
  filter: drop-shadow(0 2px 4px rgba(255, 182, 193, 0.3));
  user-select: none;
}

@keyframes cherryFloat {
  0% {
    transform: translateY(0) translateX(0) rotate(0deg) scale(0.8);
    opacity: 0;
  }
  5% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    transform: translateX(30px) rotate(180deg) scale(1.1);
    opacity: 0.8;
  }
  95% {
    opacity: 0.6;
  }
  100% {
    transform: translateY(100vh) translateX(-30px) rotate(360deg) scale(0.7);
    opacity: 0;
  }
}
</style>
