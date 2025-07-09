package com.example.communityserver.adminController;

import com.example.communityserver.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TODO
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-07-08
 **/

@RestController
@RequestMapping("admin/article")
public class AdminArticleController {
    @Autowired
    private IArticleService articleService;

}
