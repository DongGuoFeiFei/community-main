package com.example.communityserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.communityserver.entity.model.UserFavorite;
import com.example.communityserver.entity.model.UserPointsAccount;
import com.example.communityserver.entity.request.SearchNameStatusParam;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/


public interface IUserPointsAccountService extends IService<UserPointsAccount> {
    IPage<UserPointsAccount> getPointsAccounts(SearchNameStatusParam param);

    UserPointsAccount getPointsAccountDetail(Long accountId);
}
