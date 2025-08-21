package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.model.PointsLevel;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.model.UserPointsAccount;
import com.example.communityserver.entity.request.SearchNameStatusParam;
import com.example.communityserver.mapper.PointsLevelMapper;
import com.example.communityserver.mapper.UserMapper;
import com.example.communityserver.mapper.UserPointsAccountMapper;
import com.example.communityserver.service.IUserPointsAccountService;
import com.example.communityserver.utils.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-08-21
 **/
@Service
public class UserPointsAccountServiceImpl extends ServiceImpl<UserPointsAccountMapper, UserPointsAccount> implements IUserPointsAccountService {
    @Autowired
    private UserPointsAccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointsLevelMapper levelMapper;

    @Override
    public IPage<UserPointsAccount> getPointsAccounts(SearchNameStatusParam param) {
        Page<UserPointsAccount> page = new Page<>(param.getPage(), param.getSize());

        LambdaQueryWrapper<UserPointsAccount> wrapper = new LambdaQueryWrapper<>();

        if (StringUtil.isNotBlank(param.getKeyword())) {
            wrapper.like(UserPointsAccount::getAccountId, param.getKeyword())
                    .or()
                    .like(UserPointsAccount::getUserId, getUserIdByUserName(param.getKeyword()));
        }

        Page<UserPointsAccount> selectPage = accountMapper.selectPage(page, wrapper);

        List<Long> userIds = selectPage.getRecords().stream().map(UserPointsAccount::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<Integer> levelIds = selectPage.getRecords().stream().map(UserPointsAccount::getLevelId).collect(Collectors.toList());
        Map<Integer, PointsLevel> levelMap = levelMapper.selectBatchIds(levelIds).stream()
                .collect(Collectors.toMap(PointsLevel::getLevelId, Function.identity()));

        page.getRecords().forEach(account -> {
            account.setUser(userMap.get(account.getUserId()));
            account.setPointsLevel(levelMap.get(account.getLevelId()));
        });
        return page;
    }

    @Override
    public UserPointsAccount getPointsAccountDetail(Long accountId) {
        UserPointsAccount account = accountMapper.selectById(accountId);
        User user = userMapper.selectById(account.getUserId());
        PointsLevel pointsLevel = levelMapper.selectById(account.getLevelId());
        account.setUser(user);
        account.setPointsLevel(pointsLevel);
        return account;
    }

    private Long getUserIdByUserName(String keyword) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().like(User::getUsername, keyword));
        return user.getUserId();
    }
}
