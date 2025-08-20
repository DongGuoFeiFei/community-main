package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.enums.ActiveTypeEnum;
import com.example.communityserver.entity.enums.ReportTypeEnum;
import com.example.communityserver.entity.enums.StatusTypeEnum;
import com.example.communityserver.entity.model.ReportRecord;
import com.example.communityserver.entity.request.AddReportParam;
import com.example.communityserver.entity.request.ReportQueryParam;
import com.example.communityserver.mapper.ReportRecordMapper;
import com.example.communityserver.security.util.SecurityUtils;
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

    @Override
    public Integer submitReport(AddReportParam param) {
        log.info("submitReport param: {}", param);
        ReportRecord reportRecord = new ReportRecord(
                SecurityUtils.getLoginUserId(),
                param.getContentId(),
                ActiveTypeEnum.fromKey(param.getContentType()),
                ReportTypeEnum.fromKey(param.getType()),
                param.getReason());
        return reportRecordMapper.insert(reportRecord);
    }

    @Override
    public IPage<ReportRecord> getReportList(ReportQueryParam param) {
        Page<ReportRecord> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<ReportRecord> wrapper = new LambdaQueryWrapper<>();

        if (param.getContentType() != null) {
            wrapper.eq(ReportRecord::getContentType, ActiveTypeEnum.fromKey(param.getContentType()));
        }
        if (param.getStatus() != null) {
            wrapper.eq(ReportRecord::getStatus, StatusTypeEnum.fromKey(param.getStatus()));
        }
        if (param.getReportType() != null) {
            wrapper.eq(ReportRecord::getReportType, ReportTypeEnum.fromKey(param.getReportType()));
        }

        return reportRecordMapper.selectPage(page, wrapper);
    }

    @Override
    public ReportRecord getReportDetail(Long id) {
        return reportRecordMapper.selectById(id);
    }

    @Override
    public boolean ignoreReport(Long id) {
        LambdaUpdateWrapper<ReportRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(id != null, ReportRecord::getId, id)
                .set(ReportRecord::getStatus, StatusTypeEnum.IGNORED);
        return reportRecordMapper.update(wrapper) > 0;
    }
}
