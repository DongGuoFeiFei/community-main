package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.request.ModifyFolderNameParam;
import com.example.communityserver.entity.model.FavoriteFolder;
import com.example.communityserver.entity.response.FolderVo;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/


public interface IFavoriteFolderService extends IService<FavoriteFolder> {
    List<FolderVo> getFavoriteFolder();

    // 创建
    FolderVo addFolder(String name);

    // 修改
    FolderVo modifyFolderName(Long folderId, ModifyFolderNameParam param);

    boolean delFolder(Long folderId);
}
