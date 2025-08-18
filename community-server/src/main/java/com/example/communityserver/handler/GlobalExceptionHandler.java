package com.example.communityserver.handler;

import com.example.communityserver.entity.response.ErrorResponse;
import com.example.communityserver.utils.web.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局异常处理器
 * <p>
 * 该类用于处理全局异常，捕获系统中抛出的特定异常并返回相应的错误响应
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-23
 **/
// TODO: 2025/8/17 分析全局处理器和权限认证处理器的作用域 
// 使用@RestControllerAdvice注解声明这是一个全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException e) {
        // 创建错误响应对象，包含错误代码、错误信息和HTTP状态码
        ErrorResponse errorResponse = new ErrorResponse(
                "INSUFFICIENT_BALANCE",
                e.getMessage(),
                HttpStatus.PAYMENT_REQUIRED.value()
        );
        // 返回带有指定HTTP状态码的错误响应体
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(errorResponse);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<String> handleBusinessException(BusinessException e) {
        return Result.error(e.getMessage());
    }
}

