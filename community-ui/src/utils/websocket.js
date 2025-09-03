// /utils/websocket.js
import { Client } from '@stomp/stompjs';
import { ref } from 'vue';
import { localStores } from '@/stores/localStores';

let client = null;
const isConnected = ref(false);
const privateMessages = ref({}); // 改为对象，按sessionId存储消息
const error = ref(null);
const subscriptions = ref({}); // 存储所有订阅

const store = localStores();

export function useChatWebSocket() {
    // 连接WebSocket
    const connect = () => {
        if (client && client.connected) {
            console.log('WebSocket already connected');
            return;
        }

        client = new Client({
            brokerURL: `ws://localhost:8081/ws`,
            connectHeaders: {
                Authorization: `Bearer ${store.tokenInfo.token}`,
            },
            onWebSocketError: (event) => {
                console.error('WebSocket Error:', event);
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 4000,
            heartbeatOutgoing: 4000,
            onConnect: onConnect,
            onStompError: onError,
            onDisconnect: onDisconnect
        });

        client.activate();
    };

    // 订阅特定聊天室
    const subscribeToRoom = (sessionId) => {
        if (!client?.connected || subscriptions.value[sessionId]) return;

        console.log(`[WebSocket] 正在订阅聊天室: ${sessionId}`);

        const subscription = client.subscribe(
            `/chatRoom.private.${sessionId}`,
            (message) => {
                console.log(`[WebSocket] 收到来自聊天室 ${sessionId} 的消息:`, message);
                const msg = JSON.parse(message.body);
                console.log('解析后的消息内容:', msg);
                handleIncomingMessage(msg, sessionId);
            }
        );

        subscriptions.value[sessionId] = subscription;
        privateMessages.value[sessionId] = privateMessages.value[sessionId] || [];
        console.log(`[WebSocket] 聊天室 ${sessionId} 订阅成功`);
    };

    // 取消订阅特定聊天室
    const unsubscribeFromRoom = (sessionId) => {
        if (subscriptions.value[sessionId]) {
            subscriptions.value[sessionId].unsubscribe();
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
        message.timestamp = new Date(message.timestamp);
        if (!privateMessages.value[sessionId]) {
            privateMessages.value[sessionId] = [];
        }
        privateMessages.value[sessionId].push(message);

        console.log(`[WebSocket] 聊天室 ${sessionId} 当前消息:`, privateMessages.value[sessionId]);
    };

    // 发送私聊消息
    const sendPrivateMessage = (content, sessionId) => {
        if (!client?.connected) throw new Error('WebSocket not connected');

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
