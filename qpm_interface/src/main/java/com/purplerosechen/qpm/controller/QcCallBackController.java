package com.purplerosechen.qpm.controller;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.dto.CallBackTestDto;
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


    @RequestMapping("/test")
    public Mono<Object> testCallBack(
            @RequestBody String reqBody,
            @RequestHeader("X-Signature-Ed25519") String xSignatureEd25519,
            @RequestHeader("X-Signature-Timestamp") String xSignatureTimestamp,
            ServerWebExchange serverWebExchange
            ) throws Exception {

        log.info("xSignatureEd25519:{}", xSignatureEd25519);
        log.info("xSignatureTimestamp:{}", xSignatureTimestamp);
        log.info("reqBody:{}", reqBody);

//        if (!CallBackSignUtil.verifySignature(
//                botConfig.getAppSecret(),
//                xSignatureEd25519,
//                xSignatureTimestamp + reqBody
//        )) {
//            throw new RuntimeException("签名验证失败");
//        }

        ParentCallBackDto parentCallBackDto = JSONObject.parseObject(reqBody, ParentCallBackDto.class);
        CallBackTestDto callBackTestDto = parentCallBackDto.getD().to(CallBackTestDto.class);
        log.info("callBackTestDto:{}", callBackTestDto);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("plain_token", callBackTestDto.getPlainToken());
        jsonResponse.put(
                "signature",
                CallBackSignUtil.generateResponse(
                        botConfig.getAppSecret(),
                        callBackTestDto.getEventTs(),
                        callBackTestDto.getPlainToken()
                )
        );

        serverWebExchange.getResponse().getHeaders().add("X-Bot-Appid", botConfig.getAppId());
        serverWebExchange.getResponse().getHeaders().add("User-Agent", "QQBot-Callback");

        return Mono.just(jsonResponse);
    }
}
