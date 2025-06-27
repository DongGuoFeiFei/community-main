package com.example.communityserver.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-27
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdsListParam {
    @NotNull
    private List<Long> ids;
}
