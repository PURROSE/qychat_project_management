package com.purplerosechen.qpm.pojo.gd;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 16 4月 2025 11:24
 */
@Data
public class GdLifeResPojo extends GdResPojo {
    private List<GdLifeMxResPojo> lives;

    @Data
    public static class GdLifeMxResPojo {
        private String province;
        private String city;
        private String adcode;
        private String temperatureFloat;
        private String humidityFloat;
        private String windpower;
        private String weather;
        private String temperature;
        private String humidity;
        private OffsetDateTime reporttime;
        private String winddirection;
    }
}


