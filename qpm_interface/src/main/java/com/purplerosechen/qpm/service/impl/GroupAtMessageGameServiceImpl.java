package com.purplerosechen.qpm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purplerosechen.qpm.database.model.SGame;
import com.purplerosechen.qpm.database.service.SGameService;
import com.purplerosechen.qpm.service.GroupAtMessageTypeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chen
 * @version 1.0
 * @description: TODO 群聊@机器人游戏服务
 * @date 17 4月 2025 10:28
 */
@Slf4j
@Service()
public class GroupAtMessageGameServiceImpl implements GroupAtMessageTypeService {

    @Resource
    private SGameService sGameService;

    @Override
    public Object execute(String groupOpenid, String userOpenId, Object obj) throws Exception {
        long maxLine = 5;
        List<SGame> sGameList = sGameService.list(new QueryWrapper<SGame>().like("g_name", obj));
        if (sGameList.isEmpty()) {
            return "未找到【"+obj+"】游戏！";
        }
        // .replaceAll("\\.",",")
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("检索到【").append(sGameList.size()).append("】游戏列表：");
        for (SGame sGame : sGameList) {
            maxLine--;
            stringBuilder.append("\n").append(sGame.getId()).append("\t");
            stringBuilder.append("【").append(sGame.getGName()).append("】\t");
            stringBuilder.append("游戏介绍：").append(sGame.getGNote()).append("\t");
            stringBuilder.append("游戏地址：").append(sGame.getGUrl().replaceAll("\\.",",")).append("\t");
            stringBuilder.append("游戏密码：").append(sGame.getGPwd()).append("\t");
            stringBuilder.append("其他地址：").append(sGame.getGOther().replaceAll("\\.",","));
            if (maxLine <= 0) {
                stringBuilder.append("\n...请输入更精确名称以便提供更准确结果");
                break;
            }
        }
        log.info("【{}】游戏列表：{}", obj, stringBuilder);
        return stringBuilder.toString();
    }
}
