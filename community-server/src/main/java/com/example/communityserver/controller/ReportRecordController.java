package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.model.ReportRecord;
import com.example.communityserver.entity.request.AddReportParam;
import com.example.communityserver.entity.request.ReportQueryParam;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IReportRecordService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/

@Slf4j
@Api(tags = "举报管理")
@RestController
@RequestMapping("report")
public class ReportRecordController {
    @Autowired
    private IReportRecordService reportRecordService;

    @ApiOperation("提交举报")
    @PostMapping("submit")
    @RequiresPermission(api = {"report:submit:post"})
    public Result<Void> submitReport(@RequestBody AddReportParam param) {
        Integer is = reportRecordService.submitReport(param);
        return is > 0 ? Result.success() : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }

    @ApiOperation("获取举报列表")
    @GetMapping()
    @RequiresPermission(api = {"report:get"})
    public Result<Result.PageData<ReportRecord>> getReportList(ReportQueryParam param) {
        IPage<ReportRecord> reportList = reportRecordService.getReportList(param);
        reportRecordService.getReportList(param);
        return reportList != null ? Result.pageSuccess(reportList.getTotal(), reportList.getRecords()) : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }

    @GetMapping("/{id}")
    @ApiOperation("获取举报详情")
    @RequiresPermission(api = {"report:id:get"})
    public Result<ReportRecord> getReportDetail(@PathVariable Long id) {
        ReportRecord reportRecord = reportRecordService.getReportDetail(id);
        return Result.success(reportRecord);
    }

    //    @PostMapping("/process")
//    @ApiOperation("处理举报")
//    @RequiresPermission(api = {"report:process:post"})
//    public Result<Void> processReport(@RequestBody ProcessReportDTO processReportDTO) {
//        reportRecordService.processReport(processReportDTO);
//        return R.ok();
//    }
// TODO: 2025/8/19 补全举报内容，以及封禁机制 
    @PostMapping("/{id}/ignore")
    @ApiOperation("忽略举报")
    @RequiresPermission(api = {"report:ignore:post"})
    public Result<Void> ignoreReport(@PathVariable Long id) {
        return reportRecordService.ignoreReport(id) ? Result.success() : Result.error();
    }


}
