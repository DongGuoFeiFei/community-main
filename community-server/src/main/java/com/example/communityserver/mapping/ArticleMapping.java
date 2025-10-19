package com.example.communityserver.mapping;

import com.example.communityserver.entity.model.Article;
import com.example.communityserver.entity.response.ArticleCardVo;
import com.example.communityserver.entity.response.ArticleDtlVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p>
 * 文章映射
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ArticleMapping {
    ArticleMapping INSTANCE = Mappers.getMapper(ArticleMapping.class);

    void updateArticle(ArticleDtlVo source, @MappingTarget ArticleDtlVo target);
    List<ArticleCardVo> toCardVo(List<Article> source);
}
