package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.po.User;


import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<String> queryPermissionByUserId(Long userId);

}
