package com.purplerosechen.qpm.tools.http;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.tools.BotConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 请求http
 * @date 15 4月 2025 16:10
 */

@Service
@Slf4j
public class BotSendHttp {
    @Resource()
    private BotConfig botConfig;
    @Resource
    private WebClient webClient;

    private String accessToken;
    private Long expireTime = 0L;

    public Mono<String> post(Object body, String url) throws Exception {
        if (!updateAccessToken()) {
            throw new Exception("获取accessToken失败");
        }
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "QQBot "+accessToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                ;
    }

    public boolean inputAccTokens(){
        JSONObject at = new JSONObject();
        at.put("appId",botConfig.getAppId());
        at.put("clientSecret",botConfig.getAppSecret());
        Mono<String> res = webClient.post()
                .uri("https://bots.qq.com/app/getAppAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(at)
                .retrieve()
                .bodyToMono(String.class)
                ;
        AtomicBoolean success = new AtomicBoolean(false);
        res.subscribe(
                s -> {
                    log.info("获取accessToken成功:{}", s);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    accessToken = jsonObject.getString("access_token");
                    expireTime = jsonObject.getLong("expires_in") + System.currentTimeMillis();
                    success.set(true);
                },
                e -> {
                    log.error("获取accessToken失败:{}", e.getMessage());
                }
        );
        res.block();
        return success.get();
    }

    private boolean updateAccessToken() throws Exception {
        synchronized (this) {
            if (System.currentTimeMillis() >= expireTime) {
                if (inputAccTokens()) {
                    return true;
                }
                return false;
            }
            return false;
        }
    }
}
