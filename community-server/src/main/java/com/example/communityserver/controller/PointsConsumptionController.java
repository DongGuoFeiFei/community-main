package com.example.communityserver.controller;

import com.example.communityserver.entity.model.PointsLevel;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IPointsConsumptionService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/

@Slf4j
@RestController
@Api(tags = "积分交易")
@RequestMapping("pointsConsumption")
public class PointsConsumptionController {
    @Autowired
    private IPointsConsumptionService pointsConsumptionService;

//    /**
// * 获取用户积分交易记录
// * @param {number} userId - 用户ID
// * @param {Object} params - 查询参数
// * @returns {Promise}
// */
//export const getPointsTransactions = (userId, params = {}) => {
//  return request.get(`/pointsConsumption/${userId}`, {params});
//};

//    @GetMapping("{userId}")
//    @ApiOperation("获取用户积分交易记录")
//    @RequiresPermission(api = {"pointsConsumption:{id}:get"})
//    public Result<Void> getPointsTransactions() {
//        List<PointsLevel> levels = pointsConsumptionService.getPointsTransactions();
//        return null;
//    }
}
