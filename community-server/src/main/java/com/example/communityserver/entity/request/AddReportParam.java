package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/

@Data
public class AddReportParam {
    private Long contentId;
    private Integer contentType; // a
    private String reason;
    private Integer type;  // r
}
