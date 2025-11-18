package com.example.communityserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class test {

    @Test
    void contextLoads() {
        long nowMs = System.currentTimeMillis();
        System.out.println(nowMs);
    }


}
