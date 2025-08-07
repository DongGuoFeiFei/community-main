package com.example.communityserver.handler;

/**
 * <p>
 * todo
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
