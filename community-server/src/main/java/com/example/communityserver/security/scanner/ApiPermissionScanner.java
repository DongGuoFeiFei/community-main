package com.example.communityserver.security.scanner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.communityserver.entity.model.ApiPermission;
import com.example.communityserver.mapper.ApiPermissionMapper;
import com.example.communityserver.security.core.Logical;
import com.example.communityserver.security.core.RequiresPermission;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * 扫描并保存API权限信息
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-09-20
 **/
@Slf4j
//@Component
public class ApiPermissionScanner implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApiPermissionMapper apiPermissionMapper;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("开始扫描API权限");
        scanAndSaveApiPermissions();
        log.info("API权限扫描完成");
    }


    private void scanAndSaveApiPermissions() {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RestController.class);

        for (Object controller : controllers.values()) {
            log.info("扫描控制器: {}", controller.getClass().getName());

            // 获取原始类
            Class<?> targetClass = AopUtils.getTargetClass(controller);
            // 获取类上的RequestMapping注解
            RequestMapping classMapping = targetClass.getAnnotation(RequestMapping.class);
            String basePath = classMapping != null && classMapping.value().length > 0 ? classMapping.value()[0] : "";
            log.info("处理路径: {}", basePath);
            // 获取所有方法
            Method[] methods = targetClass.getMethods();

            for (Method method : methods) {
                // 检查方法是否有RequiresPermission注解
                RequiresPermission requiresPermission = method.getAnnotation(RequiresPermission.class);
                if (requiresPermission == null) {
                    continue;
                }
                // 获取方法映射信息
                ApiMethodInfo methodInfo = extractMethodInfo(method);
                // 处理每个权限标识
                for (String perm : requiresPermission.api()) {
                    saveApiPermission(
                            methodInfo.getApiName(),
                            basePath + methodInfo.getPath(),
                            methodInfo.getHttpMethod(),
                            perm,
                            requiresPermission.logical()
                    );
                }
            }
        }
    }

    private ApiMethodInfo extractMethodInfo(Method method) {
        ApiMethodInfo info = new ApiMethodInfo();

        // 获取方法名称
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        info.setApiName(apiOperation != null ? apiOperation.value() : method.getName());

        // 获取路径和HTTP方法
        RequestMapping methodMapping = method.getAnnotation(RequestMapping.class);
        if (methodMapping != null) {
            info.setPath(methodMapping.value().length > 0 ? methodMapping.value()[0] : "");
            info.setHttpMethod(methodMapping.method().length > 0 ?
                    methodMapping.method()[0].name() : "GET");
        } else {
            // 检查特定HTTP方法注解
            if (method.getAnnotation(GetMapping.class) != null) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                info.setPath(getMapping.value().length > 0 ? getMapping.value()[0] : "");
                info.setHttpMethod("GET");
            } else if (method.getAnnotation(PostMapping.class) != null) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                info.setPath(postMapping.value().length > 0 ? postMapping.value()[0] : "");
                info.setHttpMethod("POST");
            } else if (method.getAnnotation(PutMapping.class) != null) {
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                info.setPath(putMapping.value().length > 0 ? putMapping.value()[0] : "");
                info.setHttpMethod("PUT");
            } else if (method.getAnnotation(DeleteMapping.class) != null) {
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                info.setPath(deleteMapping.value().length > 0 ? deleteMapping.value()[0] : "");
                info.setHttpMethod("DELETE");
            }
        }

        return info;
    }

    private void saveApiPermission(String apiName, String apiPath, String httpMethod,
                                   String perms, Logical logical) {
        LambdaQueryWrapper<ApiPermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiPermission::getPerms, perms);

        ApiPermission existing = apiPermissionMapper.selectOne(queryWrapper);

        if (existing == null) {
            // 创建新记录
            ApiPermission permission = new ApiPermission();
            permission.setApiName(apiName);
            permission.setApiPath(apiPath);
            permission.setHttpMethod(httpMethod);
            permission.setPerms(perms);
            permission.setStatus(1);
            apiPermissionMapper.insert(permission);
        } else {
            // 更新现有记录
            existing.setApiName(apiName);
            existing.setApiPath(apiPath);
            existing.setHttpMethod(httpMethod);
            existing.setStatus(1);
            apiPermissionMapper.updateById(existing);
        }
    }

    @Data
    private static class ApiMethodInfo {
        private String apiName;
        private String path;
        private String httpMethod;
    }
}
