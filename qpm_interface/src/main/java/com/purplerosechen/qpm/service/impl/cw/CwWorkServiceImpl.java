package com.purplerosechen.qpm.service.impl.cw;

import com.purplerosechen.qpm.database.model.SCw;
import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
import com.purplerosechen.qpm.service.cw.CwServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 10605
 * @version 1.0
 * @description: TODO 宠物打工
 * @date 19 4月 2025 00:34
 */
@Slf4j
@Service
public class CwWorkServiceImpl implements GroupAtMessageTypeService {
    @Resource
    private CwServiceImpl cwService;
    @Override
    public Object execute(String groupOpenId, String userOpenId, Object obj) throws Exception {
        SCw scw = cwService.getCw(groupOpenId, userOpenId);
        if (scw == null) {
            return "您还没有创建宠物";
        }
        try {
            String body = (String) obj;
            String[] split = body.split(" ");
            if (split.length != 2) {
                throw new Exception("参数错误！请使用/工作 {工作类型：打工，炒股，拾荒，卖屁股，种地} {小时数}为格式发送！");
            }
            String time = split[1];
            String type = split[0];

            log.info("cwWork:type:{}, time:{}", type, time);
            // 第0位是打工类型
            CwServiceImpl.CwWorkType cwWorkType = CwServiceImpl.CwWorkType.getCwWorkType(type);
            if (cwWorkType == null) {
                return "未找到该工作！";
            }
            long workTime = Long.parseUnsignedLong(time);
            return cwService.cwWork(scw, cwWorkType, workTime);
        } catch ( Exception exception ) {
            return "参数错误！请使用/工作 {工作类型：打工，炒股，拾荒，卖屁股，种地} {小时数}为格式发送！";
        }
    }
}
