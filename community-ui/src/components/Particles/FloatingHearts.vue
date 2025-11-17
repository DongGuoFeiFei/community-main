<template>
  <div class="floating-hearts-container" ref="containerRef">
    <div
      v-for="(heart, index) in hearts"
      :key="index"
      class="floating-heart"
      :style="{
        left: heart.x + 'px',
        top: heart.y + 'px',
        animationDelay: heart.delay + 's',
        animationDuration: heart.duration + 's',
        fontSize: heart.size + 'px',
        opacity: heart.opacity,
      }"
    >
      ❤️
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const containerRef = ref(null);
const hearts = ref([]);
let animationFrameId = null;

// 创建心形粒子
const createHeart = () => {
  return {
    x: Math.random() * (containerRef.value?.clientWidth || window.innerWidth),
    y: (containerRef.value?.clientHeight || window.innerHeight) + 20,
    delay: Math.random() * 2,
    duration: 10 + Math.random() * 5,
    size: 16 + Math.random() * 12,
    opacity: 0.3 + Math.random() * 0.4,
  };
};

// 初始化心形粒子
const initHearts = () => {
  const count = 8;
  hearts.value = Array.from({ length: count }, () => createHeart());
};

// 动画循环
const animate = () => {
  if (!containerRef.value) return;

  hearts.value.forEach((heart, index) => {
    // 更新位置
    heart.y -= 0.3 + Math.random() * 0.2;
    heart.x += Math.sin(heart.y * 0.01) * 0.3;

    // 如果心形飘出屏幕，重新生成
    if (heart.y < -50) {
      hearts.value[index] = createHeart();
    }
  });

  animationFrameId = requestAnimationFrame(animate);
};

onMounted(() => {
  initHearts();
  animate();
});

onUnmounted(() => {
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId);
  }
});
</script>

<style scoped lang="scss">
.floating-hearts-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 1;
  overflow: hidden;
}

.floating-heart {
  position: absolute;
  animation: floatUp linear infinite;
  will-change: transform, opacity;
}

@keyframes floatUp {
  0% {
    transform: translateY(0) translateX(0) rotate(0deg) scale(0.5);
    opacity: 0;
  }
  5% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    transform: translateX(20px) rotate(180deg) scale(1.1);
  }
  95% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) translateX(-20px) rotate(360deg) scale(0.8);
    opacity: 0;
  }
}
</style>
