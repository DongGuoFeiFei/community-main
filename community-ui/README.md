# 🌸 采芙蓉 - Community UI

<div align="center">

一个基于 Vue 3 + Vite 构建的现代化社区论坛平台

[![Vue](https://img.shields.io/badge/Vue-3.5.13-brightgreen.svg)](https://vuejs.org/)
[![Vite](https://img.shields.io/badge/Vite-6.2.4-646CFF.svg)](https://vitejs.dev/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-2.10.2-409EFF.svg)](https://element-plus.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

</div>

---

## 📖 项目简介

**采芙蓉社区** 是一个功能丰富的内容社区平台，提供文章发布、实时聊天、社交互动等功能。项目采用现代化的前端技术栈，注重用户体验和交互设计。

### ✨ 核心特性

- 🚀 **现代化技术栈** - Vue 3 Composition API + Vite + Pinia
- 📝 **富文本编辑** - 集成 TipTap 和 Vditor 双编辑器
- 💬 **实时通信** - 基于 WebSocket 的即时聊天系统
- 🎨 **精美 UI** - Element Plus 组件库 + 自定义主题
- 🎭 **Live2D 看板娘** - 多角色交互式看板娘
- 🔔 **消息通知** - 实时消息提醒系统
- 👥 **社交功能** - 关注/粉丝、点赞、收藏
- 🎯 **积分系统** - 用户积分与等级管理
- 🤖 **AI 集成** - DeepSeek AI 对话功能
- 🎪 **粒子特效** - 炫酷的页面动画效果

---

## 🛠️ 技术栈

### 核心框架

- **Vue 3.5.13** - 渐进式 JavaScript 框架
- **Vite 6.2.4** - 下一代前端构建工具
- **Vue Router 4.5.0** - 官方路由管理器
- **Pinia 3.0.1** - Vue 状态管理库
- **TypeScript 5.7.2** - JavaScript 的超集（支持 JS/TS 混合开发）

### UI 框架与组件

- **Element Plus 2.10.2** - Vue 3 组件库
- **Remixicon 4.6.0** - 图标库
- **@iconify/vue** - 图标框架

### 富文本编辑

- **TipTap 3.0.7** - 无头编辑器框架
- **Vditor 3.11.0** - Markdown 编辑器

### 实时通信

- **@stomp/stompjs** - STOMP 协议客户端
- **sockjs-client** - WebSocket 客户端

### 数据处理

- **Axios 1.8.4** - HTTP 客户端
- **Day.js** - 轻量级日期处理
- **DOMPurify** - XSS 过滤器

### 特效与交互

- **pixi-live2d-display** - Live2D 渲染
- **@tsparticles** - 粒子动画
- **NProgress** - 页面加载进度条

### 开发工具

- **Vitest** - 单元测试框架
- **Sass/Less** - CSS 预处理器

---

## 📁 项目结构

```
community-ui/
├── public/                          # 静态资源目录
│   ├── favicon.png                  # 网站图标
│   ├── live2d/                      # Live2D 模型文件
│   │   ├── alya/                    # Alya 角色
│   │   ├── Castorice_V2/           # Castorice 角色
│   │   ├── Hu Tao/                 # 胡桃角色
│   │   ├── Murasame/               # 村雨角色
│   │   ├── tako m/                 # Tako 角色
│   │   ├── tutu/                   # Tutu 角色
│   │   └── 草神/                    # 草神角色
│   ├── live2dcubismcore.min.js     # Live2D 核心库
│   └── 芙蓉花.png                   # 品牌图片
│
├── src/                             # 源代码目录
│   ├── api/                         # API 接口层
│   │   ├── advertising.js           # 广告相关
│   │   ├── announcement.js          # 公告相关
│   │   ├── article.js               # 文章相关
│   │   ├── auth.js                  # 认证相关
│   │   ├── author.js                # 作者相关
│   │   ├── category.js              # 分类相关
│   │   ├── chat.js                  # 聊天相关
│   │   ├── collectApi.js            # 收藏相关
│   │   ├── deepseek.js              # AI 对话相关
│   │   ├── files.js                 # 文件上传
│   │   ├── follow.js                # 关注相关
│   │   ├── likeApi.js               # 点赞相关
│   │   ├── message.js               # 消息相关
│   │   ├── notification.js          # 通知相关
│   │   ├── payment.js               # 支付相关
│   │   ├── points.js                # 积分相关
│   │   ├── report.js                # 举报相关
│   │   ├── session.js               # 会话相关
│   │   ├── tag.js                   # 标签相关
│   │   └── user.js                  # 用户相关
│   │
│   ├── assets/                      # 静态资源
│   │   ├── emoji-data.js            # Emoji 数据
│   │   ├── images/                  # 图片资源
│   │   └── scss/                    # SCSS 样式
│   │
│   ├── components/                  # 全局组件
│   │   ├── Advertising.vue          # 广告组件
│   │   ├── Announcement.vue         # 公告组件
│   │   ├── GlobalLoginDialog.vue    # 全局登录弹窗
│   │   ├── NotificationBadge.vue    # 通知徽章
│   │   ├── NotificationPanel.vue    # 通知面板
│   │   ├── Live2D/                  # Live2D 相关组件
│   │   ├── Particles/               # 粒子效果组件
│   │   └── payment/                 # 支付组件
│   │
│   ├── router/                      # 路由配置
│   │   └── index.js                 # 路由定义与守卫
│   │
│   ├── stores/                      # 状态管理
│   │   ├── localStores.js           # 本地持久化状态
│   │   └── sessionStores.js         # 会话状态
│   │
│   ├── styles/                      # 全局样式
│   │   ├── skeleton.scss            # 骨架屏样式
│   │   ├── style.scss               # 全局样式
│   │   └── theme.scss               # 主题样式
│   │
│   ├── utils/                       # 工具函数
│   │   ├── date.js                  # 日期处理
│   │   ├── env.js                   # 环境配置
│   │   ├── format.js                # 格式化工具
│   │   ├── progress.js              # 进度条配置
│   │   ├── protect.js               # 安全防护
│   │   ├── request.js               # HTTP 请求封装
│   │   └── websocket.js             # WebSocket 封装
│   │
│   ├── views/                       # 页面视图
│   │   ├── auth/                    # 认证相关页面
│   │   │   ├── Login.vue            # 登录页
│   │   │   ├── Register.vue         # 注册页
│   │   │   ├── ForgotPassword.vue   # 忘记密码
│   │   │   └── NotFound.vue         # 404 页面
│   │   │
│   │   ├── pages/                   # 功能页面
│   │   │   └── views/
│   │   │       ├── home/            # 首页
│   │   │       ├── article/         # 文章详情
│   │   │       ├── edit/            # 文章编辑
│   │   │       ├── wsChat/          # 聊天室
│   │   │       ├── author/          # 作者主页
│   │   │       └── test/            # 测试页面
│   │   │
│   │   └── user/                    # 用户中心
│   │       └── views/
│   │           ├── profile/         # 个人资料
│   │           ├── articles/        # 我的文章
│   │           ├── collection/      # 我的收藏
│   │           ├── notification/    # 通知中心
│   │           ├── follow&fans/     # 关注与粉丝
│   │           └── pointAccount/    # 积分账户
│   │
│   ├── App.vue                      # 根组件
│   └── main.js                      # 应用入口
│
├── dist/                            # 构建输出目录
├── node_modules/                    # 依赖包
├── .gitignore                       # Git 忽略配置
├── index.html                       # HTML 模板
├── jsconfig.json                    # JS 配置
├── package.json                     # 项目依赖配置
├── package-lock.json                # 依赖锁定文件
├── vite.config.js                   # Vite 配置
├── vitest.config.js                 # Vitest 配置
└── README.md                        # 项目文档
```

---

## 🚀 快速开始

### 环境要求

- **Node.js**: >= 16.0.0
- **npm**: >= 8.0.0 或 **pnpm** / **yarn**

### 安装依赖

```bash
# 使用 npm
npm install

# 或使用 pnpm (推荐)
pnpm install

# 或使用 yarn
yarn install
```

### 开发环境运行

```bash
npm run dev
```

访问 http://localhost:5173 (默认端口)

### TypeScript 支持

本项目支持 **JavaScript 和 TypeScript 混合开发**：

- ✅ 现有的 JavaScript 代码完全不受影响
- ✅ 新功能可以使用 TypeScript
- ✅ 详细指南请查看 [TypeScript 迁移指南](./TS_MIGRATION_GUIDE.md)
- ✅ 快速上手请查看 [TypeScript 快速开始](./QUICK_START_TS.md)

### 生产环境构建

```bash
npm run build
```

构建产物将输出到 `dist/` 目录

### 预览构建结果

```bash
npm run preview
```

### 运行单元测试

```bash
npm run test:unit
```

---

## 🔧 配置说明

### 环境变量

创建 `.env` 文件配置环境变量：

```bash
# API 基础地址
VITE_API_BASE_URL=http://your-api-domain.com

# WebSocket 地址
VITE_WS_URL=ws://your-websocket-domain.com

# 其他配置...
```

### Vite 配置

编辑 `vite.config.js` 文件进行自定义配置：

```javascript
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      "/api": {
        target: "http://your-backend-url",
        changeOrigin: true,
      },
    },
  },
});
```

---

## 📝 开发指南

### 代码规范

- 使用 **Vue 3 Composition API** 编写组件
- 遵循 **ESLint** 规范
- 组件命名使用 **PascalCase**
- 文件命名使用 **kebab-case** 或 **PascalCase**
- **新功能推荐使用 TypeScript**（可选，不强制）
- 已有的 JavaScript 代码保持不变

### 路由管理

所有路由配置在 `src/router/index.js`：

```javascript
{
  path: '/article/:id',
  name: 'article',
  component: () => import('@/views/pages/views/article/Index.vue'),
  meta: {
    title: '文章详情',
    requiresAuth: true  // 需要登录
  }
}
```

### 状态管理

使用 Pinia 进行状态管理：

```javascript
// stores/userStore.js
import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    userInfo: null,
    token: null,
  }),
  actions: {
    setUser(user) {
      this.userInfo = user;
    },
  },
});
```

### API 调用

统一在 `src/api/` 目录下管理：

```javascript
// api/article.js
import request from "@/utils/request";

export const getArticleList = (params) => {
  return request.get("/api/articles", { params });
};

export const createArticle = (data) => {
  return request.post("/api/articles", data);
};
```

---

## 🎨 主要功能模块

### 1. 用户认证系统

- ✅ 用户注册、登录
- ✅ 找回密码
- ✅ Token 自动续期
- ✅ 路由权限守卫

### 2. 文章管理系统

- ✅ 富文本编辑器（TipTap + Vditor）
- ✅ Markdown 支持
- ✅ 图片上传
- ✅ 文章分类与标签
- ✅ 文章搜索

### 3. 社交互动

- ✅ 关注/粉丝系统
- ✅ 点赞功能
- ✅ 收藏功能
- ✅ 评论系统

### 4. 实时聊天

- ✅ WebSocket 长连接
- ✅ 多人聊天室
- ✅ 私信功能
- ✅ 消息提醒

### 5. 通知系统

- ✅ 实时消息推送
- ✅ 系统公告
- ✅ 站内信

### 6. 积分系统

- ✅ 积分获取与消费
- ✅ 等级体系
- ✅ 积分记录

### 7. Live2D 看板娘

- ✅ 多角色支持
- ✅ 交互动画
- ✅ 语音提示

---

## 🔐 安全特性

- ✅ **XSS 防护** - DOMPurify 过滤
- ✅ **CSRF 防护** - Token 验证
- ✅ **Token 过期检测** - 自动跳转登录
- ✅ **路由守卫** - 权限验证
- ✅ **敏感信息加密** - 本地存储加密

---

## 📱 浏览器支持

|  Chrome   |  Firefox  |  Safari   |   Edge    |
| :-------: | :-------: | :-------: | :-------: |
| ✅ Latest | ✅ Latest | ✅ Latest | ✅ Latest |

---

## 🤝 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

---

## 📄 开源协议

本项目采用 [MIT](LICENSE) 协议

---

## 👥 开发团队

- **开发者**: Your Name
- **设计师**: Designer Name
- **项目经理**: PM Name

---

## 📧 联系方式

- **项目地址**: [GitHub](https://github.com/your-username/community-ui)
- **问题反馈**: [Issues](https://github.com/your-username/community-ui/issues)
- **邮箱**: your-email@example.com

---

## 🙏 致谢

感谢以下开源项目：

- [Vue.js](https://vuejs.org/)
- [Vite](https://vitejs.dev/)
- [Element Plus](https://element-plus.org/)
- [TipTap](https://tiptap.dev/)
- [pixi-live2d-display](https://github.com/guansss/pixi-live2d-display)

---

<div align="center">

**⭐ 如果这个项目对你有帮助，请给一个 Star！⭐**

Made with ❤️ by Community Team

</div>
