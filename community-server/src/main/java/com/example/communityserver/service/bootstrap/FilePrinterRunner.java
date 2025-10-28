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
public class FilePrinterRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        bannerStartPrint();
    }

    private void bannerStartPrint() {
        try {
            Resource resource = new ClassPathResource("bannerStart.txt");
            String content = Files.readString(Paths.get(resource.getURI()));
            System.out.println(content);
        } catch (IOException e) {
            System.err.println("无法读取bannerStart.txt文件: " + e.getMessage());
        }
    }

}
