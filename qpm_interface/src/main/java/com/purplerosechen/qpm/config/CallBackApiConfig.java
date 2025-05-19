package com.purplerosechen.qpm.config;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.dto.*;
import com.purplerosechen.qpm.service.*;
import com.purplerosechen.qpm.service.impl.AckCallBackServiceImpl;
import com.purplerosechen.qpm.service.impl.GroupAtMessageCreateCallBackServiceImpl;
import com.purplerosechen.qpm.service.impl.TestCallBackServiceImpl;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 回调接口配置
 * @date 15 4月 2025 14:48
 */

@Component
@Slf4j
public class CallBackApiConfig {
    @Resource
    private ApplicationContext applicationContext;
    private final HashMap<Integer, CallBackApiModule> callBackApiModuleHashMap;
    private final HashMap<String, DispatchCallBackModule> dispatchCallBackModuleHashMap;


    public Object execute(ParentCallBackDto parentCallBackDto) throws Exception {
        if (parentCallBackDto.getOp() != 0) {
            CallBackApiModule callBackApiModule = callBackApiModuleHashMap.get(parentCallBackDto.getOp());
            CallBackService callBackService = (CallBackService) applicationContext.getBean(callBackApiModule.getCallBackService());
            Object allCallBackDto = parentCallBackDto.getD().to(callBackApiModule.requestClass);
            return callBackService.execute((AllCallBackDto) allCallBackDto);
        } else {
            if (Strings.isEmpty(parentCallBackDto.getT())) {
                throw new RuntimeException("回调类型处理异常");
            }

            DispatchCallBackModule dispatchCallBackModule = dispatchCallBackModuleHashMap.get(parentCallBackDto.getT());
            DispatchCallBackTypeService dispatchCallBackService = (DispatchCallBackTypeService) applicationContext.getBean(dispatchCallBackModule.getDispatchCallBackService());
            Object requestModel = parentCallBackDto.getD().to(dispatchCallBackModule.getRequestClass());

            return dispatchCallBackService.execute((AllCallBackDto) requestModel);
        }

    }


    public CallBackApiConfig() {
        callBackApiModuleHashMap = new HashMap<>(CallBackApiModule.values().length);
        // 遍历CallBackApiModule枚举
        for (CallBackApiModule callBackApiModule : CallBackApiModule.values()) {
            // 创建CallBackApiModule对象，并添加到HashMap中
            callBackApiModuleHashMap.put(callBackApiModule.getCode(), callBackApiModule);
        }

        dispatchCallBackModuleHashMap = new HashMap<>(DispatchCallBackModule.values().length);
        for (DispatchCallBackModule dispatchCallBackModule : DispatchCallBackModule.values()) {
            dispatchCallBackModuleHashMap.put(dispatchCallBackModule.getT(), dispatchCallBackModule);
        }
    }

    @Getter
    public enum CallBackApiModule {

        TEST("回调地址验证",13, TestCallBackServiceImpl.class, "开放平台对机器人服务端进行验证", CallBackTestDto.class),
        ACK("HTTP Callback ACK",  12, AckCallBackServiceImpl.class, "仅用于 http 回调模式的回包，代表机器人收到了平台推送的数据", JSONObject.class),
        Dispatch("Dispatch",0, DispatchCallBackTypeService.class, "服务端进行消息推送", JSONObject.class),

        ;

        private final Integer code;
        private final String name;
        private final Class<?> callBackService;
        private final Class<?> requestClass;
        private final String note;


        CallBackApiModule(String name, Integer code, Class<?> callBackService, String note, Class<?> requestClass) {
            this.code = code;
            this.callBackService = callBackService;
            this.note = note;
            this.name = name;
            this.requestClass = requestClass;
        }
    }

    @Getter
    private enum DispatchCallBackModule{

        GROUP_AT_MESSAGE_CREATE(
                "GROUP_AT_MESSAGE_CREATE",
                "用户在群聊@机器人发送消息",
                GroupAtMessageCreateCallBackServiceImpl.class,
                GroupAtMessageCreateDto.class
        ),

        ;

        private final String t;
        private final String name;
        private final Class<?> dispatchCallBackService;
        private final Class<?> requestClass;

        DispatchCallBackModule(String t, String name, Class<?> dispatchCallBackService, Class<?> requestClass){
            this.t = t;
            this.name = name;
            this.dispatchCallBackService = dispatchCallBackService;
            this.requestClass = requestClass;
        }
    }
}
