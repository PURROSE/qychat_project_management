package com.purplerosechen.qpm.config;

import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
import com.purplerosechen.qpm.service.exception.NotFoundAtMessageTypeException;
import com.purplerosechen.qpm.service.impl.GroupAtMessageWeatherServiceImpl;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 16 4月 2025 11:55
 */

@Component
@Slf4j
public class AtMessageTypeConfig {
    @Resource
    private ApplicationContext applicationContext;

    private final HashMap<String, AtMessageEnum> atMessageEnumHashMap;

    public Object execute(String type, Object obj) throws Exception {
        AtMessageEnum atMessageEnum = atMessageEnumHashMap.get(type);
        if (atMessageEnum == null) {
            throw new NotFoundAtMessageTypeException();
        }

        GroupAtMessageTypeService groupAtMessageTypeService = (GroupAtMessageTypeService) applicationContext.getBean(atMessageEnum.getExecClass());
        return groupAtMessageTypeService.execute(obj);
    }

    public AtMessageTypeConfig() {
        atMessageEnumHashMap = new HashMap<>();
        for (AtMessageEnum atMessageEnum : AtMessageEnum.values()) {
            atMessageEnumHashMap.put(atMessageEnum.getType(), atMessageEnum);
        }
    }

    @Getter
    public enum AtMessageEnum{

        WEATHER("/天气", GroupAtMessageWeatherServiceImpl.class),

        ;

        private final String type;
        private final Class<?> execClass;

        AtMessageEnum(String type, Class<?> execClass) {
            this.type = type;
            this.execClass = execClass;
        }
    }

}
