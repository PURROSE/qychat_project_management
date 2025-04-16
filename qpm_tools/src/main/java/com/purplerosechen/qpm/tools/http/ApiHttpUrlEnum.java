package com.purplerosechen.qpm.tools.http;

import lombok.Getter;

/**
 * @description: TODO
 * @author: chen
 * @date: 2025/04/16
 **/
@Getter
public enum ApiHttpUrlEnum {

    GROUP_AT_MESSAGE_CREATE_CALL_BACK("/v2/groups/%s/messages", "群发送消息（%s为替换群ID）"),
    GET_ACC_TOKENS("https://bots.qq.com/app/getAppAccessToken", "获取access_token"),
    GET_WEATHER("https://restapi.amap.com/v3/weather/weatherInfo", "高德地图获取天气")
    ;

    private final String url;
    private final String name;

    ApiHttpUrlEnum(String url, String name) {
        this.url = url;
        this.name = name;
    }

}
