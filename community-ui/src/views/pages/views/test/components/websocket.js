// /utils/websocket.js
import { Client } from '@stomp/stompjs';
import { ref } from 'vue';
import { localStores } from '@/stores/localStores';

let client = null;
const isConnected = ref(false);
const privateMessages = ref({}); // 按sessionId存储消息
const error = ref(null);
const subscriptions = ref({}); // 存储所有订阅

const store = localStores();

export function useChatWebSocket() {
    // 连接WebSocket
    const connect = () => {
        if (client && client.connected) {
            console.log('WebSocket already connected');
            return Promise.resolve();
        }

        return new Promise((resolve, reject) => {
            client = new Client({
                brokerURL: `ws://localhost:8081/ws`,
                connectHeaders: {
                    Authorization: `Bearer ${store.tokenInfo.token}`,
                },
                onWebSocketError: (event) => {
                    console.error('WebSocket Error:', event);
                    reject(event);
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                onConnect: (frame) => {
                    onConnect(frame);
                    resolve(frame);
                },
                onStompError: (frame) => {
                    onError(frame);
                    reject(frame);
                },
                onDisconnect: onDisconnect
            });

            client.activate();
        });
    };

    // 订阅特定聊天室
    const subscribeToRoom = (sessionId) => {
        if (!client?.connected) {
            throw new Error('WebSocket not connected');
        }

        if (subscriptions.value[sessionId]) {
            console.log(`[WebSocket] 已经订阅过聊天室: ${sessionId}`);
            return;
        }

        console.log(`[WebSocket] 正在订阅聊天室: ${sessionId}`);

        // 修正订阅路径 - 根据你的后端配置
        const publicTopic = `/topic/chatRoom.private.${sessionId}`;
        const privateQueue = `/user/queue/private`;

        // 订阅公共主题
        const publicSub = client.subscribe(publicTopic, (message) => {
            console.log(`[WebSocket] 收到公共主题消息:`, message);
            const msg = JSON.parse(message.body);
            handleIncomingMessage(msg, sessionId);
        });

        // 订阅私人队列
        const privateSub = client.subscribe(privateQueue, (message) => {
            console.log(`[WebSocket] 收到私人队列消息:`, message);
            const msg = JSON.parse(message.body);
            if (msg.sessionId === sessionId) {
                handleIncomingMessage(msg, sessionId);
            }
        });

        subscriptions.value[sessionId] = {
            public: publicSub,
            private: privateSub
        };

        privateMessages.value[sessionId] = privateMessages.value[sessionId] || [];
        console.log(`[WebSocket] 聊天室 ${sessionId} 订阅成功`);
    };

    // 取消订阅特定聊天室
    const unsubscribeFromRoom = (sessionId) => {
        if (subscriptions.value[sessionId]) {
            subscriptions.value[sessionId].public.unsubscribe();
            subscriptions.value[sessionId].private.unsubscribe();
            delete subscriptions.value[sessionId];
            console.log(`[WebSocket] 已取消订阅聊天室: ${sessionId}`);
        }
    };

    // 连接成功回调
    const onConnect = (frame) => {
        isConnected.value = true;
        error.value = null;
        console.log('[WebSocket] 连接成功', frame);
    };

    // 处理接收到的消息（按sessionId分类）
    const handleIncomingMessage = (message, sessionId) => {
        console.log(`[WebSocket] 处理聊天室 ${sessionId} 的消息`, message);

        // 确保消息有timestamp
        if (!message.timestamp) {
            message.timestamp = new Date().toISOString();
        }

        if (!privateMessages.value[sessionId]) {
            privateMessages.value[sessionId] = [];
        }

        // 避免重复消息
        const exists = privateMessages.value[sessionId].some(
            m => m.timestamp === message.timestamp && m.senderId === message.senderId
        );

        if (!exists) {
            privateMessages.value[sessionId].push({
                ...message,
                timestamp: new Date(message.timestamp)
            });
        }

        console.log(`[WebSocket] 聊天室 ${sessionId} 当前消息:`, privateMessages.value[sessionId]);
    };

    // 发送私聊消息
    const sendPrivateMessage = async (content, sessionId) => {
        if (!client?.connected) {
            await connect();
        }

        const message = {
            content: content,
            sender: store.userInfo.userInfo.username,
            senderId: store.userInfo.userInfo.userId,
            sessionId: sessionId,
            timestamp: new Date().toISOString()
        };

        console.log(`[WebSocket] 发送消息到聊天室 ${sessionId}:`, message);

        client.publish({
            destination: `/app/privateChat.${sessionId}`,
            body: JSON.stringify(message),
            headers: { Authorization: `Bearer ${store.tokenInfo.token}` }
        });
    };

    // 获取特定聊天室的消息
    const getMessagesBySession = (sessionId) => {
        return privateMessages.value[sessionId] || [];
    };

    // 断开连接
    const disconnect = () => {
        if (client) {
            Object.keys(subscriptions.value).forEach(unsubscribeFromRoom);
            client.deactivate();
            client = null;
            isConnected.value = false;
            console.log('[WebSocket] 已断开连接');
        }
    };

    // 错误处理
    const onError = (frame) => {
        error.value = `WebSocket error: ${frame.headers.message}`;
        console.error('[WebSocket] STOMP协议错误:', frame);
        isConnected.value = false;
    };

    // 断开连接处理
    const onDisconnect = () => {
        isConnected.value = false;
        console.log('[WebSocket] 连接已断开');
    };

    return {
        connect,
        disconnect,
        isConnected,
        subscribeToRoom,
        unsubscribeFromRoom,
        sendPrivateMessage,
        getMessagesBySession,
        error
    };
}
