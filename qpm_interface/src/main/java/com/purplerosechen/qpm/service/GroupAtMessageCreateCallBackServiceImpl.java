package com.purplerosechen.qpm.service;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.dto.AllCallBackDto;
import com.purplerosechen.qpm.dto.GroupAtMessageCreateDto;
import com.purplerosechen.qpm.pojo.GroupMessageReqPojo;
import com.purplerosechen.qpm.tools.http.BotSendHttp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 处理群聊里面@机器人的操作
 * @date 15 4月 2025 15:01
 */

@Slf4j
@Service
public class GroupAtMessageCreateCallBackServiceImpl implements DispatchCallBackTypeService{

    @Resource
    private BotSendHttp botSendHttp;

    @Override
    public Object execute(AllCallBackDto request) {
        GroupAtMessageCreateDto groupAtMessageCreateDto = (GroupAtMessageCreateDto) request;
        log.info("groupAtMessageCreateDto:{}", groupAtMessageCreateDto);
        GroupMessageReqPojo groupMessageReqPojo = new GroupMessageReqPojo();
        groupMessageReqPojo.setContent("你好呀");
        groupMessageReqPojo.setMsgType(0);
        groupMessageReqPojo.setMsgId(groupAtMessageCreateDto.getId());
        groupMessageReqPojo.setMsgSeq("1");

        try {
            Mono<String> res = botSendHttp.post(groupMessageReqPojo, String.format("/v2/groups/%s/messages", groupAtMessageCreateDto.getGroupOpenid()));
            res.subscribe(
                    s -> {
                        log.info("发送群聊消息成功:{}", s);
                    },
                    e -> {
                        log.error("发送群聊消息失败", e);
                    }
            );
        } catch (Exception e) {
            log.error("发送群聊消息失败", e);
        }

        return new JSONObject();
    }

}
