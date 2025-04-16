package com.purplerosechen.qpm.controller;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.config.CallBackApiConfig;
import com.purplerosechen.qpm.dto.ParentCallBackDto;
import com.purplerosechen.qpm.tools.BotConfig;
import com.purplerosechen.qpm.tools.enc.CallBackSignUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.Executor;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 回调接口
 * @date 11 4月 2025 16:30
 */

@Slf4j
@RestController
@RequestMapping("/mqb/call-back")
public class QcCallBackController {

    @Resource
    private BotConfig botConfig;
    @Resource
    private CallBackApiConfig callbackApiConfig;
    @Resource
    private Executor executor;

    @RequestMapping("/test")
    public Mono<Object> testCallBack(
            @RequestBody String reqBody,
            @RequestHeader("X-Signature-Ed25519") String xSignatureEd25519,
            @RequestHeader("X-Signature-Timestamp") String xSignatureTimestamp,
            ServerWebExchange serverWebExchange
            ) throws Exception {

        log.info("xSignatureEd25519:{};xSignatureTimestamp:{};reqBody:{}", xSignatureEd25519, xSignatureTimestamp, reqBody);

        if (!CallBackSignUtil.verifySignature(
                botConfig.getAppSecret(),
                xSignatureEd25519,
                xSignatureTimestamp,
                reqBody
        )) {
            throw new RuntimeException("签名验证失败");
        }

        ParentCallBackDto parentCallBackDto = JSONObject.parseObject(reqBody, ParentCallBackDto.class);
        executor.execute(() -> {
            try {
                Object result = callbackApiConfig.execute(parentCallBackDto);
            } catch ( Exception e ) {
                log.error("执行回调接口失败", e);
            }
        });

        serverWebExchange.getResponse().getHeaders().add("X-Bot-Appid", botConfig.getAppId());
        serverWebExchange.getResponse().getHeaders().add("User-Agent", "QQBot-Callback");

        return Mono.just("成功接收");
    }
}
