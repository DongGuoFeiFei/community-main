package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.ReportRecord;
import com.example.communityserver.entity.request.AddReportParam;
import com.example.communityserver.entity.request.ReportQueryParam;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/


public interface IReportRecordService extends IService<ReportRecord> {

    Integer submitReport(AddReportParam param);

    IPage<ReportRecord> getReportList(ReportQueryParam param);

    ReportRecord getReportDetail(Long id);

    boolean ignoreReport(Long id);
}
