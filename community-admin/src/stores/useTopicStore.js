import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getTopics, addTopic, updateTopic, deleteTopic, batchDeleteTopics } from '@/api/topic.js';

export const useTopicStore = defineStore('adminTopic', () => {
    const topics = ref([]);
    const total = ref(0);
    const loading = ref(false);
    const queryParams = ref({
        page: 1,
        limit: 10,
        title: '',
        status: '',
        sort: '-createdAt'
    });

    // 获取话题列表
    const fetchTopics = async () => {
        loading.value = true;
        try {
            const res = await getTopics(queryParams.value);
            topics.value = res.data.list;
            total.value = res.data.total;
        } finally {
            loading.value = false;
        }
    };

    // 新增话题
    const createTopic = async (data) => {
        await addTopic(data);
        await fetchTopics();
    };

    // 更新话题
    const modifyTopic = async (id, data) => {
        await updateTopic(id, data);
        await fetchTopics();
    };

    // 删除话题
    const removeTopic = async (id) => {
        await deleteTopic(id);
        await fetchTopics();
    };

    // 批量删除
    const removeTopics = async (ids) => {
        await batchDeleteTopics(ids);
        await fetchTopics();
    };

    return {
        topics,
        total,
        loading,
        queryParams,
        fetchTopics,
        createTopic,
        modifyTopic,
        removeTopic,
        removeTopics
    };
});