package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.po.LoginLog;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-07
 **/


public interface ILoginLogService extends IService<LoginLog> {

    String addLoginLog(HttpServletRequest request, Long userId);
}
