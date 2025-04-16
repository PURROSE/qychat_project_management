package com.purplerosechen.qpm.tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 16 4月 2025 09:36
 */

@Configuration
public class ThreadPoolLocal {

    @Bean
    public Executor getExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
