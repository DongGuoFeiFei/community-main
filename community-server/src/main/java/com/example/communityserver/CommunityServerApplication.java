package com.example.communityserver;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;


@Slf4j
@SpringBootApplication
@MapperScan({"com.example.communityserver.mapper", "com.example.communityserver.chat.mapper"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@EnableRetry
@EnableAsync
@PropertySource("classpath:secrets.properties")
public class CommunityServerApplication {

    @SneakyThrows
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(CommunityServerApplication.class);
        ConfigurableApplicationContext application = app.run(args);
        System.out.println("               _                            _                    _  __                                                                         _ _         \n" +
                "              | |                          | |                  (_)/ _|                                                                       (_) |        \n" +
                " __      _____| | ___ ___  _ __ ___   ___  | |_ ___     ___ __ _ _| |_ _   _ _ __ ___  _ __   __ _    ___ ___  _ __ ___  _ __ ___  _   _ _ __  _| |_ _   _ \n" +
                " \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\   / __/ _` | |  _| | | | '__/ _ \\| '_ \\ / _` |  / __/ _ \\| '_ ` _ \\| '_ ` _ \\| | | | '_ \\| | __| | | |\n" +
                "  \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) | | (_| (_| | | | | |_| | | | (_) | | | | (_| | | (_| (_) | | | | | | | | | | | |_| | | | | | |_| |_| |\n" +
                "   \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/   \\___\\__,_|_|_|  \\__,_|_|  \\___/|_| |_|\\__, |  \\___\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|_|\\__|\\__, |\n" +
                "                                                                                              __/ |                                                   __/ |\n" +
                "                                                                                             |___/                                                   |___/ ");
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t" +
                        "Doc: \thttp://{}:{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));

    }

}
