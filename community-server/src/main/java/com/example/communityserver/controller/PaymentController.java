package com.example.communityserver.controller;

import com.example.communityserver.entity.request.PaymentRequest;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.impl.AlipayService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 支付宝接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-25
 **/

@Api(tags = "支付宝接口")
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private AlipayService alipayService;

//    /**
//     * 创建支付宝支付订单,订单数据由后台生成，并作为未支付账单
//     */
//    @PostMapping("/alipay/create/order")
//    @ApiOperation(value = "创建支付宝支付订单")
//    @RequiresPermission(api = {"api:payment:alipay:create:order:post"}, role = {"super_admin"})
//    public Result<String> createAlipayPaymentOrder() {
//        String payment = alipayService.createAlipayPaymentOrder();
//        return Result.success(payment);
//    }

    /**
     * 创建支付宝支付
     */
    @PostMapping("/alipay/create")
    @ApiOperation(value = "创建支付宝支付")
    @RequiresPermission(api = {"api:payment:alipay:create:post"}, role = {"super_admin"})
    public Result<String> createAlipayPayment(@RequestBody PaymentRequest request) {
        String payment = alipayService.createPayment(request.getOutTradeNo(), request.getTotalAmount(), request.getSubject(), request.getBody());
        return Result.success(payment);
    }

    /**
     * 支付宝异步通知处理
     */
    @PostMapping("/alipay/notify")
    @ApiOperation(value = "支付宝异步通知处理")
    @RequiresPermission(api = {"api:payment:alipay:notify:post"}, role = {"super_admin"})
    public Result<String> handleAlipayNotify(@RequestBody String notifyData) {
        // 这里处理支付宝的异步通知，验证签名并更新订单状态
        // 返回success表示处理成功
        // todo 支付成功，进行积分分配，商品分发
        return Result.success("success");
    }

    /**
     * 支付宝同步返回处理
     */
    @GetMapping("/alipay/return")
    @ApiOperation(value = "支付宝同步返回处理")
    @RequiresPermission(api = {"api:payment:alipay:return:get"}, role = {"super_admin"})
    public Result<String> handleAlipayReturn() {
        // 处理支付完成后的页面跳转
        // 显示支付成功
        return Result.success("支付成功");
    }
}

