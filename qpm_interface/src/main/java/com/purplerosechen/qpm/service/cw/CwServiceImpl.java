package com.purplerosechen.qpm.service.cw;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.purplerosechen.qpm.database.model.SCw;
import com.purplerosechen.qpm.database.model.SCwTask;
import com.purplerosechen.qpm.database.service.SCwService;
import com.purplerosechen.qpm.database.service.SCwTaskService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
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

        // 首先要判断当前时间有没有任务在执行
        List<SCwTask> sCwTaskList = sCwTaskService.list(new QueryWrapper<SCwTask>().eq("cw_id", scw.getCwId()).gt("end_time", new Date()));

        if (!sCwTaskList.isEmpty()) {
            return "你正在" + Objects.requireNonNull(CwWorkType.getCwWorkType(sCwTaskList.getFirst().getTaskType())).getName() + "中，预计"+ sCwTaskList.getFirst().getEndTime() + "结束，请稍后再试";
        }
        SCwTask swTask = new SCwTask();
        swTask.setCwId(scw.getCwId());
        swTask.setTaskType(cwWorkType.getType());
        swTask.setStartTime(new Date());
        // 开始时间增加workTime的小时时间
        swTask.setEndTime(new Date(swTask.getStartTime().getTime() + workTime * 3600 * 1000));
        sCwTaskService.save(swTask);

        // 第二个参数系数为保留系数
        Long my = cwWorkType.work(scw,workTime);
        if (addJq(scw,my)) {
            if (my > 0 ) {
                return scw.getCwName() + cwWorkType.getName() + "成功，收入" + my + "金钱，钱包余额："+scw.getJq();
            } else {
                return scw.getCwName() + cwWorkType.getName() + "失败了，赔钱" + Math.abs(my) + "金钱，钱包余额："+scw.getJq();
            }
        }
        return "你" + cwWorkType.getName() + "失败了";
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
        CHAO_GUO(1,"炒股", CwWorkType :: stockTrading),
        DANG_GONG(2,"打工", CwWorkType :: workForOther),
        SHI_HUANG(3,"拾荒", CwWorkType :: workForSh),
        MAI_PAI_DI(4,"卖屁股",CwWorkType :: safAs),
        ZHONG_DI(5,"种地",CwWorkType :: workForTd);

        private final Integer type;
        private final String name;
        private final BiFunction<SCw,Long,Long> func;

        CwWorkType(Integer type, String name, BiFunction<SCw, Long, Long> func) {
            this.type = type;
            this.name = name;
            this.func = func;
        }

        /** 
         * @description: TODO 根据ID号返回宠物工作枚举
         * @author chen
         * @date: 15 5月 2025 11:40
         */ 
        public static CwWorkType getCwWorkType(Integer type) {
            for (CwWorkType cwWorkType : CwWorkType.values()) {
                if (cwWorkType.getType().equals(type)) {
                    return cwWorkType;
                }
            }
            return null;
        }

        /** 
         * @description: TODO 根据名称返回宠物工作类型的枚举 
         * @author chen
         * @date: 15 5月 2025 09:45
         */ 
        public static CwWorkType getCwWorkType(String name) {
            for (CwWorkType cwWorkType : CwWorkType.values()) {
                if (cwWorkType.getName().equals(name)) {
                    return cwWorkType;
                }
            }
            return null;
        }

        public Long work(SCw scw,Long workTime) {
            return func.apply(scw,workTime);
        }

        /** 
         * @description: TODO 炒股赚钱 
         * @author chen
         * @date: 18 4月 2025 16:05
         */
        private static long stockTrading(SCw scw, Long time) {
            // 生成随机数
            // 这里入参的是本金,只能使用本金的32%用于炒股
            long jq = 0L;
            long je = (long) (scw.getJq() * 0.32);
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

        /** 
         * @description: TODO 打工收入 
         * @author 10605
         * @date: 18 4月 2025 23:54
         */ 
        private static long workForOther(SCw scw, Long time) {
            // 打工是稳定的收入，收入和等级挂钩

            long jq = 0L;
            long random = 0;
            for ( int i = 0; i < time; i++ ) {
                random = (long) (Math.random() * 100);
                jq += (random * scw.getDj());
            }

            return jq;
        }

        /**
         * @description: TODO 拾荒收入
         * @author 10605
         * @date: 18 4月 2025 23:59
         */
        private static long workForSh(SCw scw, Long time) {
            // 拾荒是收益最低的类型，但是时间加成高，时间加成*10倍
            long jq = 0L;
            time = time * 2;
            long random = 0;
            for ( int i = 0; i < time; i++ ) {
                random = (long) (Math.random() * 10);
                jq += random;
            }
            return jq;
        }

        /**
         * @description: TODO 卖屁股
         * @author 10605
         * @date: 19 4月 2025 00:16
         */
        private static long safAs(SCw scw, Long time) {
            // 根据等级卖屁股
            // 卖屁股需要查找可以购买的宠物，这里的time就不是时间了，而是购买者的钱
            long jq = 0L;
            long random = (long) (Math.random() * 200);
            jq = random * scw.getDj();
            // 如果支付的钱不够，就只能把time的钱全部给出去。
            if (jq >= time) {
                jq = time;
            }
            return jq;
        }

        /**
         * @description: TODO 种地
         * @author 10605
         * @date: 19 4月 2025 00:16
         */
        private static long workForTd(SCw scw, Long time) {
            // 种地是一种比拾荒高，但是时间投入成本高的收入
            long jq = 0L;
            time = (long) (time * 0.2);
            long random = 0;
            for ( int i = 0; i < time; i++ ) {
                random = (long) (Math.random() * 100);
                jq += random;
            }
            return jq;
        }

    }
}
