package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-13
 **/

@Data
public class GetAnnouncementsParam {
    private String title;
    private String publisher;
    private Integer status;
    private String startTime;
    private String endTime;
    private Integer page;
    private Integer pageSize;
}
