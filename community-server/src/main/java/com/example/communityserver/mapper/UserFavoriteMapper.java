package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.dto.GetUserFavoListParam;
import com.example.communityserver.entity.po.UserFavorite;
import com.example.communityserver.entity.vo.UserFavoListVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/


public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
    Page<UserFavoListVo> selectFavoList(Page<UserFavoListVo> page, @Param("param") GetUserFavoListParam param,@Param("loginUserId") Long loginUserId);
}
