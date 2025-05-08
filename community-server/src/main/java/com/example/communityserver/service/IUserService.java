package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.po.User;


public interface IUserService extends IService<User> {
    String login(String username, String password);
}
