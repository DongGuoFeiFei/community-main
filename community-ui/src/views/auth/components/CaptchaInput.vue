<template>
  <div class="captcha-input">
    <el-input
      v-model="model.captchaCode"
      :placeholder="placeholder"
      style="width: 160px"
      @keydown.enter.native.prevent
    />
    <button class="captcha-image" type="button" @click="onRefresh" :aria-label="refreshAria">
      <img v-if="captchaImage" :src="captchaImage" alt="captcha" />
      <span v-else>{{ emptyText }}</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { computed, toRefs } from 'vue'

interface FormModel {
  captchaCode: string
  captchaKey?: string
}

const props = defineProps<{
  model: FormModel
  captchaImage: string
  placeholder?: string
  emptyText?: string
}>()

const emit = defineEmits<{
  (e: 'refresh'): void
}>()

const { model, captchaImage } = toRefs(props)

const onRefresh = () => emit('refresh')

const refreshAria = computed(() => (captchaImage?.value ? '点击刷新验证码' : '加载验证码'))
const placeholder = computed(() => props.placeholder ?? '请输入验证码')
const emptyText = computed(() => props.emptyText ?? '点击刷新验证码')
</script>

<style scoped lang="scss">
.captcha-input {
  display: flex;
  align-items: center;
  gap: 12px;

  .captcha-image {
    border: 0;
    padding: 0;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 12px;
    width: 120px;
    height: 40px;
    overflow: hidden;
    display: grid;
    place-items: center;
    cursor: pointer;
    border: 1px solid rgba(103, 58, 183, 0.2);
    box-shadow: 0 4px 12px rgba(103, 58, 183, 0.15);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 18px rgba(103, 58, 183, 0.25);
      border-color: rgba(103, 58, 183, 0.3);
    }

    &:active {
      transform: translateY(0);
    }

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    span {
      color: #9370db;
      font-size: 12px;
      font-weight: 500;
    }
  }
}
</style>
