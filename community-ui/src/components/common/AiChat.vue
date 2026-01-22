<template>
    <div class="matechat-demo" :style="{ height }">
        <mc-layout class="chat-layout">
            <mc-layout-content class="content-area" ref="contentRef">
                <template v-for="(msg, idx) in messages" :key="idx">
                    <!-- 用户气泡 -->
                    <McBubble v-if="msg.role === 'user'" :content="msg.content" align="right"
                        :avatar-config="{ displayName: userAvatar, name: userName, isRound: true }" />
                    <!-- AI 回复 -->
                    <McBubble v-else
                        :avatar-config="{ displayName: assistantAvatar, name: assistantName, isRound: true }"
                        :loading="msg.loading">
                        <McMarkdownCard v-if="!msg.loading" :content="msg.content" :typing="msg.typing" theme="light" />
                    </McBubble>
                </template>
            </mc-layout-content>

            <mc-layout-sender>
                <div class="sender-wrapper">
                    <el-button v-if="loading" type="danger" size="small" class="stop-btn" @click="handleStop" round>
                        <span style="display: flex; align-items: center; gap: 4px;">
                            <span style="font-size: 14px;">⏸</span>
                            <span>停止生成</span>
                        </span>
                    </el-button>
                    <mc-input :value="inputText" :placeholder="placeholder" :disabled="loading"
                        @change="(e) => inputText = e" @submit="handleSend" />
                </div>
            </mc-layout-sender>
        </mc-layout>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { McLayout, McLayoutContent, McLayoutSender, McInput, McBubble, McMarkdownCard } from '@matechat/core'
import { ElMessage } from 'element-plus'
import { localStores } from '@/stores/localStores'

const props = defineProps({
    // API 配置
    apiKey: { type: String, default: '' },
    apiEndpoint: { type: String, default: '' },
    model: { type: String, default: '' },
    // UI 配置
    welcomeMessage: { type: String, default: '你好！我是 AI 助手，有什么可以帮您？' },
    placeholder: { type: String, default: '输入消息...' },
    height: { type: String, default: '600px' },
    // 头像配置
    userAvatar: { type: String, default: '👤' },
    assistantAvatar: { type: String, default: '🤖' },
    userName: { type: String, default: '用户' },
    assistantName: { type: String, default: 'AI 助手' },
    // 请求配置
    temperature: { type: Number, default: 0.7 },
    stream: { type: Boolean, default: true }
})

const emit = defineEmits(['send', 'message', 'error', 'stop'])

const inputText = ref('')
const loading = ref(false)
const messages = ref([])
const contentRef = ref(null)
const abortController = ref(null)
const store = localStores()

// 实际使用的配置（props 优先，否则从 localStorage/env 获取）
const actualApiKey = computed(() =>
    props.apiKey || localStorage.getItem('apiKey') || import.meta.env.VITE_OPENAI_API_KEY || ''
)
const actualEndpoint = computed(() =>
    props.apiEndpoint || localStorage.getItem('apiEndpoint') || import.meta.env.VITE_OPENAI_API_ENDPOINT || ''
)
const actualModel = computed(() =>
    props.model || localStorage.getItem('selectedModel') || import.meta.env.VITE_DEFAULT_MODEL || 'deepseek-chat'
)

onMounted(() => {
    if (props.welcomeMessage) {
        messages.value = [{ role: 'assistant', content: props.welcomeMessage }]
    }
    if (store.aiChatMessage) {
        messages.value = store.aiChatMessage
    }
})

// 滚动到底部
const scrollToBottom = () => {
    nextTick(() => {
        const el = contentRef.value?.$el || contentRef.value
        if (el) el.scrollTop = el.scrollHeight
    })
}
watch(() => messages.value.length, scrollToBottom)

