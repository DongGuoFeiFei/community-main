<script setup>
import {onMounted, ref} from 'vue'
import {localStores} from '@/stores/localStores'

const store = localStores()
const ads = ref([])
const currentAd = ref(null)
const loading = ref(false)
const error = ref(null)

const mockAds = [
  {
    id: 1,
    type: 'image',
    title: '云服务器特惠',
    content: 'https://fakeimg.pl/350x200/?text=Server%20Promo',
    link: 'https://example.com/server-promo',
    position: 'article_sidebar'
  },
  {
    id: 2,
    type: 'text',
    title: '开发者工具包',
    content: '限时优惠！全套开发者工具包7折优惠，包含IDE、调试工具和云服务',
    link: 'https://example.com/dev-tools',
    position: 'article_sidebar'
  },
  {
    id: 3,
    type: 'image',
    title: '在线课程',
    content: 'https://fakeimg.pl/350x200/?text=Online%20Course',
    link: 'https://example.com/courses',
    position: 'article_sidebar'
  }
]
// 获取广告数据
const fetchAds = async () => {
  try {
    loading.value = true
    // const response = await getAdvertising({
    //   position: 'article_sidebar',
    //   userId: store.userInfo.userInfo.userId || null
    // })
    // ads.value = response.data
    ads.value = mockAds
    if (ads.value.length > 0) {
      selectRandomAd()
    }
  } catch (err) {
    error.value = err.message || '广告加载失败'
    console.error('广告加载错误:', err)
  } finally {
    loading.value = false
  }
}

// 随机选择广告
const selectRandomAd = () => {
  if (ads.value.length === 0) return
  const randomIndex = Math.floor(Math.random() * ads.value.length)
  currentAd.value = ads.value[randomIndex]
}

// 广告点击处理
const handleAdClick = (adId) => {
  // 记录点击事件
  console.log(`广告 ${adId} 被点击`)
  // 这里可以添加点击统计API调用
  window.open(currentAd.value.link, '_blank')
}

// 组件挂载时加载广告
onMounted(() => {
  fetchAds()
})
</script>

<template>
  <div class="advertising-container">
    <div v-if="loading" class="ad-loading">
      <el-skeleton :rows="3" animated/>
    </div>

    <div v-else-if="error" class="ad-error">
      <el-alert :title="error" type="error" :closable="false"/>
    </div>

    <div v-else-if="currentAd" class="ad-content">
      <div class="ad-label">赞助商</div>

      <div
          class="ad-item"
          @click="handleAdClick(currentAd.id)"
      >
        <div v-if="currentAd.type === 'image'" class="image-ad">
          <img
              :src="currentAd.content"
              :alt="currentAd.title"
              class="ad-image"
          >
          <div class="ad-title">{{ currentAd.title }}</div>
        </div>

        <div v-else-if="currentAd.type === 'text'" class="text-ad">
          <h4>{{ currentAd.title }}</h4>
          <p>{{ currentAd.content }}</p>
        </div>

        <div v-else class="default-ad">
          {{ currentAd.content }}
        </div>
      </div>
    </div>

    <div v-else class="ad-placeholder">
      <el-empty description="暂无广告" :image-size="60"/>
    </div>
  </div>
</template>

<style scoped lang="scss">
.advertising-container {
  margin-top: 20px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: var(--el-bg-color);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
  }

  .ad-label {
    padding: 8px 16px;
    background-color: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
    font-size: 12px;
    font-weight: bold;
    text-align: center;
    text-transform: uppercase;
  }

  .ad-content {
    padding: 16px;

    .ad-item {
      cursor: pointer;
      transition: transform 0.2s;

      &:hover {
        transform: translateY(-2px);
      }
    }

    .image-ad {
      .ad-image {
        width: 100%;
        border-radius: 4px;
        margin-bottom: 8px;
      }

      .ad-title {
        font-size: 14px;
        font-weight: 500;
        text-align: center;
        color: var(--el-text-color-primary);
      }
    }

    .text-ad {
      h4 {
        margin: 0 0 8px 0;
        font-size: 16px;
        color: var(--el-color-primary);
      }

      p {
        margin: 0;
        font-size: 14px;
        color: var(--el-text-color-regular);
        line-height: 1.5;
      }
    }
  }

  .ad-loading, .ad-error, .ad-placeholder {
    padding: 20px;
  }
}
</style>