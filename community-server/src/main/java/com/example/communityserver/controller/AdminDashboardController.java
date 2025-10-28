package com.example.communityserver.controller;

import com.example.communityserver.entity.response.DashboardDataVo;
import com.example.communityserver.core.security.aop.RequiresPermission;
import com.example.communityserver.service.IDashboardService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/
@Slf4j
@Api(tags = "管理员控制台数据")
@RestController
@RequestMapping("dashboard")
public class AdminDashboardController {
    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("data")
    @ApiOperation("获取控制台数据")
    @RequiresPermission(api = {"dashboard:data:get"}, role = {"super_admin"})
    public Result<DashboardDataVo> getDashboardData() {
        DashboardDataVo vo = dashboardService.getDashboardData();
        return vo != null ? Result.success(vo) : Result.error();
    }
}
