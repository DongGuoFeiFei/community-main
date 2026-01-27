package com.example.communityserver.service.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p>
 * 启动横板打印
 * <p>
 *
 * @author: DongGuo
 * @create: 2025-10-26
 **/


@Component
public class StartRunFilePrinter implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        bannerStartPrint();
    }

    private void bannerStartPrint() {
        try {
            Resource resource = new ClassPathResource("bannerStart.txt");
            if (!resource.exists()) {
                return;
            }
            String content = Files.readString(Paths.get(resource.getURI()));
            System.out.println(content);
        } catch (Exception e) {
            // 静默处理，避免启动失败
        }
    }

}
