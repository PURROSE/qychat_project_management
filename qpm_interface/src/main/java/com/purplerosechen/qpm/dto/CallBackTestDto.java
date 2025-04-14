package com.purplerosechen.qpm.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 回调测试数据
 * @date 14 4月 2025 14:57
 */
@Data
public class CallBackTestDto {
    @JSONField(name = "plain_token")
    private String plainToken;
    @JSONField(name = "event_ts")
    private String eventTs;
}
