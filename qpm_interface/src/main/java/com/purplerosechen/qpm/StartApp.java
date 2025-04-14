package com.purplerosechen.qpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 启动app
 * @date 11 4月 2025 14:30
 */

@SpringBootApplication
public class StartApp {
    public static void main(String[] args) {
        System.setProperty("jasypt.encryptor.password", "8FPeEHKNCzRt3UX4Yrdn");
        SpringApplication.run(StartApp.class, args);
    }
}
