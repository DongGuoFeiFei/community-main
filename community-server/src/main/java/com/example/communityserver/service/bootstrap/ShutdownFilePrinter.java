package com.example.communityserver.service.bootstrap;
import com.example.communityserver.entity.constants.SystemConstants;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private final Instant startTime = Instant.now();

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            Resource resource = new ClassPathResource("bannerEnd.txt");
            String content = Files.readString(Paths.get(resource.getURI()));
            System.out.println(content);

        } catch (Exception e) {
            System.err.println("无法读取bannerEnd.txt文件: " + e.getMessage());
        }
        try {
            printSystemMetrics();
        } catch (Exception e) {
            System.out.println("无法打印系统信息: " + e.getMessage());
        }
    }

    private void printSystemMetrics() {
        Runtime runtime = Runtime.getRuntime();
        System.out.printf(
                "服务版本: %s\n" +
                "运行时长: %d分钟\n" +
                "内存使用: %.1fMB/%.1fMB\n" +
                "启动时间: %s\n" +
                "终止时间: %s\n" +
                "%n",
                SystemConstants.COMMUNITY_VERSION,
                Duration.between(startTime, Instant.now()).toMinutes(),
                (runtime.totalMemory() - runtime.freeMemory()) / 1024.0 / 1024,
                runtime.maxMemory() / 1024.0 / 1024,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.ofInstant(startTime,java.time.ZoneId.systemDefault())),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
