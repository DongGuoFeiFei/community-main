<template>
  <button
    class="menu-item"
    :class="{ 'is-active': isActive ? isActive() : null }"
    @click="action"
    :title="title"
  >
    <svg class="remix">
      <use :xlink:href="`${iconUrl}#ri-${icon}`" />
    </svg>
  </button>
</template>

<script setup>
import { ref } from "vue";
import remixiconUrl from "remixicon/fonts/remixicon.symbol.svg";

const props = defineProps({
  icon: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
  action: {
    type: Function,
    required: true,
  },
  isActive: {
    type: Function,
    default: null,
  },
});

const iconUrl = ref(remixiconUrl);
</script>

<style lang="scss">
.menu-item {
  width: 2rem;
  height: 2rem;
  color: #5e35b1;
  border: 2px solid transparent;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 10px;
  padding: 0.25rem;
  margin: 0.2rem;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    inset: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: 0;
  }

  svg {
    width: 100%;
    height: 100%;
    fill: currentColor;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;
    z-index: 1;
  }

  &:hover {
    transform: translateY(-2px) scale(1.05);
    border-color: rgba(102, 126, 234, 0.3);
    background: rgba(255, 255, 255, 0.8);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);

    svg {
      transform: scale(1.1);
      filter: drop-shadow(0 2px 4px rgba(94, 53, 177, 0.3));
    }
  }

  &.is-active {
    color: #fff;
    border-color: transparent;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4),
      inset 0 1px 0 rgba(255, 255, 255, 0.3);
    transform: translateY(-1px);

    &::before {
      opacity: 1;
    }

    svg {
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
    }

    &:hover {
      box-shadow: 0 6px 16px rgba(102, 126, 234, 0.5),
        inset 0 1px 0 rgba(255, 255, 255, 0.3);
    }
  }

  &:active {
    transform: translateY(0) scale(0.95);
  }

  // 添加点击涟漪效果
  &::after {
    content: "";
    position: absolute;
    inset: 0;
    background: radial-gradient(
      circle,
      rgba(255, 255, 255, 0.6) 0%,
      transparent 70%
    );
    opacity: 0;
    transform: scale(0);
    transition: all 0.6s ease;
  }

  &:active::after {
    opacity: 1;
    transform: scale(1);
    transition: 0s;
  }
}
</style>
