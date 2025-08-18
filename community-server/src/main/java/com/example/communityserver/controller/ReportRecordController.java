package com.example.communityserver.controller;

import com.example.communityserver.entity.enums.ResponseCodeEnum;
import com.example.communityserver.entity.request.AddRoleParam;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IReportRecordService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //export const submitReport = (data) => {
//    return request.post('/report/submit', data).then(res => {
//        if (res.code !== 200) {
//            throw new Error(res.msg || '提交举报失败');
//        }
//        return res.data;
//    });
//};
    @ApiOperation("提交举报")
    @PostMapping()
    @RequiresPermission(api = {"report:post"})
    public Result<Void> submitReport(@RequestBody AddRoleParam param) {
        Integer is = reportRecordService.submitReport(param);
        return is > 0 ? Result.success() : Result.error(ResponseCodeEnum.FAILED.getCode(), ResponseCodeEnum.FAILED.getValue());
    }

}
