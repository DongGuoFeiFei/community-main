package com.example.communityserver.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.communityserver.security.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description: 自动填充元数据处理器
 * @Param: 无
 * @return: 无
 * @Author: DongGuo
 */

@Component  // 将标记该类为Spring组件，使其成为Spring容器中的一个Bean
public class AutoFillMetaObjectHandler implements MetaObjectHandler {  // 实现MetaObjectHandler接口，用于MyBatis Plus的自动填充功能


    /**
     * 插入数据时的填充策略
     *
     * @param metaObject 元数据对象，可以获取和设置到对象的值
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置创建时间字段
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createdAt", new Date(), metaObject);
        this.setFieldValByName("sendTime", new Date(), metaObject);
        // 设置更新时间字段
        this.setFieldValByName("updatedTime", new Date(), metaObject);
        this.setFieldValByName("createAt", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        // 设置创建人字段，获取当前登录用户ID
        this.setFieldValByName("createBy", SecurityUtils.getLoginUserId(), metaObject);
    }

    /**
     * 更新数据时的填充策略
     *
     * @param metaObject 元数据对象，可以获取和设置到对象的值
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 设置更新时间字段
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updatedTime", new Date(), metaObject);
        this.setFieldValByName("updateAt", new Date(), metaObject);
        this.setFieldValByName("updatedAt", new Date(), metaObject);
        // 设置最后登录时间字段
        this.setFieldValByName("loginLastTime", new Date(), metaObject);
        // 设置更新人字段，获取当前登录用户ID
        this.setFieldValByName("updateBy", SecurityUtils.getLoginUserId(), metaObject);
    }
}
