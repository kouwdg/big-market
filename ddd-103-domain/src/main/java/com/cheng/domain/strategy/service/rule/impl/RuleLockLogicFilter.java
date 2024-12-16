package com.cheng.domain.strategy.service.rule.impl;


import com.cheng.domain.strategy.model.entity.RuleActionEntity;
import com.cheng.domain.strategy.model.entity.RuleMatterEntity;
import com.cheng.domain.strategy.model.vo.RuleLogicCheckTypeVo;
import com.cheng.domain.strategy.repository.IStrategyRepository;
import com.cheng.domain.strategy.service.annotation.LogicStrategy;
import com.cheng.domain.strategy.service.rule.ILogicFilter;
import com.cheng.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


//加锁
@Component
@Slf4j
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter  implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    //todo 先暂定用户的抽奖次数
    private Long userRaffleCount=0L;
    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatterEntity) {
        log.info("规则过滤-次数锁 userId:{} strategyId:{} ruleModel:{}",
                ruleMatterEntity.getUserId(),ruleMatterEntity.getStrategyId(),ruleMatterEntity.getRuleModel());

        //查询对应奖品的加锁次数
        String ruleValue=repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(),ruleMatterEntity.getAwardId(),ruleMatterEntity.getRuleModel());
        long raffleCount=Long.parseLong(ruleValue);

        //放行
        if(userRaffleCount>=raffleCount){
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVo.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVo.ALLOW.getInfo())
                    .build();
        }
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVo.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVo.TAKE_OVER.getInfo())
                .build();
    }
}
