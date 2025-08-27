<template>
  <div class="alipay-payment-container">
    <!-- 支付信息展示 -->
    <div v-if="showPaymentInfo" class="payment-info">
      <h3>订单支付</h3>
      <div class="order-details">
        <p><strong>订单号:</strong> {{ paymentData.outTradeNo }}</p>
        <p><strong>商品名称:</strong> {{ paymentData.subject }}</p>
        <p><strong>商品描述:</strong> {{ paymentData.body }}</p>
        <p><strong>支付金额:</strong> ¥{{ paymentData.totalAmount }}</p>
      </div>

      <el-button
          type="primary"
          :loading="loading"
          @click="handlePayment"
          class="payment-btn"
      >
        {{ loading ? '正在生成支付...' : '前往支付宝支付' }}
      </el-button>
    </div>

    <!-- 支付加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-progress
          :percentage="progress"
          status="success"
          :indeterminate="true"
      />
      <p>正在跳转到支付宝支付页面...</p>
    </div>

    <!-- 错误提示 -->
    <el-alert
        v-if="errorMessage"
        :title="errorMessage"
        type="error"
        show-icon
        closable
        @close="errorMessage = ''"
    />
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, reactive, ref} from 'vue'
import {createAlipayPayment} from '@/api/payment.js'

const props = defineProps({
  // 订单信息
  orderInfo: {
    type: Object,
    required: true,
    validator: (value) => {
      return value.outTradeNo && value.totalAmount && value.subject
    }
  },
  // 是否自动触发支付
  autoPayment: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['payment-success', 'payment-failed', 'payment-cancel'])

// 响应式数据
const loading = ref(false)
const progress = ref(0)
const errorMessage = ref('')
const showPaymentInfo = ref(true)
const paymentData = reactive({
  outTradeNo: props.orderInfo.outTradeNo,
  totalAmount: props.orderInfo.totalAmount,
  subject: props.orderInfo.subject,
  body: props.orderInfo.body || '商品支付'
})

// 支付计时器
let progressTimer = null

// 创建支付
const handlePayment = async () => {
  try {
    loading.value = true
    errorMessage.value = ''
    showPaymentInfo.value = false

    // 启动进度条动画
    startProgressAnimation()

    // 调用支付API
    const response = await createAlipayPayment(paymentData)

    // 创建支付表单并提交
    const div = document.createElement('div')
    div.innerHTML = response
    document.body.appendChild(div)

    // 自动提交表单跳转到支付宝
    const form = div.querySelector('form')
    if (form) {
      form.submit()
    } else {
      throw new Error('支付表单生成失败')
    }

    // 清理DOM
    setTimeout(() => {
      document.body.removeChild(div)
    }, 1000)

  } catch (error) {
    handlePaymentError(error)
  } finally {
    stopProgressAnimation()
    loading.value = false
    showPaymentInfo.value = true
  }
}

// 进度条动画
const startProgressAnimation = () => {
  progressTimer = setInterval(() => {
    progress.value = (progress.value + 10) % 100
  }, 300)
}

const stopProgressAnimation = () => {
  if (progressTimer) {
    clearInterval(progressTimer)
    progressTimer = null
  }
  progress.value = 0
}

// 错误处理
const handlePaymentError = (error) => {
  console.error('支付宝支付错误:', error)
  errorMessage.value = error.message || '支付创建失败，请稍后重试'
  emit('payment-failed', error)
}

// 自动支付
onMounted(() => {
  if (props.autoPayment) {
    handlePayment()
  }
})

// 清理
onUnmounted(() => {
  stopProgressAnimation()
})
</script>

<style scoped lang="scss">
.alipay-payment-container {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .payment-info {
    text-align: center;

    h3 {
      margin-bottom: 20px;
      color: #333;
      font-size: 18px;
    }

    .order-details {
      margin-bottom: 30px;
      padding: 20px;
      background: #f9f9f9;
      border-radius: 6px;
      text-align: left;

      p {
        margin: 8px 0;
        color: #666;

        strong {
          color: #333;
          margin-right: 8px;
        }
      }
    }

    .payment-btn {
      width: 200px;
      height: 40px;
      font-size: 16px;
    }
  }

  .loading-container {
    text-align: center;
    padding: 40px 0;

    :deep(.el-progress) {
      margin-bottom: 20px;
    }

    p {
      color: #666;
      font-size: 14px;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .alipay-payment-container {
    margin: 10px;
    padding: 15px;

    .payment-info {
      .order-details {
        padding: 15px;
      }

      .payment-btn {
        width: 100%;
      }
    }
  }
}
</style>
