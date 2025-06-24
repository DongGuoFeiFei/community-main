package com.example.communityserver.entity.request;

import lombok.Data;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/

@Data
public class GetValidateCodeDto {
    private String email;
    private String type;
    private String username;
    private String nickname;
}
