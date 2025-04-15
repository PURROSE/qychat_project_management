package com.purplerosechen.qpm.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 15 4月 2025 16:36
 */
@Data
public class GroupMessageReqPojo {
    private String content;
    @JSONField(name = "msg_type")
    private Integer msgType;
    private Object markdown;
    private Object keyboard;
    private Object media;
    private Object ark;
    @JSONField(name = "event_id")
    private String eventId;
    @JSONField(name = "msg_id")
    private String msgId;
    @JSONField(name = "msg_seq")
    private String msgSeq;

}
