package com.example.communityserver.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.communityserver.entity.model.UserPointsAccount;
import com.example.communityserver.entity.request.SearchNameStatusParam;
import com.example.communityserver.security.core.RequiresPermission;
import com.example.communityserver.service.IUserPointsAccountService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/

@Slf4j
@RestController
@Api(tags = "用户积分账号")
@RequestMapping("pointsAccount")
public class UserPointsAccountController {
    @Autowired
    private IUserPointsAccountService userPointsAccountService;

    ///**
// * 获取用户积分账户列表
// * @param {Object} params 查询参数
// * @returns {Promise}
// */
//export const getPointsAccounts = (params) => {
//  return request.get('/points/accounts', { params });
//};
    @GetMapping()
    @ApiOperation("获取用户积分账户列表")
    @RequiresPermission(api = {"pointsAccount:get"})
    public Result<Result.PageData<UserPointsAccount>> getPointsAccounts(SearchNameStatusParam param) {
        IPage<UserPointsAccount> pageVo = userPointsAccountService.getPointsAccounts(param);
        return pageVo != null ? Result.pageSuccess(pageVo.getTotal(), pageVo.getRecords()) : Result.error();
    }

    //    /**
// * 获取单个用户积分账户详情
// * @param {number} accountId 账户ID
// * @returns {Promise}
// */
//export const getPointsAccountDetail = (accountId) => {
//  return request.get(`/pointsAccount/${accountId}`);
//};
    @GetMapping("{accountId}")
    @ApiOperation("获取单个用户积分账户详情")
    @RequiresPermission(api = {"pointsAccount:{id}:get"})
    public Result<UserPointsAccount> getPointsAccountDetail(@PathVariable Long accountId) {
        UserPointsAccount userPointsAccount = userPointsAccountService.getPointsAccountDetail(accountId);
        return userPointsAccount != null ? Result.success(userPointsAccount) : Result.error();
    }

}
