package com.example.communityserver.entity.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-07
 **/

@Data
public class MarkAsReadParam {
    private List<Long> ids;
    private String type;
}
