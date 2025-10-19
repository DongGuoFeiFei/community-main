package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.PointsGoods;
import com.example.communityserver.mapper.PointsGoodsMapper;
import com.example.communityserver.service.IPointsGoodsService;
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
public class PointsGoodsServiceImpl extends ServiceImpl<PointsGoodsMapper, PointsGoods> implements IPointsGoodsService {
    @Autowired
    private PointsGoodsMapper pointsGoodsMapper;
}
