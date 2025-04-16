package com.purplerosechen.qpm.service.gd;

import com.purplerosechen.qpm.pojo.gd.GdLifeResPojo;
import com.purplerosechen.qpm.pojo.gd.GdResPojo;

import java.util.List;

/**
 * @description: TODO 高德API调用服务
 * @author: chen
 * @date: 2025/04/16
 **/

public interface GdApiService {
    List<GdLifeResPojo.GdLifeMxResPojo> getWeather(String cityCode);
}
