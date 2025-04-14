package com.purplerosechen.qpm.tools;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 机器人配置
 * @date 14 4月 2025 10:03
 */

@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "bot.app-id")
public class BotConfig {
    @Value("${bot.app-id}")
    private String appId;
    @Value("${bot.secret}")
    private String appSecret;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.number}")
    private String number;
}
