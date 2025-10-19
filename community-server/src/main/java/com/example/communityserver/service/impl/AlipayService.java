package com.example.communityserver.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.communityserver.security.config.AlipayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付宝支付服务实现类
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-25
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayService {

    private final AlipayConfig alipayConfig;

    /**
     * 创建支付宝支付页面
     */
    public String createPayment(String outTradeNo, String totalAmount, String subject, String body) {
        try {
            // 初始化支付宝客户端
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayConfig.getGatewayUrl(),
                    alipayConfig.getAppId(),
                    alipayConfig.getPrivateKey(),
                    alipayConfig.getFormat(),
                    alipayConfig.getCharset(),
                    alipayConfig.getPublicKey(),
                    alipayConfig.getSignType());

            // 创建请求模型
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(totalAmount);
            model.setSubject(subject);
            model.setBody(body);
            model.setProductCode("FAST_INSTANT_TRADE_PAY");

            // 创建请求
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setBizModel(model);
            request.setNotifyUrl(alipayConfig.getNotifyUrl());
            request.setReturnUrl(alipayConfig.getReturnUrl());

            // 执行请求
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                return response.getBody();
            } else {
                log.error("支付宝支付创建失败: {}", response.getSubMsg());
                throw new RuntimeException("支付创建失败");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝支付接口调用异常", e);
            throw new RuntimeException("支付接口调用失败");
        }
    }
}
