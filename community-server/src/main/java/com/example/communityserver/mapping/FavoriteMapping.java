package com.example.communityserver.mapping;

import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.response.MoveFavoriteVo;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 映射
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-23
 **/


@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FavoriteMapping {
    FavoriteMapping INSTANCE = Mappers.getMapper(FavoriteMapping.class);

    @Mapping(source = "favoriteId", target = "id")
    @Mapping(source = "targetId", target = "articleId")
    void updateMoveVoFromUserFavorite(UserFavorite source, @MappingTarget MoveFavoriteVo target);

}
