package com.example.communityserver.adminController;

import com.example.communityserver.entity.model.SysAnnouncement;
import com.example.communityserver.service.IAnnouncementService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/

@Api(tags = "系统公告管理")
@RestController
@RequestMapping("/system/announcement")
public class AnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;

    @GetMapping("/list")
    @ApiOperation("获取公告列表")
    public Result<List<SysAnnouncement>> list() {
        List<SysAnnouncement> list = announcementService.getActiveAnnouncements();
        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("新增公告")
    public Result<Void> add(@Validated @RequestBody SysAnnouncement announcement) {
        announcementService.saveAnnouncement(announcement);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改公告")
    public Result<Void> edit(@Validated @RequestBody SysAnnouncement announcement) {
        announcementService.updateAnnouncement(announcement);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除公告")
    public Result<Void> remove(@PathVariable Long id) {
        announcementService.removeAnnouncement(id);
        return Result.success();
    }

    @ApiOperation("获取激活的公告")
    @GetMapping("/active")
    public Result<List<SysAnnouncement>> getActiveAnnouncements() {
        List<SysAnnouncement> announcements = announcementService.getActiveAnnouncements();
        return Result.success(announcements);
    }

    @ApiOperation("获取最新的公告")
    @GetMapping("/latest")
    public Result<SysAnnouncement> getLatestAnnouncement() {
        SysAnnouncement announcement = announcementService.getLatestAnnouncement();
        return Result.success(announcement);
    }
}