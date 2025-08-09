package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.UserSearchParam;
import com.example.communityserver.entity.response.AuthorInfoVo;
import com.example.communityserver.entity.response.UserDelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/


public interface UserMapper extends BaseMapper<User> {

    List<String> queryPermissionByUserId(Long userId);

    AuthorInfoVo getAuthorInfoVo(@Param("articleId") Long articleId, @Param("loginUserId") Long loginUserId);

    IPage<UserDelVo> getUsers(Page<UserDelVo> page, @Param("param") UserSearchParam param);

    UserDelVo getUserProfile(Long userId);
}
