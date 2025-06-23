package com.example.communityserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.communityserver.entity.po.FavoriteFolder;
import com.example.communityserver.entity.vo.FolderVo;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/


public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {
    List<FolderVo> getFavoriteFolder(Long loginUserId);

    FolderVo selectFolderVoById(Long folderId);
}
