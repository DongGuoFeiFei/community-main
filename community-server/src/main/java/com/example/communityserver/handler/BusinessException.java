package com.example.communityserver.handler;

/**
 * <p>
 * 业务异常
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-17
 **/


public class BusinessException extends RuntimeException {
    public BusinessException(String message) { // 构造方法，接收一个字符串类型的消息参数
        super(message);
    }
}
