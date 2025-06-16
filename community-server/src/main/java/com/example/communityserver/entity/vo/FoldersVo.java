package com.example.communityserver.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/

@Data
public class FoldersVo {
    private Long id;
    private String name;
    private Long articleCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createAt;
}
