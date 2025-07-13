package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.enums.MessageCodeEnum;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.RegisterDto;
import com.example.communityserver.entity.request.UserSearchParam;
import com.example.communityserver.entity.response.AuthorInfoVo;
import com.example.communityserver.entity.response.UserCountStats;
import com.example.communityserver.entity.response.UserListVo;


public interface IUserService extends IService<User> {
    String login(String username, String password);


    MessageCodeEnum isExistUser(String email, String username,String phone);

    MessageCodeEnum register(RegisterDto dto);

    AuthorInfoVo getAuthorInfoVo(Long articleId);

    UserCountStats getUserStats(Long userId);

    IPage<UserListVo> getUsers(UserSearchParam param);
}
