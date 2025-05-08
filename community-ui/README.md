# community-ui

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```

### Run Unit Tests with [Vitest](https://vitest.dev/)

```sh
npm run test:unit
```


### 项目目录结构

```
my-vue-project/
├── public/                      # 公共静态资源
│   ├── index.html               # 主页面 HTML 模板
│   ├── favicon.ico              # 网站图标
│   └── assets/                  # 存放静态资源如图片、字体等
├── src/                         # 源代码
│   ├── assets/                  # 静态资源（图片、字体、样式等）
│   │   ├── logo.png             # logo 图片
│   │   └── style.css            # 全局样式
│   ├── components/              # 可复用的 UI 组件
│   │   ├── Header.vue           # 页头组件
│   │   ├── Sidebar.vue          # 侧边栏组件
│   │   ├── ArticleCard.vue      # 文章卡片组件
│   │   └── Footer.vue           # 页脚组件
│   ├── composables/             # 组合式 API 逻辑
│   │   ├── useFetch.js          # 获取数据的钩子
│   │   └── useAuth.js           # 用户认证相关的钩子
│   ├── layouts/                 # 布局组件（全局布局）
│   │   ├── DefaultLayout.vue    # 默认布局（带侧边栏、头部、底部）
│   │   └── EmptyLayout.vue      # 空布局（没有头部、侧边栏等）
│   ├── router/                  # 路由配置
│   │   └── sessionStores.js             # 路由配置文件
│   ├── store/                   # 状态管理（Pinia）
│   │   ├── sessionStores.js             # 状态管理入口
│   │   └── useArticles.js       # 文章相关的状态管理
│   ├── views/                   # 页面视图
│   │   ├── Home.vue             # 首页
│   │   ├── Article.vue          # 文章详情页面
│   │   ├── About.vue            # 关于我页面
│   │   ├── Category.vue         # 分类页面
│   │   └── Contact.vue          # 联系我们页面
│   ├── App.vue                  # 根组件
│   ├── main.js                  # 项目入口文件
│   └── style/                   # 全局样式
│       ├── variables.less       # 全局 LESS 变量
│       ├── layout.less          # 全局 layout 样式
│       └── global.less          # 全局样式
├── .gitignore                   # Git 忽略文件
├── package.json                 # 项目依赖及配置信息
├── vite.config.js               # Vite 配置文件
└── README.md                    # 项目说明文件
```

### 目录结构说明

- **`public/`**: 存放静态资源文件，像是 HTML 模板、图标、外部链接等。`index.html` 是你的主 HTML 页面。

- **`src/`**: 项目的源代码，主要包含 Vue 组件、路由配置、状态管理、页面视图等。

    - **`assets/`**: 存放图片、图标、字体等静态资源。
    - **`components/`**: 存放可以复用的组件，例如 `Header.vue`, `Sidebar.vue` 等，这些组件可以在多个页面中使用。
    - **`composables/`**: 存放组合式 API 相关的自定义钩子。比如你可以创建 `useFetch.js` 来封装 API 请求的逻辑，或者 `useAuth.js` 来管理用户认证。
    - **`layouts/`**: 存放布局组件。`DefaultLayout.vue` 是默认的带侧边栏和页头的布局，`EmptyLayout.vue` 是不带侧边栏和头部的空布局，适用于某些特定页面。
    - **`router/`**: 存放 Vue Router 配置文件，`sessionStores.js` 中包含了应用的路由信息。
    - **`store/`**: 存放状态管理相关的文件。可以使用 Pinia 来管理应用的状态。
    - **`views/`**: 存放页面级组件，如首页（`Home.vue`）、文章详情页（`Article.vue`）、关于我页面（`About.vue`）等。
    - **`App.vue`**: 根组件，作为应用的容器，包含整体布局。
    - **`main.js`**: 应用的入口文件，通常在这里创建 Vue 实例并挂载到 DOM 上。
    - **`style/`**: 存放全局样式文件，例如 LESS 变量、全局样式等。

- **`.gitignore`**: 用于 Git 管理的忽略文件，通常用来排除不需要上传到 Git 仓库的文件，如 `node_modules/`、构建输出等。

- **`package.json`**: 项目的依赖文件，包含了项目的所有依赖包以及配置。

- **`vite.config.js`**: Vite 配置文件，包含项目的构建和开发配置。

- **`README.md`**: 项目说明文件，描述项目的功能、安装步骤、使用方法等。

### 组织原则

1. **组件化**: 将页面分解为多个可复用的组件，比如 `Header.vue`, `Sidebar.vue` 和 `ArticleCard.vue`。这样可以提高代码的复用性和可维护性。

2. **布局和视图分离**: 使用布局组件来定义页面的基本结构，页面视图只专注于展示数据。`DefaultLayout.vue` 用于大多数页面，而 `EmptyLayout.vue` 适用于需要全屏显示或无侧边栏的页面。

3. **状态管理**: 使用 `Pinia` 来管理应用的状态，尤其是需要跨页面共享的数据。比如你可以在 `useArticles.js` 中存储和管理所有文章的列表，供多个页面使用。

4. **路由管理**: 将所有页面的路由配置集中在 `router/sessionStores.js` 文件中，便于管理和扩展。

5. **组合式 API (Composition API)**: 代码组织和逻辑封装更加灵活，组合式 API 能够使你的逻辑更加模块化。你可以将不同的业务逻辑封装在 `composables/` 文件夹下，提升代码的复用性。

6. **样式管理**: 全局样式和变量应当分开，保持清晰和易于维护。`variables.less` 存放全局样式变量，而 `global.less` 存放全局样式规则。
