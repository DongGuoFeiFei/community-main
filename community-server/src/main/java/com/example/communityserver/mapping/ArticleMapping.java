package com.example.communityserver.mapping;

import com.example.communityserver.entity.vo.ArticleDtlVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ArticleMapping {
    ArticleMapping INSTANCE = Mappers.getMapper(ArticleMapping.class);

    void updateArticle(ArticleDtlVo source, @MappingTarget ArticleDtlVo target);
}
