package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.SysAnnouncement;
import com.example.communityserver.entity.request.GetAnnouncementsParam;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/


public interface IAnnouncementService extends IService<SysAnnouncement> {
    List<SysAnnouncement> getActiveAnnouncements();
    void saveAnnouncement(SysAnnouncement announcement);
    void updateAnnouncement(SysAnnouncement announcement);
    void removeAnnouncement(Long id);

    SysAnnouncement getLatestAnnouncement();

    IPage<SysAnnouncement> GetAnnouncementsList(GetAnnouncementsParam param);
}