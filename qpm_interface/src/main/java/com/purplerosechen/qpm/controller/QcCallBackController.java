package com.purplerosechen.qpm.controller;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 回调接口
 * @date 11 4月 2025 16:30
 */

@RestController
@RequestMapping("/mqb/call-back")
public class QcCallBackController {

    @RequestMapping("/test")
    public String testCallBack(
            @RequestBody String reqBody,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        request.getHeaders().forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        System.out.println(reqBody);
        return "";
    }
}
