package com.purplerosechen.qpm.dto;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 14 4月 2025 14:57
 */
@Data
public class ParentCallBackDto {
    @JSONField(name = "d")
    private JSONObject d;
    @JSONField(name = "op")
    private String op;
}
