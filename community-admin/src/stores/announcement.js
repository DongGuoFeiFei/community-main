import {defineStore} from 'pinia';
import {ref} from 'vue';
import announcementApi from '@/api/announcement';

export const useAnnouncementStore = defineStore('announcement', () => {
    const announcementList = ref([]);
    const currentAnnouncement = ref(null);
    const loading = ref(false);
    const pagination = ref({
        page: 1,
        pageSize: 10,
        total: 0
    });
    const filterParams = ref({
        title: '',
        publisher: '',
        status: null,
        startTime: null,
        endTime: null
    });

    // 获取公告列表
    const fetchAnnouncementList = async () => {
        loading.value = true;
        try {
            const response = await announcementApi.getAnnouncementList({
                ...pagination.value,
                ...filterParams.value
            });
            announcementList.value = response.data.rows;
            pagination.value.total = response.data.total;
        } finally {
            loading.value = false;
        }
    };

    // 获取公告详情
    const fetchAnnouncementDetail = async (id) => {
        loading.value = true;
        try {
            const response = await announcementApi.getAnnouncementDetail(id);
            currentAnnouncement.value = response.data;
        } finally {
            loading.value = false;
        }
    };

    // 新增公告
    const addAnnouncement = async (data) => {
        loading.value = true;
        try {
            await announcementApi.addAnnouncement(data);
            await fetchAnnouncementList();
        } finally {
            loading.value = false;
        }
    };

    // 更新公告
    const updateAnnouncement = async (data) => {
        loading.value = true;
        try {
            await announcementApi.updateAnnouncement(data);
            await fetchAnnouncementList();
        } finally {
            loading.value = false;
        }
    };

    // 删除公告
    const deleteAnnouncement = async (id) => {
        loading.value = true;
        try {
            await announcementApi.deleteAnnouncement(id);
            await fetchAnnouncementList();
        } finally {
            loading.value = false;
        }
    };

    // 发布公告
    const publishAnnouncement = async (id) => {
        loading.value = true;
        try {
            await announcementApi.publishAnnouncement(id);
            await fetchAnnouncementList();
        } finally {
            loading.value = false;
        }
    };

    // 下线公告
    const offlineAnnouncement = async (id) => {
        loading.value = true;
        try {
            await announcementApi.offlineAnnouncement(id);
            await fetchAnnouncementList();
        } finally {
            loading.value = false;
        }
    };

    return {
        announcementList,
        currentAnnouncement,
        loading,
        pagination,
        filterParams,
        fetchAnnouncementList,
        fetchAnnouncementDetail,
        addAnnouncement,
        updateAnnouncement,
        deleteAnnouncement,
        publishAnnouncement,
        offlineAnnouncement
    };
});