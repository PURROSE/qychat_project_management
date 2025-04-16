package com.purplerosechen.qpm.tools.http;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.tools.BotConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
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
                .uri(ApiHttpUrlEnum.GET_ACC_TOKENS.getUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(at)
                .retrieve()
                .bodyToMono(String.class)
                ;
        AtomicBoolean success = new AtomicBoolean(false);
        res.subscribe(
                s -> {
                    log.info("获取accessToken成功:{}", s);

                },
                e -> {
                    log.error("获取accessToken失败:{}", e.getMessage());
                }
        );

        JSONObject jsonObject = JSONObject.parseObject(res.block());
        if (jsonObject != null && jsonObject.getString("access_token") != null && jsonObject.getLong("expires_in") != null) {
            accessToken = jsonObject.getString("access_token");
            expireTime = (jsonObject.getLong("expires_in") * 1000) + System.currentTimeMillis();
            success.set(true);
        }
        return success.get();
    }

    private boolean updateAccessToken() throws Exception {
        synchronized (this) {
            if (System.currentTimeMillis() >= expireTime) {
                return inputAccTokens();
            }
            return true;
        }
    }

    public Mono<String> otherGet(HashMap<String,Object> body, String url) {
        UriComponentsBuilder uriBuilder =  UriComponentsBuilder.fromHttpUrl(url);
        for ( String key : body.keySet() ) {
            uriBuilder.queryParam(key, body.get(key));
        }
        UriComponents uri = uriBuilder.build();

        return webClient.get()
                .uri(uri.toUriString())
                .retrieve()
                .bodyToMono(String.class)
                ;
    }
}
