package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.model.FavoriteFolder;
import com.example.communityserver.entity.response.FolderVo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/


public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {
    List<FolderVo> getFavoriteFolder(Long loginUserId);

    FolderVo selectFolderVoById(Long folderId);
}
