package com.example.communityserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"com.example.communityserver.mapper", "com.example.communityserver.chat.mapper"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@EnableRetry
@EnableAsync
@PropertySource("classpath:secrets.properties")
public class CommunityServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityServerApplication.class, args);
        System.out.println("               _                            _                    _  __                                                                         _ _         \n" +
                "              | |                          | |                  (_)/ _|                                                                       (_) |        \n" +
                " __      _____| | ___ ___  _ __ ___   ___  | |_ ___     ___ __ _ _| |_ _   _ _ __ ___  _ __   __ _    ___ ___  _ __ ___  _ __ ___  _   _ _ __  _| |_ _   _ \n" +
                " \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\   / __/ _` | |  _| | | | '__/ _ \\| '_ \\ / _` |  / __/ _ \\| '_ ` _ \\| '_ ` _ \\| | | | '_ \\| | __| | | |\n" +
                "  \\ V  V /  __/ | (_| (_) | | | | | |  __/ | || (_) | | (_| (_| | | | | |_| | | | (_) | | | | (_| | | (_| (_) | | | | | | | | | | | |_| | | | | | |_| |_| |\n" +
                "   \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/   \\___\\__,_|_|_|  \\__,_|_|  \\___/|_| |_|\\__, |  \\___\\___/|_| |_| |_|_| |_| |_|\\__,_|_| |_|_|\\__|\\__, |\n" +
                "                                                                                              __/ |                                                   __/ |\n" +
                "                                                                                             |___/                                                   |___/ ");
    }

}
