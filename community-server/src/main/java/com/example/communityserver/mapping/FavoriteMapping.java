package com.example.communityserver.mapping;

import com.example.communityserver.entity.po.UserFavorite;
import com.example.communityserver.entity.vo.MoveFavoriteVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
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
    void updateMoveVoFromUserFavorite(UserFavorite source, @MappingTarget MoveFavoriteVo target);

}