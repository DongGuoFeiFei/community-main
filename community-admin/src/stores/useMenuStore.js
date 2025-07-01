import {defineStore} from 'pinia';
import {markRaw, ref} from 'vue';
import {
    Bell,
    ChatDotRound,
    Collection,
    Connection,
    Document,
    Flag,
    Grid,
    Key,
    List,
    Lock,
    Message,
    Monitor,
    Notebook,
    Setting,
    TrendCharts,
    User,
    Warning
} from "@element-plus/icons-vue";

export const useMenuStore = defineStore('menu', () => {
    // 图标映射表 - 使用markRaw避免响应式转换
    const iconComponents = {
        monitor: markRaw(Monitor),
        grid: markRaw(Grid),
        document: markRaw(Document),
        collection: markRaw(Collection),
        chat: markRaw(ChatDotRound),
        connection: markRaw(Connection),
        trend: markRaw(TrendCharts),
        user: markRaw(User),
        warning: markRaw(Warning),
        lock: markRaw(Lock),
        key: markRaw(Key),
        list: markRaw(List),
        log: markRaw(Notebook),
        flag: markRaw(Flag),
        bell: markRaw(Bell),
        message: markRaw(Message),
        setting: markRaw(Setting)
    };

    // 完整的静态菜单数据结构
    const staticMenuData = [
        {
            path: '/admin/dashboard',
            meta: {
                title: '控制台',
                icon: 'monitor'
            }
        },
        {
            path: '/admin/content',
            meta: {
                title: '内容管理',
                icon: 'grid'
            },
            children: [
                {
                    path: '/admin/articles',
                    meta: {
                        title: '文章管理',
                        icon: 'document'
                    }
                },
                {
                    path: '/admin/tags',
                    meta: {
                        title: '标签管理',
                        icon: 'collection'
                    }
                },
                {
                    path: '/admin/comments',
                    meta: {
                        title: '评论管理',
                        icon: 'chat'
                    }
                }
            ]
        },
        {
            path: '/admin/community',
            meta: {
                title: '社区管理',
                icon: 'connection'
            },
            children: [
                {
                    path: '/admin/topics',
                    meta: {
                        title: '话题管理',
                        icon: 'collection'
                    }
                },
                {
                    path: '/admin/activities',
                    meta: {
                        title: '活动管理',
                        icon: 'trend'
                    }
                }
            ]
        },
        {
            path: '/admin/user',
            meta: {
                title: '用户管理',
                icon: 'user'
            },
            children: [
                {
                    path: '/admin/userList',
                    meta: {
                        title: '用户列表',
                        icon: 'user'
                    }
                },
                {
                    path: '/admin/userAudit',
                    meta: {
                        title: '用户审核',
                        icon: 'warning'
                    }
                }
            ]
        },
        {
            path: '/admin/permission',
            meta: {
                title: '权限管理',
                icon: 'lock'
            },
            children: [
                {
                    path: '/admin/permissionRole',
                    meta: {
                        title: '角色管理',
                        icon: 'key'
                    }
                },
                {
                    path: '/admin/permissionUser',
                    meta: {
                        title: '用户权限',
                        icon: 'user'
                    }
                },
                {
                    path: '/admin/permissionMenu',
                    meta: {
                        title: '菜单权限',
                        icon: 'list'
                    }
                },
                {
                    path: '/admin/permissionLog',
                    meta: {
                        title: '权限操作日志',
                        icon: 'log'
                    }
                },
                {
                    path: '/admin/permissionSetting',
                    meta: {
                        title: '权限设置',
                        icon: 'setting'
                    }
                }
            ]
        },
        {
            path: '/admin/moderation',
            meta: {
                title: '内容审核',
                icon: 'flag'
            },
            children: [
                {
                    path: '/admin/moderationReports',
                    meta: {
                        title: '举报处理',
                        icon: 'flag'
                    }
                },
                {
                    path: '/admin/moderationSensitive',
                    meta: {
                        title: '敏感词过滤',
                        icon: 'warning'
                    }
                }
            ]
        },
        {
            path: '/admin/system',
            meta: {
                title: '系统设置',
                icon: 'setting'
            },
            children: [
                {
                    path: '/admin/systemBasic',
                    meta: {
                        title: '基础配置',
                        icon: 'setting'
                    }
                },
                {
                    path: '/admin/systemLogs',
                    meta: {
                        title: '系统操作日志',
                        icon: 'document'
                    }
                }
            ]
        },
        {
            path: '/admin/notification',
            meta: {
                title: '消息通知',
                icon: 'bell'
            },
            children: [
                {
                    path: '/admin/notificationSystem',
                    meta: {
                        title: '系统通知',
                        icon: 'message'
                    }
                },
                {
                    path: '/admin/notificationUser',
                    meta: {
                        title: '用户消息',
                        icon: 'user'
                    }
                },
                {
                    path: '/admin/notificationTemplate',
                    meta: {
                        title: '消息模板',
                        icon: 'document'
                    }
                },
                {
                    path: '/admin/notificationSettings',
                    meta: {
                        title: '通知设置',
                        icon: 'setting'
                    }
                }
            ]
        }
    ];

    // 转换菜单数据格式，添加iconComponent
    const transformMenuData = (menuItems) => {
        return menuItems.map(item => ({
            ...item,
            meta: {
                ...item.meta,
                iconComponent: item.meta.icon ? iconComponents[item.meta.icon] : null
            },
            children: item.children ? transformMenuData(item.children) : []
        }));
    };

    // 原始菜单数据（ref包装以便响应式更新）
    const rawMenuData = ref(transformMenuData(staticMenuData));

    return {
        rawMenuData
    };
}, {
    persist: {
        key: 'useMenu',
        storage: sessionStorage,
    }
});

// todo 数据加密，后台动态获取，安全性讨论，或者放在menuUtil.js工具中