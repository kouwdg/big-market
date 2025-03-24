package org.example.domain.strategy.service.award;



import org.example.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 策略奖品接口
 * @date 2024/12/15 22:00
 */
public interface IRaffleAward {

    /**
     *  查询 抽奖的奖品的数据
     * @param strategyId
     * @return
     */
    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);
    List<StrategyAwardEntity> queryRaffleStrategyAwardListByActivityId(Long ActivityId);
}
