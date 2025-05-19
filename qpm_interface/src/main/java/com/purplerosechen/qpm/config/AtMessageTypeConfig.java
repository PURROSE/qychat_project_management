package com.purplerosechen.qpm.config;

import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
import com.purplerosechen.qpm.service.exception.NotFoundAtMessageTypeException;
import com.purplerosechen.qpm.service.impl.GroupAtMessageGameServiceImpl;
import com.purplerosechen.qpm.service.impl.GroupAtMessageWeatherServiceImpl;
import com.purplerosechen.qpm.service.impl.cw.CwBaseInfoServiceImpl;
import com.purplerosechen.qpm.service.impl.cw.CwWorkServiceImpl;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.Executor;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 对于群@
 * @date 16 4月 2025 11:55
 */

@Component
@Slf4j
public class AtMessageTypeConfig {
    @Resource
    private ApplicationContext applicationContext;


    private final HashMap<String, AtMessageEnum> atMessageEnumHashMap;

    public Object execute(String type, Object obj,String groupOpenId,String userOpenId) throws Exception {
        AtMessageEnum atMessageEnum = atMessageEnumHashMap.get(type);
        if (atMessageEnum == null) {
            throw new NotFoundAtMessageTypeException();
        }

        GroupAtMessageTypeService groupAtMessageTypeService = (GroupAtMessageTypeService) applicationContext.getBean(atMessageEnum.getExecClass());
        return groupAtMessageTypeService.execute(groupOpenId, userOpenId,obj);
    }

    public AtMessageTypeConfig() {
        atMessageEnumHashMap = new HashMap<>();
        for (AtMessageEnum atMessageEnum : AtMessageEnum.values()) {
            atMessageEnumHashMap.put(atMessageEnum.getType(), atMessageEnum);
        }
    }

    /**
     * @description: TODO 返回消息类型是否异步的状态
     * @author chen
     * @date: 19 5月 2025 15:56
     */
    public boolean isAsync(String type) {
        return atMessageEnumHashMap.get(type).isAsync();
    }

    @Getter
    public enum AtMessageEnum{

        WEATHER("/天气", GroupAtMessageWeatherServiceImpl.class, true),
        GAME("/游戏", GroupAtMessageGameServiceImpl.class, true),
        CW_BASE_INFO("/宠物信息", CwBaseInfoServiceImpl.class, false),
        WORK("/工作", CwWorkServiceImpl.class, false)
        ;

        private final String type;
        private final Class<?> execClass;
        private final boolean isAsync;

        AtMessageEnum(String type, Class<?> execClass, boolean isAsync) {
            this.type = type;
            this.execClass = execClass;
            this.isAsync = isAsync;
        }
    }

}
