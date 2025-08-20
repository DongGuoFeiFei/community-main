package com.example.communityserver.controller;

import com.example.communityserver.service.IPointsGoodsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "通知管理")
@RestController
@RequestMapping("/goods")
public class PointsGoodsController {
    @Autowired
    private IPointsGoodsService pointsGoodsService;

}
