package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.SysAnnouncement;
import com.example.communityserver.entity.request.GetAnnouncementsParam;
import com.example.communityserver.mapper.AnnouncementMapper;
import com.example.communityserver.service.IAnnouncementService;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, SysAnnouncement> implements IAnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public List<SysAnnouncement> getActiveAnnouncements() {
        LambdaQueryWrapper<SysAnnouncement> query = new LambdaQueryWrapper<>();
        query.eq(SysAnnouncement::getStatus, 1)
                .eq(SysAnnouncement::getDeleted, 0)
                .le(SysAnnouncement::getStartTime, LocalDateTime.now())
                .ge(SysAnnouncement::getEndTime, LocalDateTime.now())
                .orderByDesc(SysAnnouncement::getPriority)
                .orderByDesc(SysAnnouncement::getPublishTime);
        return announcementMapper.selectList(query);
    }

    @Override
    public void saveAnnouncement(SysAnnouncement announcement) {
        announcement.setCreateBy(SecurityUtils.getLoginUserId());
        announcement.setPublishTime(LocalDateTime.now());
        save(announcement);
    }

    @Override
    public void updateAnnouncement(SysAnnouncement announcement) {
        announcement.setUpdateBy(SecurityUtils.getLoginUserId());
        updateById(announcement);
    }

    @Override
    public void removeAnnouncement(Long id) {
        removeById(id);
    }

    @Override
    public SysAnnouncement getLatestAnnouncement() {

        LambdaQueryWrapper<SysAnnouncement> query = new LambdaQueryWrapper<>();
        query.eq(SysAnnouncement::getStatus, 1)
                .eq(SysAnnouncement::getDeleted, 0)
                .le(SysAnnouncement::getStartTime, LocalDateTime.now())
                .ge(SysAnnouncement::getEndTime, LocalDateTime.now())
                .orderByDesc(SysAnnouncement::getPriority)
                .orderByDesc(SysAnnouncement::getPublishTime);
        List<SysAnnouncement> announcementList = announcementMapper.selectList(query);
        return announcementList.isEmpty() ? null : announcementList.get(0);
    }

    @Override
    public IPage<SysAnnouncement> GetAnnouncementsList(GetAnnouncementsParam param) {
        Page<SysAnnouncement> page = new Page<>(param.getPage(), param.getPageSize());
        LambdaQueryWrapper<SysAnnouncement> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SysAnnouncement::getDeleted, 0)
                .like(StringUtils.isNotBlank(param.getTitle()), SysAnnouncement::getTitle, param.getTitle())
                .eq(StringUtils.isNotBlank(param.getPublisher()), SysAnnouncement::getPublisher, param.getPublisher())
                .eq(param.getStatus() != null, SysAnnouncement::getStatus, param.getStatus())
                .ge(StringUtils.isNotBlank(param.getStartTime()), SysAnnouncement::getPublishTime, param.getStartTime())
                .le(StringUtils.isNotBlank(param.getEndTime()), SysAnnouncement::getPublishTime, param.getEndTime())
                .orderByDesc(SysAnnouncement::getPriority)
                .orderByDesc(SysAnnouncement::getPublishTime)
                .orderByDesc(SysAnnouncement::getStatus);

        return announcementMapper.selectPage(page, queryWrapper);
    }
}