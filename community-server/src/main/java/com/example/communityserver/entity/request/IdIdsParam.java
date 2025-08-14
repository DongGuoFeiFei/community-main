package com.example.communityserver.entity.request;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-14
 **/

@Data
public class IdIdsParam {
    private Long id;
    private List<Long> ids;
}
