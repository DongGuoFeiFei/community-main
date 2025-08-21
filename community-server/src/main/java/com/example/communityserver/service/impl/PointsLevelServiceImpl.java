package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.PointsLevel;
import com.example.communityserver.mapper.PointsLevelMapper;
import com.example.communityserver.service.IPointsLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/
@Service
public class PointsLevelServiceImpl extends ServiceImpl<PointsLevelMapper, PointsLevel> implements IPointsLevelService {
    @Autowired
    private PointsLevelMapper pointsLevelMapper;

    @Override
    public List<PointsLevel> getPointsLevels() {
        return pointsLevelMapper.selectList(null);
    }
}
