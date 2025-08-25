package com.example.communityserver.config;

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
@ConfigurationProperties(prefix = "alipay")
public class AlipayConfig {
    // 应用ID
    private String appId = "9021000151643622";
    // 商户私钥
    private String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCjsiawg15nlXliVxmHoaXIR+Nv0ZQCGWplxKEJQVQe+0kjD85JXQSpNyJ43THSkZwz1X5vaShD+Dn8Q/nqP4w0JnWXUU6TTREvzNV2/QLlcUl/GfnmfsRdHLIOdkAMa6pQ442v9kb9BX0+yEyzt9tOvCXe6DsK4gY0Mq6roGOz8Yw3OSf4P+wrujB/KU/0ShCNT32f+MSU8mziPT7aRvB/oaEGw3SA3gNFALtAeuuRNfe3+M1JMd7lZk/HHFJfKwYbZ/+wLd2PTRg4xqMR5/fTRwCHHi3WtMBy6Sj7zEblADMRxekp9KwpQyiik3tso49zLW+WDgfwen3QCa98ak6ZAgMBAAECggEAey6jqK6iyuYTeuo6XvNIksduIgYfD9laVIy79gAAMnQ8XDMXs+NYj5Ha0gL57BAA8IyW4XXLhCJ2WBw7I4iAk/nhAWaA753N8h98/1Zz6H7wpMHCaS45zCu83d8kYXRfrREf3StBzkNkNWt7NL6FPOGeyfgN/imKK5t8D9DZVl2W1vhwWFtFZSEGb+UnW5fB8LA/iGgH4K/Pbxulac40RkadKQ4hTJKNBYJtMXPtoIsP8AfpSo+2Doy+8hU9uFkGi0q0iMuLYkgxe+51Dj3KOAcbiXpF60djnh80gvr7+D+RhpemLbtCjIWIHbPwGyG645mZkGZi8FqI3/kBnIsDUQKBgQDoxbK2rclXEmNro7zUWNsHkYh45cHGgEee9X2mF9fYEg6jNUmI/3Sw18DRbHyzhHy8pFp4ofmJM072qGjc0y9q3/iDIwGWLrojJWw4MLRt1p/K9Y9MT8m59Zgj11VrJo14T1Wz49nIacS/5/DbG9/SOMkS6EuLU+8X698U4emqVQKBgQC0B9uLvALY4+F7j9n5LTGeYso9qb8Pb3aGsL25rDazc7Xd7qcgLzQrUB2vo35nHk/c/FqGfmy5LumtCs741Qp+lPeL21pSPM5CbLcIo1zjLECJDQkFXgxnRsrHqQ2WRysvNiXey+n6vVwiqohkcsljA3rmbLfIBr9/etVIewLfNQKBgEeTQvjZR2f2x4t5tb618+PH5GcIfyVCnMNMSW6hNkXCh6233Py7UG5waWH8MfwhaRm4j/k4FTsTA9zkvBnq9lGuvQPV587+X04fW7+Lem+r50bJa17TyfvcgQPEWwevuyCbhIbkMTtnCpDYCT/WumQyAeJGe7REAukgJmCNdwRhAoGAHiBeffC1bUQdHIdDC66jagTQISeBwHx/kiSLa0HFWZxYZ84jC4C6szDR9u1eHrV02d8k6vLHEOon0nxHjF5iRjJ5hl7rq21fesAklP55cc6a8hRLWSmW28V985F3o5k/QcRG7BSlj/8WJvrwnhaTWs1fSKYSzxdJ/nQ4ssta/K0CgYA1uh03emJycF8Lv2VCxh29g0Yh3hI504jbvogkKDxaVVU1NjhuE+Np0Wv3+No0+ljkuGDmB6At/UmZtocmrV+LmXQ0wwUfAVN6UkF95wSrpwP9ZwyaMtXs5gtvpj1ifBKE6yUXuPf2Ya3jwGG9kErF4nE0S4msiiqnJ+VzzpRW7g==";
    // 支付宝公钥
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmwxYSvxA/+9vEraVVxIDYr9QLc55I/xovHV9H4DfJ47ARvLQo9Ras+3XvsTPz0wtl5rmChoLD/p3YL/BCwR8czguDOPk2klslJyg0uGRUuh8/8rwOdjYfWZEocFPbnCJyoqHIjabyxe35os9Df7Z4B/Sjat5aJSCIcAVN2DYPiVbZGw3jpca1b17pNVd57xONFZ2iJgCLQc3dyzVc8MP6eI+jO3YtUvaQ+cMKc0e0LtzKmX9swPrh0UKtM2mGXPdN20RY6VGnbXAbSDZIWhdqe4DP8g+Y5d4o/B+TApH+OB20n5pkkSUtbqKEtMGMpHe5i3DfYFQTUPXfW3nQhhl5QIDAQAB";
    // 支付宝网关地址
    private String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // 签名类型
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
