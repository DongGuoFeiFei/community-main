package com.example.communityserver.core.config;

import com.example.communityserver.entity.constants.SystemConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 支付宝配置
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-25
 **/

@Data
@Configuration
// 自动绑定application.yml或 application.properties）中前缀为alipay的属性，但是配置文件中需要是短横线替代小驼峰 需要给出get|set方法
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
    // 支付宝应用ID
    private String appId;
    // 商户私钥
    private String privateKey;
    // 支付宝公钥
    private String publicKey;
    // 支付宝网关
    private String gatewayUrl;
    // 签名方式
    private String signType = "RSA2";
    // 字符编码
    private String charset = "UTF-8";
    // 返回格式
    private String format = "json";
    // 异步通知地址
    private String notifyUrl = "http://ze4c9f28.natappfree.cc/api/payment/alipay/notify";
    // 同步跳转地址
    private String returnUrl = SystemConstants.BASIC_URL + "/api/payment/alipay/return";
}
