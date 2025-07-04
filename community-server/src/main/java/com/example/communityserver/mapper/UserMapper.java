package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.response.AuthorInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<String> queryPermissionByUserId(Long userId);

    AuthorInfoVo getAuthorInfoVo(@Param("articleId") Long articleId, @Param("loginUserId") Long loginUserId);
}
