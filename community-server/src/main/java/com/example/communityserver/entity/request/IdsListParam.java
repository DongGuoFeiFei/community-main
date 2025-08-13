package com.example.communityserver.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "ID列表不能为空")
    @Size(min = 1, message = "至少需要提供一个ID")
    private List<Long> ids;
}
