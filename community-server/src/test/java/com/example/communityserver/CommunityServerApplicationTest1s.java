package com.example.communityserver;

import com.example.communityserver.service.IArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommunityServerApplicationTest1s {

    @Autowired
    private IArticleService articleService;

    @Test
    void test() {
        System.out.println(articleService.getArticleDtlVo(1L));
    }
}
