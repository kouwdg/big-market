package com.cheng.domain.strategy.service.armory;


//进行抽奖动作的接口
public interface IStrategyDispatch {
    //根据策略ID进行抽奖
    Integer getRandomAwardId(Long strategyId);

    //根据策略Id与 rule_weight_value进行抽奖
    Integer getRandomAwardId(Long strategyId,String ruleWeightValue);

    //库存的扣减
    public Boolean subtractionAwardStock(Long strategyId,Integer awardId);

}
