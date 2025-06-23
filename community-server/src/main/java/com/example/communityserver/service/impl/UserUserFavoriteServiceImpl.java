package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.dto.AddFavoriteArticle;
import com.example.communityserver.entity.dto.GetUserFavoListParam;
import com.example.communityserver.entity.dto.MoveFavoriteDto;
import com.example.communityserver.entity.enums.FavoriteTypeEnum;
import com.example.communityserver.entity.po.UserFavorite;
import com.example.communityserver.entity.vo.FavArticleVo;
import com.example.communityserver.entity.vo.MoveFavoriteVo;
import com.example.communityserver.entity.vo.UserFavoListVo;
import com.example.communityserver.mapper.UserFavoriteMapper;
import com.example.communityserver.mapping.FavoriteMapping;
import com.example.communityserver.service.IUserFavoriteService;
import com.example.communityserver.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-06-16
 **/

@Service
public class UserUserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite> implements IUserFavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;


    @Override
    public FavArticleVo addFavArticle(AddFavoriteArticle param) {
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setArticleId(param.getArticleId());
        userFavorite.setFolderId(param.getFolderId());
        userFavorite.setUserId(SecurityUtils.getLoginUserId());
        int insert = userFavoriteMapper.insert(userFavorite);
        FavArticleVo favArticleVo = new FavArticleVo();
        if (insert > 0) {
            BeanUtils.copyProperties(userFavorite, favArticleVo);
        } else {
            favArticleVo = null;
        }
        return favArticleVo;
    }

    @Override
    public Page<UserFavoListVo> getUserFavoListVo(GetUserFavoListParam param) {
        Page<UserFavoListVo> page = new Page<>(param.getPage(), param.getSize());
        // TODO: 2025/6/22 连接的表格过多，优化sql，分成多次查询，代码组装数据
        return userFavoriteMapper.selectFavoList(page, param, SecurityUtils.getLoginUserId());
    }

    @Override
    public MoveFavoriteVo moveFavoriteVoResult(MoveFavoriteDto dto) {
        // 判断是否存在——删除原有——建立新数据
        MoveFavoriteVo vo = new MoveFavoriteVo();
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SecurityUtils.getLoginUserId() != null, UserFavorite::getUserId, SecurityUtils.getLoginUserId())
                .eq(dto.getArticleId() != null, UserFavorite::getArticleId, dto.getArticleId())
                .eq(dto.getActiveFolderId() != null, UserFavorite::getFolderId, dto.getActiveFolderId());
        UserFavorite userFavorite = userFavoriteMapper.selectOne(queryWrapper);
        if (userFavorite != null) {
            int delete = userFavoriteMapper.delete(queryWrapper);
            if (delete > 0) {
                userFavorite.setFavoriteId(null);
                userFavorite.setFolderId(dto.getFolderId());
                userFavorite.setType(FavoriteTypeEnum.ARTICLE.getValue());
                int insert = userFavoriteMapper.insert(userFavorite);
                if (insert > 0) {
                    FavoriteMapping.INSTANCE.updateMoveVoFromUserFavorite(userFavorite, vo);
                } else {
                    vo = null;
                }
            }
        }
        return vo;
    }
}
