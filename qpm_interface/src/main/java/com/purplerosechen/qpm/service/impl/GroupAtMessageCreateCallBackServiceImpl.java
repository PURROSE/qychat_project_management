package com.purplerosechen.qpm.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.config.AtMessageTypeConfig;
import com.purplerosechen.qpm.dto.AllCallBackDto;
import com.purplerosechen.qpm.dto.GroupAtMessageCreateDto;
import com.purplerosechen.qpm.pojo.gd.GdLifeResPojo;
import com.purplerosechen.qpm.pojo.qq.GroupMessageReqPojo;
import com.purplerosechen.qpm.service.DispatchCallBackTypeService;
import com.purplerosechen.qpm.service.exception.NotFoundAtMessageTypeException;
import com.purplerosechen.qpm.service.exception.NotFoundCityException;
import com.purplerosechen.qpm.tools.http.ApiHttpUrlEnum;
import com.purplerosechen.qpm.tools.http.BotSendHttp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 处理群聊里面@机器人的操作
 * @date 15 4月 2025 15:01
 */

@Slf4j
@Service
public class GroupAtMessageCreateCallBackServiceImpl implements DispatchCallBackTypeService {

    @Resource
    private BotSendHttp botSendHttp;

    @Resource
    private AtMessageTypeConfig atMessageTypeConfig;


    @Override
    public Object execute(AllCallBackDto request) {
        GroupAtMessageCreateDto groupAtMessageCreateDto = (GroupAtMessageCreateDto) request;

        log.info("群聊里面@机器人:{}", groupAtMessageCreateDto);
        String[] content = groupAtMessageCreateDto.getContent().split(" ");
        if (content.length<3) {
            sendGroupMessage(groupAtMessageCreateDto.getId(), groupAtMessageCreateDto.getGroupOpenid(), "请输入正确的指令");
            return new JSONObject();
        }
        try{
            log.info("群聊里面@机器人:{},{}", content[1], content[2]);
            Object resMsg  = atMessageTypeConfig.execute(content[1], content[2]);
            if (resMsg instanceof String) {
                sendGroupMessage(groupAtMessageCreateDto.getId(), groupAtMessageCreateDto.getGroupOpenid(), (String) resMsg);
            }
        }catch ( NotFoundAtMessageTypeException exception ) {
            sendGroupMessage(groupAtMessageCreateDto.getId(), groupAtMessageCreateDto.getGroupOpenid(), "暂不支持该功能");
        }
        catch ( Exception e ) {
            throw new RuntimeException(e);
        }

        return new JSONObject();
    }

    /**
     * @description: TODO 发送群消息
     * @author chen
     * @date: 16 4月 2025 11:06
     */
    private void sendGroupMessage(String groupMsgId,String groupId, String message) {
        GroupMessageReqPojo groupMessageReqPojo = new GroupMessageReqPojo();
        groupMessageReqPojo.setContent(message);
        groupMessageReqPojo.setMsgType(0);
        groupMessageReqPojo.setMsgId(groupMsgId);
        groupMessageReqPojo.setMsgSeq("1");

        try {
            Mono<String> res = botSendHttp.post(groupMessageReqPojo, String.format(ApiHttpUrlEnum.GROUP_AT_MESSAGE_CREATE_CALL_BACK.getUrl(), groupId));
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
    }
}
