package com.purplerosechen.qpm.tools.http;

import com.purplerosechen.qpm.tools.BotConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 15 4月 2025 16:21
 */

@Configuration
public class WebClientConfig {
    @Resource
    private BotConfig botConfig;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(botConfig.getSxUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
