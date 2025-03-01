package com.cheng.trigger.job;

import com.cheng.domain.strategy.model.vo.StrategyAwardStockKeyVo;
import com.cheng.domain.strategy.service.IRaffleStock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 更新奖品库存任务:采用了redis更新缓存库存，异步队列更新数据库
 * @date 2024/12/13 14:26
 */

@Slf4j
@Component
public class UpdateAwardStockJob {

    @Resource
    private IRaffleStock raffleStock;

    @Scheduled(cron = "0/5 * * * * ?")
    public void exec(){
        try {
            log.info("定时任务，更新库存");
            StrategyAwardStockKeyVo strategyAwardStockKeyVo = raffleStock.takeQueryValue();
            if (strategyAwardStockKeyVo==null) return;
            log.info("更新数据库");
            raffleStock.updateStrategyAwardStock(strategyAwardStockKeyVo.getStrategyId(),strategyAwardStockKeyVo.getAwardId());
        }catch (Exception e){
            log.error("定时任务，更新奖品消耗库存失败",e);
        }
    }
}
