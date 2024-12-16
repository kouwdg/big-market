package com.cheng.domain.strategy.service.rule;

import com.cheng.domain.strategy.model.entity.RuleActionEntity;
import com.cheng.domain.strategy.model.entity.RuleMatterEntity;

//抽奖规则过滤接口
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {

    //根据传入的策略,判断该用户是否受到策略的影响，并返回 是否影响策略的信息
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