const handleSend = async (content) => {
    const text = content || inputText.value
    if (!text?.trim() || loading.value) return

    emit('send', text)
    loading.value = true
    messages.value.push({ role: 'user', content: text })
    inputText.value = ''

    const assistantMsg = { role: 'assistant', content: '', loading: true, typing: false }
    messages.value.push(assistantMsg)
    scrollToBottom()

    // 发送请求
    try {
        abortController.value = new AbortController()
        const response = await fetch(actualEndpoint.value, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${actualApiKey.value}` },
            body: JSON.stringify({
                model: actualModel.value,
                messages: messages.value.filter(m => !m.loading).map(m => ({ role: m.role, content: m.content })),
                stream: props.stream,
                temperature: props.temperature
            }),
            signal: abortController.value.signal
        })

        if (!response.ok) throw new Error(`请求失败: ${response.status}`)

        assistantMsg.loading = false
        assistantMsg.typing = true
        const reader = response.body.getReader()
        const decoder = new TextDecoder()
        let buffer = ''

        // 对信息流（获取的信息）进行处理
        while (true) {
            const { done, value } = await reader.read()
            if (done) break
            buffer += decoder.decode(value, { stream: true })
            const lines = buffer.split('\n')
            buffer = lines.pop() || ''
            for (const line of lines) {
                if (line.startsWith('data: ')) {
                    const data = line.slice(6)
                    if (data === '[DONE]') continue
                    try {
                        const parsed = JSON.parse(data)
                        if (parsed.choices?.[0]?.delta?.content) {
                            assistantMsg.content += parsed.choices[0].delta.content
                            messages.value = [...messages.value]
                            scrollToBottom()
                        }
                    } catch { }
                }
            }
        }
        assistantMsg.typing = false
        emit('message', assistantMsg.content)
    } catch (error) {
        if (error.name === 'AbortError') {
            assistantMsg.typing = false
            emit('stop', assistantMsg.content)
            ElMessage.info('已停止生成')
        } else {
            emit('error', error)
            ElMessage.error('发送失败: ' + error.message)
            messages.value.pop()
        }
    } finally {
        loading.value = false
        abortController.value = null
    }
}

const handleStop = () => {
    if (abortController.value) {
        abortController.value.abort()
    }
}

// 暴露方法供外部调用
defineExpose({ messages, loading, handleSend, handleStop })


</script>

<style scoped>
.matechat-demo {
    width: 100%;
    border: 1px solid #e4e7ed;
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    background: #ffffff;
}

.chat-layout {
    height: 100%;
    display: flex;
    flex-direction: column;
    background: linear-gradient(to bottom, #f8f9fa 0%, #ffffff 100%);
}

.content-area {
    flex: 1;
    overflow-y: auto;
    padding: 1.5rem;
    background: transparent;
    display: flex;
    flex-direction: column;
    gap: 16px;
}

/* 美化滚动条 */
.content-area::-webkit-scrollbar {
    width: 6px;
}

.content-area::-webkit-scrollbar-track {
    background: transparent;
}

.content-area::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 3px;
}

.content-area::-webkit-scrollbar-thumb:hover {
    background: #c0c4cc;
}

.sender-wrapper {
    position: relative;
    width: 100%;
    padding: 1rem;
    background: #ffffff;
    border-top: 1px solid #e4e7ed;
}

.stop-btn {
    position: absolute;
    top: 1.25rem;
    right: 2.25rem;
    z-index: 10;
    border-radius: 20px;
    padding: 6px 16px;
    font-size: 13px;
    box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
    animation: pulse 2s infinite;
}

@keyframes pulse {

    0%,
    100% {
        box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
    }

    50% {
        box-shadow: 0 2px 12px rgba(245, 108, 108, 0.5);
    }
}

/* 美化气泡样式 */
.content-area :deep(.mc-bubble) {
    animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* 美化输入框 */
.sender-wrapper :deep(.mc-input) {
    border-radius: 8px;
    transition: all 0.3s;
}

.sender-wrapper :deep(.mc-input:focus-within) {
    box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.1);
}

/* 美化 Markdown 卡片 */
.content-area :deep(.mc-markdown-card) {
    border-radius: 8px;
}

/* 美化代码块 */
.content-area :deep(pre) {
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

/* 响应式设计 */
@media (max-width: 768px) {
    .content-area {
        padding: 1rem;
        gap: 12px;
    }

    .sender-wrapper {
        padding: 0.75rem;
    }

    .stop-btn {
        top: 1rem;
        right: 1rem;
        font-size: 12px;
        padding: 5px 12px;
    }
}
</style>
