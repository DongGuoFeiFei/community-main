package com.example.communityserver.controller;

import com.example.communityserver.entity.request.DeepseekPromptParam;
import com.example.communityserver.service.impl.DeepSeekService;
import com.example.communityserver.utils.web.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 深度搜索控制器类
 * 提供与深度搜索大模型交互的RESTful API接口
 *
 * <p>
 * 该控制器类负责处理与深度搜索大模型相关的HTTP请求
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-23
 **/
@Api(tags = "deepseek大模型对话接口")
@RestController
@RequestMapping("/api/deepseek")
public class DeepSeekController {
    @Autowired
    private DeepSeekService deepSeekService;

    // 分用户等级使用api限制
    @PostMapping("/generate")
    public Result<String> generateText(@RequestBody DeepseekPromptParam param) {
        return Result.success(deepSeekService.parseChatContent(deepSeekService.generateText(param.getPrompt())));
    }
}
