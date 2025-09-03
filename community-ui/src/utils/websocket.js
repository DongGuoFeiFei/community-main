// /utils/websocket.js
import {Client} from '@stomp/stompjs';
import {ref} from 'vue';
import {localStores} from '@/stores/localStores';

let client = null;
const isConnected = ref(false);
const privateMessages = ref([]);
const error = ref(null);

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

    // 断开连接
    const disconnect = () => {
        if (client) {
            client.deactivate();
            client = null;
            isConnected.value = false;
            console.log('WebSocket disconnected');
        }
    };

    // 连接成功回调
    const onConnect = (frame) => {
        isConnected.value = true;
        error.value = null;
        console.log('WebSocket connected successfully');

        // 订阅私有频道
        client.subscribe(`/user/chatRoom.private.${store.userInfo.userInfo.userId}`, (message) => {
            const msg = JSON.parse(message.body);
            handleIncomingMessage(msg);
        });
    };

    // 错误处理
    const onError = (frame) => {
        error.value = `WebSocket error: ${frame.headers.message}`;
        console.error('STOMP error:', frame);
        isConnected.value = false;
    };

    // 断开连接处理
    const onDisconnect = () => {
        isConnected.value = false;
        console.log('WebSocket disconnected');
    };

    // 处理接收到的消息
    const handleIncomingMessage = (message) => {
        message.timestamp = new Date(message.timestamp);
        privateMessages.value.push(message);
        console.log("privateMessages.value", privateMessages.value)
    };

    // 发送私聊消息
    const sendPrivateMessage = (content, sessionId) => {
        if (!client?.connected) {
            throw new Error('WebSocket not connected');
        }

        const message = {
            content: content,
            sender: store.userInfo.userInfo.username,
            senderId: store.userInfo.userInfo.userId,
            sessionId: sessionId,
            timestamp: new Date().toISOString()
        };

        client.publish({
            destination: `/app/privateChat.${sessionId}`,
            body: JSON.stringify(message),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    return {
        // 连接管理
        connect,
        disconnect,
        isConnected,

        // 消息发送
        sendPrivateMessage,

        // 数据
        privateMessages,

        // 错误处理
        error,

        // 工具方法
        clearMessages: () => {
            privateMessages.value = [];
        }
    };
}
