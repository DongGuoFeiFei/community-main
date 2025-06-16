package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.po.FavoriteFolder;
import com.example.communityserver.entity.vo.FoldersVo;
import com.example.communityserver.mapper.FavoriteArticleMapper;
import com.example.communityserver.mapper.FavoriteFolderMapper;
import com.example.communityserver.service.IFavoriteFolderService;
import com.example.communityserver.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-02
 **/

@Service
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder> implements IFavoriteFolderService {
    @Autowired
    private FavoriteFolderMapper favoriteFolderMapper;

    @Autowired
    private FavoriteArticleMapper favoriteArticleMapper;
    @Override
    public List<FoldersVo> getFavoriteFolder() {
        return favoriteFolderMapper.getFavoriteFolder(SecurityUtils.getLoginUserId());
    }

    @Override
    public FoldersVo addFolder(String name) {
        FavoriteFolder favoriteFolder = new FavoriteFolder();
        favoriteFolder.setFolderName(name);
        favoriteFolder.setUserId(SecurityUtils.getLoginUserId());
        FoldersVo foldersVo = new FoldersVo();
        foldersVo.setId(favoriteFolder.getFolderId());
        foldersVo.setName(favoriteFolder.getFolderName());
        foldersVo.setCreateAt(favoriteFolder.getCreatedAt());
        foldersVo.setArticleCount(0L);
        return favoriteFolderMapper.insert(favoriteFolder) > 0 ? foldersVo : null;
    }
}
