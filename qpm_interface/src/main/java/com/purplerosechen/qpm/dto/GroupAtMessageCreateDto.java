package com.purplerosechen.qpm.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 群聊@消息回调实体类
 * @date 15 4月 2025 15:36
 */

@NoArgsConstructor
@Data
public class GroupAtMessageCreateDto extends AllCallBackDto{

    @JSONField(name = "author")
    private AuthorDTO author;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "group_openid")
    private String groupOpenid;
    @JSONField(name = "id")
    private String id;
    @JSONField(name = "timestamp")
    private String timestamp;

    @NoArgsConstructor
    @Data
    public static class AuthorDTO {
        @JSONField(name = "member_openid")
        private String memberOpenid;
    }
}
