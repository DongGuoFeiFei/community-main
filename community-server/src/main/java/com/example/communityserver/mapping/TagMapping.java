package com.example.communityserver.mapping;

import com.example.communityserver.entity.model.Tag;
import com.example.communityserver.entity.response.TagVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
public interface TagMapping {
    TagMapping INSTANCE = Mappers.getMapper(TagMapping.class);

    void toListTagVo(List<Tag> source, @MappingTarget List<TagVo> target);
    void toTagVo(Tag source, @MappingTarget TagVo target);
}
