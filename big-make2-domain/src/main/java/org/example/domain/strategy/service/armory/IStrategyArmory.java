package org.example.domain.strategy.service.armory;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 策略装配工厂
 * @date 2025/2/14 9:56
 */

public interface IStrategyArmory {
    //根据 策略Id 进行 概率装配
    public boolean assembleLotteryStrategy(Long StrategyId);

    //根据活动Id装配 sku
    boolean assembleActivitySkuByActivityId(Long ActivityId);
    //根据 活动ID 进行 概率装配
    boolean assembleLotteryStrategyByActivityId(Long activityId);
}
