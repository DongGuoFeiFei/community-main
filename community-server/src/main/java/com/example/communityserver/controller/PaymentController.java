package com.example.communityserver.controller;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-25
 **/


import com.example.communityserver.entity.request.PaymentRequest;
import com.example.communityserver.service.impl.AlipayService;
import com.example.communityserver.utils.web.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private AlipayService alipayService;

    /**
     * 创建支付宝支付
     */
    @PostMapping("/alipay/create")
    public Result<String> createAlipayPayment(@RequestBody PaymentRequest request) {
        return Result.success(alipayService.createPayment(request.getOutTradeNo(), request.getTotalAmount(), request.getSubject(), request.getBody()));
    }

    /**
     * 支付宝异步通知处理
     */
    @PostMapping("/alipay/notify")
    public Result<String> handleAlipayNotify(@RequestBody String notifyData) {
        // 这里处理支付宝的异步通知，验证签名并更新订单状态
        // 返回success表示处理成功
        return Result.success("success");
    }

    /**
     * 支付宝同步返回处理
     */
    @GetMapping("/alipay/return")
    public Result<String> handleAlipayReturn() {
        // 处理支付完成后的页面跳转
        return Result.success("支付成功");
    }
}

