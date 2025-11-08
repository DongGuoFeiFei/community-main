<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

interface Particle {
  id: number
  emoji: string
  x: number
  y: number
  size: number
  duration: number
  delay: number
}

const particles = ref<Particle[]>([])
const emojis = ['🌸', '🌺', '💮', '🌼', '🌻', '✨', '💫', '⭐', '💖', '💕', '💗', '🎀', '🦋', '🐱']

const createParticle = (id: number): Particle => {
  return {
    id,
    emoji: emojis[Math.floor(Math.random() * emojis.length)],
    x: Math.random() * 100,
    y: Math.random() * 100,
    size: 20 + Math.random() * 20,
    duration: 15 + Math.random() * 15,
    delay: Math.random() * 5,
  }
}

onMounted(() => {
  // 创建初始粒子
  for (let i = 0; i < 15; i++) {
    particles.value.push(createParticle(i))
  }
})

onUnmounted(() => {
  particles.value = []
})
</script>

<template>
  <div class="floating-particles">
    <div
      v-for="particle in particles"
      :key="particle.id"
      class="particle"
      :style="{
        left: `${particle.x}%`,
        top: `${particle.y}%`,
        fontSize: `${particle.size}px`,
        animationDuration: `${particle.duration}s`,
        animationDelay: `${particle.delay}s`,
      }"
    >
      {{ particle.emoji }}
    </div>
  </div>
</template>

<style scoped lang="scss">
.floating-particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;

  .particle {
    position: absolute;
    opacity: 0;
    animation: particleFloat infinite ease-in-out;
    filter: drop-shadow(0 0 10px rgba(255, 182, 193, 0.5));
  }
}

@keyframes particleFloat {
  0% {
    opacity: 0;
    transform: translateY(0) rotate(0deg) scale(0);
  }
  10% {
    opacity: 0.6;
    transform: translateY(-20px) rotate(45deg) scale(1);
  }
  50% {
    opacity: 1;
    transform: translateY(-100px) rotate(180deg) scale(1.2);
  }
  90% {
    opacity: 0.6;
    transform: translateY(-180px) rotate(315deg) scale(1);
  }
  100% {
    opacity: 0;
    transform: translateY(-200px) rotate(360deg) scale(0);
  }
}
</style>


