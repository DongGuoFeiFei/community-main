package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.request.GetUserFavoListParam;
import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.response.UserFavoListVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/


public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
    Page<UserFavoListVo> selectFavoList(Page<UserFavoListVo> page, @Param("param") GetUserFavoListParam param,@Param("loginUserId") Long loginUserId);
}
