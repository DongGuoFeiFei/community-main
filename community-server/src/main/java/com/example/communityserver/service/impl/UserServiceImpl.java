package com.example.communityserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.communityserver.entity.constants.CacheKeyConstants;
import com.example.communityserver.entity.constants.SystemConstants;
import com.example.communityserver.entity.enums.MessageCodeEnum;
import com.example.communityserver.entity.model.LoginUser;
import com.example.communityserver.entity.model.User;
import com.example.communityserver.entity.request.RegisterDto;
import com.example.communityserver.entity.request.UserSearchParam;
import com.example.communityserver.entity.response.AuthorInfoVo;
import com.example.communityserver.entity.response.UserCountStats;
import com.example.communityserver.entity.response.UserDelVo;
import com.example.communityserver.mapper.ArticleMapper;
import com.example.communityserver.mapper.FollowMapper;
import com.example.communityserver.mapper.UserMapper;
import com.example.communityserver.service.IArticleService;
import com.example.communityserver.service.IEmailService;
import com.example.communityserver.service.IFollowService;
import com.example.communityserver.service.IUserService;
import com.example.communityserver.utils.redis.RedisUtil;
import com.example.communityserver.security.util.JWTUtil;
import com.example.communityserver.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IFollowService followService;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public String login(String username, String password) {
        // 把账号密码传入security提供的类中封装
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        // 这里会去调用loadUserByUsername方法，得到LoginUser对象
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // authenticate如果不是null，就认证通过
        if (authenticate == null) {
            throw new RuntimeException("认证没通过");
        }
        // 获取authenticate中的LoginUser对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 把loginUser存入redis中
        redisUtil.setCacheObject(CacheKeyConstants.LOGIN_USER_ID + loginUser.getUser().getUserId(), loginUser);
        return JWTUtil.createToken(loginUser.getUser().getUserId());
    }


    /**
     * @Description: 返回错误码，不存在，返回null
     * @Param: [email, username]
     * @return: com.example.communityserver.entity.enums.MessageCodeEnum
     * @Author: DongGuo
     */

    @Override
    public MessageCodeEnum isExistUser(String email, String username, String phone) {
        // 验证邮箱、用户名、用户名和邮箱
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        User user;
        queryWrapper.eq(email != null, User::getEmail, email);
        user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return MessageCodeEnum.EMAIL_EXIST;
        }
        queryWrapper.eq(username != null, User::getUsername, username);
        user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return MessageCodeEnum.USERNAME_EXIST;
        }
        queryWrapper.eq(phone != null, User::getPhone, phone);
        user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return MessageCodeEnum.PHONE_EXIST;
        }

        return MessageCodeEnum.USER_NOT_EXIST;
    }


    @Override
    public MessageCodeEnum register(RegisterDto dto) {
        MessageCodeEnum codeEnum = isExistUser(dto.getEmail(), dto.getUsername(), null);
        if (codeEnum != MessageCodeEnum.USER_NOT_EXIST) {
            return codeEnum;
        }
        //注册
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setEmail(dto.getEmail());
        user.setPassword(SecurityUtils.encryptPassword(dto.getPassword()));

        // TODO: 2025/7/29 使用email本身的url仍存在问题
        String emailGravatarUrl = emailService.getEmailGravatarUrl(dto.getEmail());
        user.setAvatar(emailGravatarUrl);
        int insert = userMapper.insert(user);
        if (insert > 0) {
            return MessageCodeEnum.REGISTER_SUCCESS;
        } else {
            return MessageCodeEnum.REGISTER_ERROR;
        }
    }

    @Override
    public AuthorInfoVo getAuthorInfoVo(Long articleId) {
        AuthorInfoVo vo = userMapper.getAuthorInfoVo(articleId, SecurityUtils.getLoginUserId());
        if (vo == null) {
            return null;
        }
        Long postCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_ARTICLE_COUNT + vo.getId(), Long.class);
        if (postCount == null) {
            postCount = articleMapper.countByUser(vo.getId());
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_ARTICLE_COUNT + vo.getId(), postCount, 3, TimeUnit.DAYS);
        Long followerCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_FOLLOWER_COUNT + vo.getId(), Long.class);
        if (followerCount == null) {
            followerCount = followMapper.countFollowers(vo.getId());
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_FOLLOWER_COUNT + vo.getId(), followerCount, 3, TimeUnit.DAYS);
        Long followingCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_FOLLOWING_COUNT + vo.getId(), Long.class);
        if (followingCount == null) {
            followingCount = followMapper.countFollowing(vo.getId());
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_FOLLOWING_COUNT + vo.getId(), followingCount, 3, TimeUnit.DAYS);
        vo.setPostCount(postCount);
        vo.setFollowerCount(followerCount);
        vo.setFollowingCount(followingCount);
        return vo;
    }

    @Override
    public UserCountStats getUserStats(Long userId) {

        // 帖子数
        Long postCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_ARTICLE_COUNT + userId, Long.class);
        if (postCount == null) {
            postCount = articleMapper.countByUser(userId);
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_ARTICLE_COUNT + userId, postCount, 3, TimeUnit.DAYS);

        // 关注数
        Long followerCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_FOLLOWER_COUNT + userId, Long.class);
        if (followerCount == null) {
            followerCount = followMapper.countFollowers(userId);
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_FOLLOWER_COUNT + userId, followerCount, 3, TimeUnit.DAYS);

        // 粉丝数
        Long followingCount = redisUtil.getCacheObjectAsNumber(CacheKeyConstants.USER_FOLLOWING_COUNT + userId, Long.class);
        if (followingCount == null) {
            followingCount = followMapper.countFollowing(userId);
        }
        redisUtil.setCacheObject(CacheKeyConstants.USER_FOLLOWING_COUNT + userId, followingCount, 3, TimeUnit.DAYS);
        UserCountStats stats = new UserCountStats();
        stats.setFollowers(followerCount);
        stats.setFollowing(followingCount);
        stats.setPostCount(postCount);
        return stats;
    }

    @Override
    public IPage<UserDelVo> getUsers(UserSearchParam param) {
        Page<UserDelVo> page = new Page<>(param.getPage(), param.getSize());
        return userMapper.getUsers(page, param);
    }

    @Override
    public UserDelVo getUserProfile(Long userId) {
        UserDelVo userProfile = userMapper.getUserProfile(userId);
        userProfile.setAvatar(SystemConstants.BASIC_URL + userProfile.getAvatar());
        return userProfile;
    }


}
