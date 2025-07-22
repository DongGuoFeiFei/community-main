<template>
  <div class="author-card">
    <div class="author-header">
      <el-avatar :size="80" :src="store.baseURL + authorInfo.avatar"/>
      <router-link :to="{ path: `/author/${authorInfo.id}` }" target="_blank"><h3>{{ authorInfo.nickname }}</h3></router-link>
    </div>

    <div class="author-bio">
      <p>{{ authorInfo.bio || '这个作者很懒，什么都没留下~' }}</p>
    </div>

    <div class="author-stats">
      <div class="stat-item">
        <span class="stat-number">{{ authorInfo.postCount }}</span>
        <span class="stat-label">文章</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">{{ authorInfo.followingCount }}</span>
        <span class="stat-label">粉丝</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">{{ authorInfo.followerCount }}</span>
        <span class="stat-label">关注</span>
      </div>
    </div>

    <div class="author-actions">
      <el-button
          :type="authorInfo.isFollowing ? 'default' : 'primary'"
          size="small"
          @click="toggleFollow"
          :disabled="Number(authorInfo.id) === store.userInfo.userInfo.userId"
      >
        {{ authorInfo.isFollowing ? '已关注' : '关注' }}
      </el-button>
    </div>

    <div class="author-meta">
      <el-tag size="small" type="info">
        <i class="el-icon-time"></i> 加入于 {{ authorInfo.joinDate }}
      </el-tag>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue'
import {getUserInfo} from '@/api/user.js'
import {ElAvatar, ElButton, ElTag} from 'element-plus'
import {localStores} from "@/stores/localStores.js";
import {addFollowAuthor, delFollowAuthor} from "@/api/follow.js";


const props = defineProps({
  articleId: {
    type: Number,
    required: true
  }
})

const store = localStores()
const authorInfo = ref({
  id: null,
  username: '',
  nickname: '',
  avatar: '',
  bio: '',
  joinDate: '',
  postCount: 0,
  followerCount: 0,
  followingCount: 0,
  isFollowing: false
})

// 获取作者信息
const fetchAuthorInfo = async () => {
  try {
    if (props.articleId) {
      const res = await getUserInfo(props.articleId)
      authorInfo.value = res.data
      console.log(authorInfo.value)
    }
  } catch (error) {
    console.error('获取作者信息失败:', error)
  }
}

// 关注/取消关注
const toggleFollow = () => {
  if (authorInfo.value.isFollowing) {
    delFollowAuthor(authorInfo.value.id)
    authorInfo.value.isFollowing = !authorInfo.value.isFollowing
    authorInfo.value.followingCount += authorInfo.value.isFollowing ? 1 : -1
  } else {
    addFollowAuthor(authorInfo.value.id)
    authorInfo.value.isFollowing = !authorInfo.value.isFollowing
    authorInfo.value.followingCount += authorInfo.value.isFollowing ? 1 : -1
  }
}

onMounted(() => {
  fetchAuthorInfo()
})

// 监听路由参数变化
watch(
    () => props.articleId,
    () => {
        fetchAuthorInfo()
    }
)
</script>

<style scoped lang="scss">
.author-card {
  max-width: 100%;
  margin: 40px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
  position: sticky;
  top: 20px;

  .author-header {
    margin-bottom: 15px;

    h3 {
      margin: 10px 0 5px;
      font-size: 18px;
      color: #333;
    }

    .username {
      margin: 0;
      font-size: 14px;
      color: #999;
    }
  }

  .author-bio {
    margin: 15px 0;
    padding: 10px 0;
    border-top: 1px solid #f0f0f0;
    border-bottom: 1px solid #f0f0f0;
    font-size: 14px;
    color: #666;
    line-height: 1.6;
  }

  .author-stats {
    display: flex;
    justify-content: space-around;
    margin: 15px 0;

    .stat-item {
      display: flex;
      flex-direction: column;

      .stat-number {
        font-weight: bold;
        font-size: 16px;
        color: #333;
      }

      .stat-label {
        font-size: 12px;
        color: #999;
      }
    }
  }

  .author-actions {
    margin: 15px 0;
  }

  .author-meta {
    margin-top: 15px;
    font-size: 12px;
    color: #999;
  }
}
</style>