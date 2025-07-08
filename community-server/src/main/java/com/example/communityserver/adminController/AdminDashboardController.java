package com.example.communityserver.adminController;

import com.example.communityserver.entity.response.DashboardDataVo;
import com.example.communityserver.service.IDashboardService;
import com.example.communityserver.utils.web.Result;
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

@RestController
@RequestMapping("dashboard")
public class AdminDashboardController {
    @Autowired
    private IDashboardService dashboardService;

    @GetMapping("data")
    public Result<DashboardDataVo> getDashboardData(){
        DashboardDataVo vo = dashboardService.getDashboardData();
        return vo!=null?Result.success(vo):Result.error();

    }
}
