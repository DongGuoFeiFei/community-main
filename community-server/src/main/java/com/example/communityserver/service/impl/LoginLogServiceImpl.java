package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.LoginLog;
import com.example.communityserver.mapper.LoginLogMapper;
import com.example.communityserver.service.ILoginLogService;
import com.example.communityserver.utils.security.HttpRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
        String ip = HttpRequestUtils.getClientIp(request);
        Map<String, String> deviceInfo = HttpRequestUtils.getDeviceInfo(request);
        // todo 接入第三方库，获取当地地址
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(ip);
        loginLog.setUserId(userId);
        loginLog.setDeviceInfo(deviceInfo.toString());
        return loginLogMapper.insert(loginLog) > 0 ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : "";
    }
}
