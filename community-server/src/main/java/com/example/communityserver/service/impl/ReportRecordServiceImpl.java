package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.model.ReportRecord;
import com.example.communityserver.mapper.NotificationMapper;
import com.example.communityserver.mapper.ReportRecordMapper;
import com.example.communityserver.service.INotificationService;
import com.example.communityserver.service.IReportRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/


@Slf4j
@Service
public class ReportRecordServiceImpl extends ServiceImpl<ReportRecordMapper, ReportRecord> implements IReportRecordService {
    @Autowired
    private ReportRecordMapper reportRecordMapper;

}
