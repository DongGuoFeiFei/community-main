package com.example.communityserver.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.example.communityserver.handler.JsonMapTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * mybatisPlus配置
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-04-24
 **/


@Configuration
@MapperScan({"com.example.communityserver.mapper", "com.example.communityserver.chat.mapper"})
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }

    /**
     * 添加map拦截器
     */
    @Bean
    public JsonMapTypeHandler jsonMapTypeHandler() {
        return new JsonMapTypeHandler();
    }

    // TODO: 2025/8/26 动态分表
//    @Bean
//    public DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
//        DynamicTableNameInnerInterceptor interceptor = new DynamicTableNameInnerInterceptor();
//        Map<String, TableNameHandler> tableNameHandlerMap = new HashMap<>();
//
//        // 为 im_message 表设置动态表名处理器
//        tableNameHandlerMap.put("im_message", (sql, tableName) -> {
//            // 根据业务逻辑动态生成表名，比如按年月: im_message_202501
//            // 这里可以根据线程局部变量、参数等决定表名
//            LocalDateTime now = LocalDateTime.now();
//            String yearMonth = String.format("%d%02d", now.getYear(), now.getMonthValue());
//            return "im_message_" + yearMonth;
//        });
//        interceptor.setTableNameHandlerMap(tableNameHandlerMap);
//        return interceptor;
//    }

}
