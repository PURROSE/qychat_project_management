package com.purplerosechen.qpm.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.dto.AllCallBackDto;
import com.purplerosechen.qpm.dto.CallBackTestDto;
import com.purplerosechen.qpm.service.CallBackService;
import com.purplerosechen.qpm.tools.BotConfig;
import com.purplerosechen.qpm.tools.enc.CallBackSignUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 测试回调
 * @date 15 4月 2025 14:47
 */
@Slf4j
@Service
public class TestCallBackServiceImpl implements CallBackService {

    @Resource
    private BotConfig botConfig;

    @Override
    public Object execute(AllCallBackDto request) throws Exception {
        CallBackTestDto callBackTestDto = (CallBackTestDto) request;

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
        return jsonResponse;
    }
}
