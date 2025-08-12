package com.example.communityserver.controller;

import com.example.communityserver.service.IRoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-12
 **/

@Api(tags = "角色管理")
@RestController
@RequestMapping("menu")
public class RoleController {

    @Autowired
    private IRoleService roleService;

///**
// * 获取角色列表
// */
//    export const getRoleList = (params) => {
//        return request.get('/role/list', { params });
//    };

}
