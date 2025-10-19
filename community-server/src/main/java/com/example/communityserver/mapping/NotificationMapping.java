package com.example.communityserver.mapping;

import com.example.communityserver.entity.model.Notification;
import com.example.communityserver.entity.response.NotificationListVo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 映射
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-26
 **/

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapping {
    NotificationMapping INSTANCE = Mappers.getMapper(NotificationMapping.class);

    @Mapping(target = "isRead", source = "isRead", qualifiedByName = "intToBoolean")
    void toNotificationListVo(Notification entity, @MappingTarget NotificationListVo vo);

    @Named("intToBoolean")
    default Boolean intToBoolean(Integer value) {
        return value != null && value == 1;
    }
}
