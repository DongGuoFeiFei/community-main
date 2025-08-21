package com.example.communityserver.controller;

import com.example.communityserver.entity.model.PointsLevel;
import com.example.communityserver.entity.model.UserPointsAccount;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IPointsLevelService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Api(tags = "积分等级")
@RequestMapping("pointsLevel")
public class PointsLevelController {
    @Autowired
    private IPointsLevelService pointsLevelService;

//    /**
// * 获取积分等级列表
// * @returns {Promise}
// */
//export const getPointsLevels = () => {
//  return request.get('/pointsLevel');
//};

    @GetMapping()
    @ApiOperation("获取积分等级列表")
    @RequiresPermission(api = {"pointsAccount:get"})
    public Result<List<PointsLevel>> getPointsLevels() {
        List<PointsLevel> levels = pointsLevelService.getPointsLevels();
        return levels != null ? Result.success(levels) : Result.error();
    }
}
