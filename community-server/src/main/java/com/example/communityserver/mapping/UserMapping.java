package com.example.communityserver.mapping;

import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.ModifyUserParam;
import com.example.communityserver.entity.response.ArticleCardVo;
import com.example.communityserver.entity.response.ArticleDtlVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 映射
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapping {
    UserMapping INSTANCE = Mappers.getMapper(UserMapping.class);
    User toUser(ModifyUserParam param);
}
