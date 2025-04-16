package com.purplerosechen.qpm.service.gd.impl;

import com.alibaba.fastjson2.JSONObject;
import com.purplerosechen.qpm.pojo.gd.GdLifeResPojo;
import com.purplerosechen.qpm.pojo.gd.GdResPojo;
import com.purplerosechen.qpm.pojo.gd.ResStatus;
import com.purplerosechen.qpm.service.gd.GdApiService;
import com.purplerosechen.qpm.tools.BotConfig;
import com.purplerosechen.qpm.tools.http.ApiHttpUrlEnum;
import com.purplerosechen.qpm.tools.http.BotSendHttp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 高德地图
 * @date 16 4月 2025 11:18
 */

@Slf4j
@Service()
public class GdApiServiceImpl implements GdApiService {

    @Resource
    private BotSendHttp botSendHttp;
    @Resource
    private BotConfig botConfig;

    @Override
    public List<GdLifeResPojo.GdLifeMxResPojo> getWeather(String cityCode) {
        JSONObject getWeather = new JSONObject();
        getWeather.put("key", botConfig.getGdKey());
        getWeather.put("city", cityCode);
        getWeather.put("extensions", "base");
        getWeather.put("output", "JSON");
        Mono<String> weatherRes = botSendHttp.otherGet(getWeather, ApiHttpUrlEnum.GET_WEATHER.getUrl());
        GdLifeResPojo weatherJson = JSONObject.parseObject(weatherRes.block(), GdLifeResPojo.class);
        log.debug("获取天气成功:{}", weatherJson);
        if (weatherJson.getStatus().equals(ResStatus.OK.getStatus()) && weatherJson.getInfocode().equals(ResStatus.INFO_SUCCESS.getStatus())) {
            return weatherJson.getLives();
        }else {
            log.info("获取天气失败:{}", weatherJson.getInfo());
            return new ArrayList<GdLifeResPojo.GdLifeMxResPojo>();
        }
    }
}
