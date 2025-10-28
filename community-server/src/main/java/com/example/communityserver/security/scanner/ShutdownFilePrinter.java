package com.example.communityserver.security.scanner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
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
public class ShutdownFilePrinter implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            Resource resource = new ClassPathResource("bannerEnd.txt");
            String content = Files.readString(Paths.get(resource.getURI()));
            System.out.println(content);
        } catch (Exception e) {
            System.err.println("无法读取bannerEnd.txt文件: " + e.getMessage());
        }
    }
}
