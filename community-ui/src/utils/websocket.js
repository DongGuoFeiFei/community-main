// /utils/websocket.js
import { Client } from '@stomp/stompjs';
import { ref } from 'vue';
import { localStores } from '@/stores/localStores';

let client = null;
const isConnected = ref(false);
const error = ref(null);

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
                    isConnected.value = true;
                    error.value = null;
                    console.log('[WebSocket] 连接成功', frame);
                    resolve(frame);
                },
                onStompError: (frame) => {
                    error.value = `WebSocket error: ${frame.headers.message}`;
                    console.error('[WebSocket] STOMP协议错误:', frame);
                    isConnected.value = false;
                    reject(frame);
                },
                onDisconnect: () => {
                    isConnected.value = false;
                    console.log('[WebSocket] 连接已断开');
                }
            });

            client.activate();
        });
    };

    // 订阅特定聊天室
    const subscribe = (destination, callback) => {
        if (!client?.connected) {
            throw new Error('WebSocket not connected');
        }

        console.log(`[WebSocket] 正在订阅: ${destination}`);

        return client.subscribe(destination, (message) => {
            try {
                const msg = JSON.parse(message.body);
                console.log(`[WebSocket] 收到消息:`, msg);
                callback(msg);
            } catch (e) {
                console.error('消息解析失败:', e);
            }
        });
    };

    // 发送消息
    const send = (destination, message) => {
        if (!client?.connected) {
            throw new Error('WebSocket not connected');
        }

        console.log(`[WebSocket] 发送消息到 ${destination}:`, message);

        client.publish({
            destination,
            body: JSON.stringify(message),
            headers: { Authorization: `Bearer ${store.tokenInfo.token}` }
        });
    };

    // 断开连接
    const disconnect = () => {
        if (client) {
            client.deactivate();
            client = null;
            isConnected.value = false;
            console.log('[WebSocket] 已断开连接');
        }
    };

    return {
        connect,
        disconnect,
        subscribe,
        send,
        isConnected,
        error
    };
}
