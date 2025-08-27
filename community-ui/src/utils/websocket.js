// /utils/websocket.js
import {Client} from '@stomp/stompjs';
import {computed, ref} from 'vue';
import {localStores} from '@/stores/localStores';

let client = null;
const isConnected = ref(false);
const messages = ref([]);
const privateMessages = ref([]);
const typingUsers = ref(new Set());
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
                Authorization: `Bearer ${store.tokenInfo.token}`
            },
            debug: function (str) {
                console.log('STOMP:', str); // 查看所有STOMP事件
            },
            onWebSocketError: (event) => {
                console.error('WebSocket Error:', event); // 捕获底层错误
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
            sendLeaveMessage();
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

        // 订阅所有频道
        subscribeToChannels();

        // 发送加入消息
        sendJoinMessage();
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

    // 订阅频道
    const subscribeToChannels = () => {
        // 公共频道
        client.subscribe('/topic/public', (message) => {
            const msg = JSON.parse(message.body);
            handleIncomingMessage(msg, 'public');
        });

        // 私有频道
        client.subscribe('/user/queue/private', (message) => {
            const msg = JSON.parse(message.body);
            handleIncomingMessage(msg, 'private');
        });

        // 系统频道
        client.subscribe('/topic/system', (message) => {
            const msg = JSON.parse(message.body);
            handleIncomingMessage(msg, 'system');
        });

        // 输入状态频道
        client.subscribe('/topic/typing', (message) => {
            const msg = JSON.parse(message.body);
            handleTypingIndicator(msg);
        });

        // 回执频道
        client.subscribe('/user/queue/receipts', (message) => {
            const msg = JSON.parse(message.body);
            handleReadReceipt(msg);
        });
    };

    // 处理接收到的消息
    const handleIncomingMessage = (message, type) => {
        message.timestamp = new Date(message.timestamp);

        if (type === 'public') {
            messages.value.push(message);
        } else if (type === 'private') {
            privateMessages.value.push(message);
        } else if (type === 'system') {
            messages.value.push(message);
        }
    };

    // 处理输入状态
    const handleTypingIndicator = (message) => {
        if (message.type === 'TYPING_START') {
            typingUsers.value.add(message.sender);
        } else if (message.type === 'TYPING_STOP') {
            typingUsers.value.delete(message.sender);
        }
    };

    // 处理已读回执
    const handleReadReceipt = (receipt) => {
        console.log('Message read receipt:', receipt);
    };

    // 发送公共消息
    const sendPublicMessage = (content) => {
        if (!client?.connected) {
            throw new Error('WebSocket not connected');
        }

        const message = {
            type: 'CHAT',
            content: content,
            sender: store.userInfo.userInfo.username,
            timestamp: new Date().toISOString()
        };

        client.publish({
            destination: '/app/chat.public',
            body: JSON.stringify(message),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    // 发送私聊消息
    const sendPrivateMessage = (content, receiver) => {
        if (!client?.connected) {
            throw new Error('WebSocket not connected');
        }

        const message = {
            type: 'CHAT',
            content: content,
            sender: store.userInfo.userInfo.username,
            receiver: receiver,
            timestamp: new Date().toISOString()
        };

        client.publish({
            destination: '/app/chat.private',
            body: JSON.stringify(message),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    // 发送加入消息
    const sendJoinMessage = () => {
        if (!client?.connected) return;

        const message = {
            type: 'JOIN',
            sender: store.userInfo.userInfo.username
        };

        client.publish({
            destination: '/app/chat.join',
            body: JSON.stringify(message),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    // 发送离开消息
    const sendLeaveMessage = () => {
        if (!client?.connected) return;

        const message = {
            type: 'LEAVE',
            sender: store.userInfo.userInfo.username
        };

        client.publish({
            destination: '/app/chat.leave',
            body: JSON.stringify(message),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    // 发送输入状态
    const sendTypingIndicator = (isTyping) => {
        if (!client?.connected) return;

        const message = {
            type: isTyping ? 'TYPING_START' : 'TYPING_STOP',
            sender: store.userInfo.userInfo.username
        };

        client.publish({
            destination: '/app/chat.typing',
            body: JSON.stringify(message),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    // 发送已读回执
    const sendReadReceipt = (messageId) => {
        if (!client?.connected) return;

        client.publish({
            destination: '/app/chat.receipt',
            body: JSON.stringify({messageId}),
            headers: {Authorization: `Bearer ${store.tokenInfo.token}`}
        });
    };

    // 计算属性
    const publicMessages = computed(() => messages.value);
    const privateMessagesList = computed(() => privateMessages.value);
    const isTyping = computed(() => typingUsers.value.size > 0);
    const typingUsersList = computed(() => Array.from(typingUsers.value));

    return {
        // 连接管理
        connect,
        disconnect,
        isConnected,

        // 消息发送
        sendPublicMessage,
        sendPrivateMessage,
        sendTypingIndicator,
        sendReadReceipt,

        // 数据
        publicMessages,
        privateMessagesList,
        isTyping,
        typingUsersList,

        // 错误处理
        error,

        // 工具方法
        clearMessages: () => {
            messages.value = [];
            privateMessages.value = [];
        }
    };
}
