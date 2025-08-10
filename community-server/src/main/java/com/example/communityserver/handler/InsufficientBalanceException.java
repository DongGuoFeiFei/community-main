package com.example.communityserver.handler;

/**
 * <p>
 *  自定义异常类
 * <p>
 *  该类继承自RuntimeException，用于处理账户余额不足的情况

 *  当用户尝试进行超出其账户余额的操作时，可以抛出此异常
 *
 * @author: DongGuo    // 作者名
 * @create: 2025-07-23 // 创建日期
 **/


public class InsufficientBalanceException extends RuntimeException { // 定义一个继承自RuntimeException的自定义异常类
    public InsufficientBalanceException(String message) { // 构造方法，接收一个字符串类型的消息参数
        super(message); // 调用父类RuntimeException的构造方法，传入消息参数
    }
}
