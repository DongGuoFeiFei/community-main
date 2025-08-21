package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.PointsLevel;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/


public interface IPointsLevelService extends IService<PointsLevel> {
    List<PointsLevel> getPointsLevels();
}
