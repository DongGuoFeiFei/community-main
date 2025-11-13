/* nodejs/validate_login_ui.js
   简要验证：
   - 路由包含 /login
   - Login.vue 使用了 TypeScript 与 SCSS
   - 组件文件存在并含模板
*/
const fs = require('fs')
const path = require('path')

const base = path.resolve(__dirname, '..')
const routerFile = path.join(base, 'src', 'router', 'index.js')
const loginFile = path.join(base, 'src', 'views', 'auth', 'Login.vue')
const compDir = path.join(base, 'src', 'views', 'auth', 'components')

function assert(cond, msg) {
  if (!cond) throw new Error(msg)
}

function read(p) {
  return fs.readFileSync(p, 'utf8')
}

function main() {
  const r = read(routerFile)
  assert(/path:\s*['"]\/login['"]/g.test(r), 'router: 未找到 /login 路由')

  const v = read(loginFile)
  assert(/<script setup lang="ts">/.test(v), 'Login.vue: 需使用 TypeScript')
  assert(/<style[^>]*lang="scss"/.test(v), 'Login.vue: 需使用 SCSS 样式')
  assert(/<template>[\s\S]*<\/template>/.test(v), 'Login.vue: 缺少模板')

  assert(fs.existsSync(path.join(compDir, 'AuthBrand.vue')), '组件缺失: AuthBrand.vue')
  assert(fs.existsSync(path.join(compDir, 'CaptchaInput.vue')), '组件缺失: CaptchaInput.vue')

  const c1 = read(path.join(compDir, 'AuthBrand.vue'))
  const c2 = read(path.join(compDir, 'CaptchaInput.vue'))
  assert(/<template>/.test(c1) && /<template>/.test(c2), '组件: 模板缺失')

  console.log('[OK] 登录页UI结构验证通过')
}

try { main() } catch (e) { console.error('[FAIL]', e.message); process.exit(1) }
