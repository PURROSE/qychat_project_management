package com.purplerosechen.qpm.service.impl.cw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purplerosechen.qpm.database.model.SCw;
import com.purplerosechen.qpm.database.service.SCwService;
import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
import com.purplerosechen.qpm.service.cw.CwServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 处理群里@宠物信息
 * @date 17 4月 2025 16:37
 */

@Slf4j
@Service
public class CwBaseInfoServiceImpl implements GroupAtMessageTypeService {

    @Resource
    private CwServiceImpl cwService;

    @Override
    public Object execute(String groupOpenId, String userOpenId, Object obj) throws Exception {
        // 入参是宠物名称
        // 每个群用户只能有一只宠物
        SCw scw = cwService.getCw(groupOpenId, userOpenId);
        if (scw == null) {
            if (((String) obj).isEmpty()) {
                return "请输入宠物名称！";
            }
            if (!cwService.addCw((String) obj, groupOpenId, userOpenId)) {
                return "宠物添加失败！";
            } else {
                // 添加成功后重新查询下获取宠物的信息
                scw = cwService.getCw(groupOpenId, userOpenId);
            }
        }

        // 这里拼接宠物信息，返回
        String cwMsg = "您的宠物【" + scw.getCwName() + "】\n" +
                "等级:" + scw.getDj() + "\n" +
                "经验:" + scw.getJy() + "/" + cwService.getNextLevelJy(scw.getDj()) + "\n" +
                "金钱:" + scw.getJq() + "\n" +
                "攻击:" + scw.getGj() + ";防御:" + scw.getFy() + ";血量:" + scw.getXl();
        return cwMsg;
    }
}
