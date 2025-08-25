package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-25
 **/

@Data
public class PaymentRequest {
    private String outTradeNo;
    private String totalAmount;
    private String subject;
    private String body;
}
