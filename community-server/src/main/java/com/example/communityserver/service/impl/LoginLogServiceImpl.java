package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.po.LoginLog;
import com.example.communityserver.mapper.LoginLogMapper;
import com.example.communityserver.service.ILoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-05-07
 **/

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {
    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public String addLoginLog(HttpServletRequest request, Long userId) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 处理多级代理的情况（取第一个 IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        // todo 接入第三方库，获取当地地址
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(ip);
        loginLog.setUserId(userId);
        return loginLogMapper.insert(loginLog) > 0 ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : "";
    }
}
