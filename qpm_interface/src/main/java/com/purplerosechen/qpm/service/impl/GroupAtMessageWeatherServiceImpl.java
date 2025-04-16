package com.purplerosechen.qpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purplerosechen.qpm.database.mapper.SCityMapper;
import com.purplerosechen.qpm.database.model.SCity;
import com.purplerosechen.qpm.pojo.gd.GdLifeResPojo;
import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
import com.purplerosechen.qpm.service.gd.GdApiService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 查询天气对应处理办法
 * @date 16 4月 2025 11:57
 */
@Slf4j
@Service
public class GroupAtMessageWeatherServiceImpl implements GroupAtMessageTypeService {

    @Resource
    private GdApiService gdapiService;
    @Resource
    private SCityMapper scoreMapper;

    @Override
    public Object execute(Object obj) throws Exception {
        // 根据传入的城市名称查询数据库表，获取到对应的城市代码
        obj = ((String) obj).replaceAll("[\n\r]","");

        List<SCity> sCityList = scoreMapper.selectList(new QueryWrapper<SCity>().like("city_name", obj));
        if (sCityList.isEmpty()) {
            return "未找到【"+obj+"】城市！";
        }
        List<?> gdLifeMxResPojoList = gdapiService.getWeather(sCityList.getFirst().getCityCode());
        if (gdLifeMxResPojoList.isEmpty()) {
            return "未找到【"+obj+"】天气！";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object item : (List<?>) gdLifeMxResPojoList) {
            GdLifeResPojo.GdLifeMxResPojo gdLifeMxResPojo = (GdLifeResPojo.GdLifeMxResPojo) item;
            stringBuilder.append("\n").append(gdLifeMxResPojo.getProvince()).append(gdLifeMxResPojo.getCity());
            stringBuilder.append("\t天气:").append(gdLifeMxResPojo.getWeather());
            stringBuilder.append("\t气温:").append(gdLifeMxResPojo.getTemperature());
            stringBuilder.append("\t风力级别:").append(gdLifeMxResPojo.getWindpower());
            stringBuilder.append("\t空气湿度:").append(gdLifeMxResPojo.getHumidity());
        }
        return stringBuilder.toString();
    }
}
