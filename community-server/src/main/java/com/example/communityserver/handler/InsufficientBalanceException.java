package com.example.communityserver.handler;

/**
 * <p>
 *  自定义异常类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-23
 **/


public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
