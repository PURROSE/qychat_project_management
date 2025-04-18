package com.purplerosechen.qpm.service.cw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purplerosechen.qpm.database.model.SCw;
import com.purplerosechen.qpm.database.model.SCwTask;
import com.purplerosechen.qpm.database.service.SCwService;
import com.purplerosechen.qpm.database.service.SCwTaskService;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.BiFunction;

/**
 * @author chen
 * @version 1.0
 * @description: TODO
 * @date 18 4月 2025 09:38
 */

@Slf4j
@Service
public class CwServiceImpl {

    @Resource
    private SCwService scwService;
    @Resource
    private SCwTaskService sCwTaskService;

    /** 
     * @description: TODO 根据唯一键获取宠物的唯一ID 
     * @author chen
     * @date: 18 4月 2025 11:30
     */ 
    public SCw getCw(String groupId, String userOpenId) {
        return scwService.getOne(new QueryWrapper<SCw>().eq("auth_id", userOpenId).eq("group_id", groupId));
    }

    /** 
     * @description: TODO 添加宠物 
     * @author chen
     * @date: 18 4月 2025 11:30
     */ 
    public boolean addCw(String name,String groupOpenId,String userOpenId)  {
        SCw scw = new SCw();
        scw.setAuthId(userOpenId);
        scw.setGroupId(groupOpenId);
        scw.setCwName(name);
        scw.setCreateTime(new Date());

        return scwService.save(scw);
    }
    
    /**
     * @description: TODO 计算到下一等级所需要的经验 
     * @author chen
     * @date: 18 4月 2025 11:32
     */ 
    public long getNextLevelJy(long level) {
        //return (level + 1) * 100 * (level);
        return 100*level*level+100*level;
    }

    /**
     * @description: TODO 升级
     * @author chen
     * @date: 18 4月 2025 11:45
     */
    public boolean updateCwLevel(SCw scw) {
        // 更新宠物等级
        // 先判断宠物的经验够不够达到升级的标准
        if (scw.getJy() >= getNextLevelJy(scw.getDj())) {
            // 如果经验够升级
            scw.setDj(scw.getDj() + 1);
            scw.setJy(scw.getJy() - getNextLevelJy(scw.getDj()));
            // 等级的增加就要增加基础属性
            scw.setGj(scw.getGj() + 10);
            scw.setFy(scw.getFy() + 10);
            scw.setXl(scw.getXl() + 10);
            return scwService.updateById(scw);
        }
        return false;
    }

    /**
     * @description: TODO 增加经验
     * @author chen
     * @date: 18 4月 2025 11:46
     */
    public boolean updateCwJy(SCw scw,long jy) {
        scw.setJy(scw.getJy() + jy);
        return scwService.updateById(scw);
    }

    /**
     * @description: TODO 修改调整金钱数量
     * @author chen
     * @date: 18 4月 2025 11:49
     */
    public boolean addJq(SCw scw, long jq) {
        scw.setJq(scw.getJq() + jq);
        return scwService.updateById(scw);
    }

    public String cwWork(SCw scw, CwWorkType cwWorkType, Long workTime) {

        SCwTask swTask = new SCwTask();
        swTask.setCwId(scw.getCwId());
        swTask.setTaskType(cwWorkType.getType());
        swTask.setStartTime(new Date());
        // 开始时间增加workTime的小时时间
        swTask.setEndTime(new Date(swTask.getStartTime().getTime() + workTime * 3600 * 1000));

        // 第二个参数系数为保留系数
        Long my = cwWorkType.work(scw,workTime);
        if (addJq(scw,my)) {
            if (my > 0 ) {
                return "你成功" + cwWorkType.getName() + "了" + my + "金钱";
            } else {
                return "你失败" + cwWorkType.getName() + "了" + Math.abs(my) + "金钱";
            }
        }
        return "你失败" + cwWorkType.getName() + "了";
    }

    @Getter
    public enum CwWorkType {
        /*
         * 1、炒股
         * 2、打工
         * 3、拾荒
         * 4、卖屁股
         * 5、种地
         */
        CHAO_GUO(1,"炒股",(e,v)->{return stockTrading(e.getJq(), v);}),
        DANG_GONG(2,"打工",(e,v)->{return 1L;}),
        SHI_HUANG(3,"拾荒",(e,v)->{return 1L;}),
        MAI_PAI_DI(4,"卖屁股",(e,v)->{return 1L;}),
        ZHONG_DI(5,"种地",(e,v)->{return 1L;});

        private final Integer type;
        private final String name;
        private final BiFunction<SCw,Long,Long> func;

        CwWorkType(Integer type, String name, BiFunction<SCw, Long, Long> func) {
            this.type = type;
            this.name = name;
            this.func = func;
        }

        public Long work(SCw scw,Long workTime) {
            return func.apply(scw,workTime);
        }

        /** 
         * @description: TODO 炒股赚钱 
         * @author chen
         * @date: 18 4月 2025 16:05
         */
        private static long stockTrading(Long je, Long time) {
            // 生成随机数
            long jq = 0L;
            for ( int i = 0; i < time; i++ ) {
                long random = (long) (Math.random() * 100);
                if (random < 50) {
                    // 随机数小于50，收益是增加的
                    jq += (long) (Math.random() / 100 * je);
                } else {
                    // 随机数大于等于50，收益是减少的
                    jq += -(long) (Math.random() / 100 * je);
                }
            }
            return jq;
        }


    }
}
