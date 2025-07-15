package com.example.communityserver.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.communityserver.utils.security.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createdAt", new Date(), metaObject);
        this.setFieldValByName("updatedTime", new Date(), metaObject);
        this.setFieldValByName("createAt", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("createBy", SecurityUtils.getLoginUserId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updatedTime", new Date(), metaObject);
        this.setFieldValByName("updateAt", new Date(), metaObject);
        this.setFieldValByName("updatedAt", new Date(), metaObject);
        this.setFieldValByName("loginLastTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", SecurityUtils.getLoginUserId(), metaObject);
    }
}
