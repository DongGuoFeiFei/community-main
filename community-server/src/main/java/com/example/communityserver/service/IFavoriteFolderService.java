package com.example.communityserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.po.FavoriteFolder;
import com.example.communityserver.entity.vo.FoldersVo;

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
    List<FoldersVo> getFavoriteFolder();

    FoldersVo addFolder(String name);
}
