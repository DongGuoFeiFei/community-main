package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.request.ModifyFolderNameParam;
import com.example.communityserver.entity.model.FavoriteFolder;
import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.response.FolderVo;
import com.example.communityserver.mapper.FavoriteFolderMapper;
import com.example.communityserver.mapper.UserFavoriteMapper;
import com.example.communityserver.service.IFavoriteFolderService;
import com.example.communityserver.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserFavoriteMapper userFavoriteMapper;

    @Override
    public List<FolderVo> getFavoriteFolder() {
        return favoriteFolderMapper.getFavoriteFolder(SecurityUtils.getLoginUserId());
    }

    @Override
    public FolderVo addFolder(String name) {
        FavoriteFolder favoriteFolder = new FavoriteFolder();
        favoriteFolder.setFolderName(name);
        favoriteFolder.setUserId(SecurityUtils.getLoginUserId());

        int insert = favoriteFolderMapper.insert(favoriteFolder);
        if (insert>0){
            FolderVo folderVo = new FolderVo();
            folderVo.setId(favoriteFolder.getFolderId());
            folderVo.setName(favoriteFolder.getFolderName());
            folderVo.setCreateAt(favoriteFolder.getCreatedAt());
            folderVo.setArticleCount(0L);
            return folderVo;
        }
        return null;
    }

    @Override
    public FolderVo modifyFolderName(Long folderId, ModifyFolderNameParam param) {
        FolderVo folderVo = null;
        LambdaUpdateWrapper<FavoriteFolder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(folderId != null, FavoriteFolder::getFolderId, folderId)
                .eq(SecurityUtils.getLoginUserId() != null, FavoriteFolder::getUserId, SecurityUtils.getLoginUserId())
                .set(param.getName() != null, FavoriteFolder::getFolderName, param.getName());
        int update = favoriteFolderMapper.update(null, updateWrapper);
        if (update > 0) {
            folderVo = favoriteFolderMapper.selectFolderVoById(folderId);
        }
        return folderVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delFolder(Long folderId) {
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SecurityUtils.getLoginUserId() != null, UserFavorite::getUserId, SecurityUtils.getLoginUserId())
                .eq(folderId != null, UserFavorite::getFolderId, folderId);
        int delete = userFavoriteMapper.delete(queryWrapper);
        // TODO: 2025/6/22 没有验证收藏的数据是否删除成功
        LambdaQueryWrapper<FavoriteFolder> folderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        folderLambdaQueryWrapper.eq(SecurityUtils.getLoginUserId() != null, FavoriteFolder::getUserId, SecurityUtils.getLoginUserId())
                .eq(folderId != null, FavoriteFolder::getFolderId, folderId);
        int deleteFolder = favoriteFolderMapper.delete(folderLambdaQueryWrapper);
        return deleteFolder > 0;
    }
}
