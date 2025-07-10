package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.UserSearchParam;
import com.example.communityserver.entity.response.AuthorInfoVo;
import com.example.communityserver.entity.response.UserListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<String> queryPermissionByUserId(Long userId);

    AuthorInfoVo getAuthorInfoVo(@Param("articleId") Long articleId, @Param("loginUserId") Long loginUserId);

    IPage<UserListVo> getUsers(Page<UserListVo> page, @Param("param") UserSearchParam param);
}
