package com.purplerosechen.qpm.service.impl.cw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purplerosechen.qpm.database.model.SCw;
import com.purplerosechen.qpm.database.service.SCwService;
import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
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
    private SCwService scwService;

    @Override
    public Object execute(String groupOpenId, String userOpenId, Object obj) throws Exception {
        log.info("处理群里@宠物信息");
        // 入参是宠物名称
        // 每个群用户只能有一只宠物
        SCw scw = scwService.getOne(new QueryWrapper<SCw>().eq("auth_id", userOpenId).eq("group_id", groupOpenId));
        if (scw == null) {
            if (((String) obj).isEmpty()) {
                return "请输入宠物名称！";
            }
            if (!addCw((String) obj, groupOpenId, userOpenId)) {
                return "宠物添加失败！";
            } else {
                scw = scwService.getOne(new QueryWrapper<SCw>().eq("auth_id", userOpenId).eq("group_id", groupOpenId));
            }
        }

        // 这里拼接宠物信息，返回
        StringBuilder cwMsg = new StringBuilder();
        cwMsg.append("您的宠物【").append(scw.getCwName()).append("】\n");
        cwMsg.append("等级:").append(scw.getDj()).append("\n");
        cwMsg.append("经验:").append(scw.getJy()).append("/").append((scw.getDj()+1) * 2 * 100).append("\n");
        cwMsg.append("金钱:").append(scw.getJq()).append("\n");
        cwMsg.append("攻击:").append(scw.getGj()).append(";防御:").append(scw.getFy()).append(";血量:").append(scw.getXl());
        return cwMsg.toString();
    }

    private boolean addCw(String name,String groupOpenId,String userOpenId)  {
        SCw scw = new SCw();
        scw.setAuthId(userOpenId);
        scw.setGroupId(groupOpenId);
        scw.setCwName(name);
        scw.setCreateTime(new Date());

        return scwService.save(scw);
    }
}
