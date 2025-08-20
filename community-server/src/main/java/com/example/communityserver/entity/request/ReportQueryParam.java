package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-19
 **/

@Data
public class ReportQueryParam {
    private Integer pageNum;
    private Integer pageSize;
    private Integer reportType;
    private Integer status;
    private Integer contentType;
}
